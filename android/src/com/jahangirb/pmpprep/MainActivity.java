package com.jahangirb.pmpprep;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/*
 * A WebView shell around index.html.
 *
 * The app and packs/ are served from the APK's assets under a fixed https
 * origin rather than file://. That matters for three things the web app does:
 * the extended pack loader only runs when the page is served over http(s);
 * localStorage and IndexedDB need a stable origin so progress and imported
 * packs survive restarts and APK updates; and fetch() of packs/*.json works
 * like it does on a web host.
 *
 * Imports go through the page's own file picker (onShowFileChooser).
 * Exports are Blob downloads in the page, which a WebView ignores, so a small
 * script hands the text to saveFile() below and Android's "save as" sheet
 * writes it wherever the user chooses.
 */
public class MainActivity extends Activity {
    private static final String HOST = "appassets.androidplatform.net";
    private static final String START_URL = "https://" + HOST + "/index.html";
    private static final int REQ_OPEN = 1;
    private static final int REQ_SAVE = 2;

    private static final String DOWNLOAD_HOOK =
        "(function () {" +
        "  if (window.__pmpAndroidHook) return; window.__pmpAndroidHook = true;" +
        "  var click = HTMLAnchorElement.prototype.click;" +
        "  HTMLAnchorElement.prototype.click = function () {" +
        "    var href = this.href || '';" +
        "    if (this.hasAttribute('download') && (href.indexOf('blob:') === 0 || href.indexOf('data:') === 0)) {" +
        "      var name = this.getAttribute('download') || 'export.json';" +
        "      fetch(href).then(function (r) { return r.blob(); }).then(function (b) {" +
        "        return b.text().then(function (t) { PMPAndroid.saveFile(name, t, b.type || 'application/json'); });" +
        "      }).catch(function (e) { PMPAndroid.toast('Export failed: ' + e); });" +
        "      return;" +
        "    }" +
        "    return click.apply(this, arguments);" +
        "  };" +
        "})();";

    private WebView web;
    private ValueCallback<Uri[]> pendingChooser;
    private String pendingSaveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        s.setSupportMultipleWindows(false);
        s.setMediaPlaybackRequiresUserGesture(true);

        web.addJavascriptInterface(new Bridge(), "PMPAndroid");
        web.setWebViewClient(new Client());
        web.setWebChromeClient(new Chrome());

        if (savedInstanceState == null || web.restoreState(savedInstanceState) == null) {
            web.loadUrl(START_URL);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        web.saveState(out);
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (web != null) web.destroy();
        super.onDestroy();
    }

    private class Client extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest req) {
            Uri u = req.getUrl();
            if (!HOST.equals(u.getHost())) return null;
            String path = u.getPath();
            if (path == null || path.equals("/") || path.isEmpty()) path = "/index.html";
            path = path.substring(1);
            if (path.contains("..")) return notFound();
            try {
                InputStream in = getAssets().open(path, AssetManager.ACCESS_STREAMING);
                WebResourceResponse r = new WebResourceResponse(mime(path), "utf-8", in);
                r.setResponseHeaders(Collections.singletonMap("Cache-Control", "no-cache"));
                return r;
            } catch (FileNotFoundException e) {
                return notFound();
            } catch (IOException e) {
                return notFound();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
            Uri u = req.getUrl();
            if (HOST.equals(u.getHost())) return false;
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, u));
            } catch (ActivityNotFoundException e) {
                toast("No app can open " + u);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.evaluateJavascript(DOWNLOAD_HOOK, null);
        }
    }

    private class Chrome extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
            if (pendingChooser != null) pendingChooser.onReceiveValue(null);
            pendingChooser = callback;
            // "*/*" rather than application/json: many file managers label
            // .json files as octet-stream and would hide them otherwise.
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            try {
                startActivityForResult(i, REQ_OPEN);
            } catch (ActivityNotFoundException e) {
                pendingChooser = null;
                toast("No file picker available");
                return false;
            }
            return true;
        }
    }

    private class Bridge {
        @JavascriptInterface
        public void saveFile(final String name, final String text, final String mimeType) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pendingSaveText = text;
                    Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType(mimeType == null || mimeType.isEmpty() ? "application/json" : mimeType);
                    i.putExtra(Intent.EXTRA_TITLE, name);
                    try {
                        startActivityForResult(i, REQ_SAVE);
                    } catch (ActivityNotFoundException e) {
                        pendingSaveText = null;
                        toast("No app can save files; use Copy in the app instead");
                    }
                }
            });
        }

        @JavascriptInterface
        public void toast(final String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() { MainActivity.this.toast(msg); }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_OPEN) {
            if (pendingChooser != null) {
                Uri[] result = null;
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    result = new Uri[] { data.getData() };
                }
                pendingChooser.onReceiveValue(result);
                pendingChooser = null;
            }
            return;
        }
        if (requestCode == REQ_SAVE) {
            String text = pendingSaveText;
            pendingSaveText = null;
            if (resultCode != RESULT_OK || data == null || data.getData() == null || text == null) return;
            try {
                OutputStream out = getContentResolver().openOutputStream(data.getData(), "wt");
                if (out == null) throw new IOException("no stream");
                try {
                    out.write(text.getBytes(StandardCharsets.UTF_8));
                } finally {
                    out.close();
                }
                toast("Saved");
            } catch (IOException e) {
                toast("Could not save: " + e.getMessage());
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private static WebResourceResponse notFound() {
        WebResourceResponse r = new WebResourceResponse("text/plain", "utf-8",
            new java.io.ByteArrayInputStream(new byte[0]));
        r.setStatusCodeAndReasonPhrase(404, "Not Found");
        return r;
    }

    private static String mime(String path) {
        String p = path.toLowerCase();
        if (p.endsWith(".html") || p.endsWith(".htm")) return "text/html";
        if (p.endsWith(".json")) return "application/json";
        if (p.endsWith(".js")) return "text/javascript";
        if (p.endsWith(".css")) return "text/css";
        if (p.endsWith(".svg")) return "image/svg+xml";
        if (p.endsWith(".png")) return "image/png";
        return "application/octet-stream";
    }
}
