package com.jahangirb.pmpprep;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The question banks: switch any on or off, import new ones from .json files,
 * delete imported ones, and back up or restore progress.
 */
public class PacksActivity extends BaseActivity {
    private static final int REQ_IMPORT = 1, REQ_EXPORT = 2, REQ_RESTORE = 3;
    private boolean busy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Question banks");
        build();
        handleIncoming(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIncoming(intent);
    }

    /** A .json file opened or shared into the app from another app. */
    private void handleIncoming(Intent in) {
        if (in == null) return;
        List<Uri> uris = new ArrayList<Uri>();
        if (Intent.ACTION_VIEW.equals(in.getAction()) && in.getData() != null) uris.add(in.getData());
        if (Intent.ACTION_SEND.equals(in.getAction())) {
            Uri u = in.getParcelableExtra(Intent.EXTRA_STREAM);
            if (u != null) uris.add(u);
            else if (in.getStringExtra(Intent.EXTRA_TEXT) != null) { importTexts(texts(in.getStringExtra(Intent.EXTRA_TEXT)), names("shared")); return; }
        }
        if (!uris.isEmpty()) importUris(uris);
    }

    private void build() {
        Ui.Pal p = Ui.pal(this);
        LinearLayout col = Ui.page(this);
        col.addView(Ui.muted(this, "Turn a bank off to leave its questions out of practice and mock exams. "
            + "Add your own bank from a .json question pack file (the same format as the files in packs/; see AUTHORING.md)."));
        col.addView(Ui.gap(this, 12));
        col.addView(Ui.button(this, "Add a question bank (.json)", true, new Runnable() {
            @Override public void run() { pickImport(); }
        }), Ui.margins(this, 0, 0, 0, 16));

        int total = 0;
        for (Data.Pack pk : Data.packs) if (Data.isEnabled(pk)) total += pk.qs.size();
        col.addView(Ui.h2(this, total + " questions enabled"));

        boolean userHeader = false;
        for (final Data.Pack pk : Data.packs) {
            if (!pk.builtin && !userHeader) { col.addView(Ui.h2(this, "Your imported banks")); userHeader = true; }
            LinearLayout c = Ui.card(this);
            LinearLayout r = Ui.row(this);
            LinearLayout info = Ui.column(this);
            info.addView(Ui.text(this, pk.name, 15, p.text, true));
            String sub = pk.qs.size() + " questions";
            if (pk.skippedDuplicates > 0) sub += " · " + pk.skippedDuplicates + " duplicates skipped";
            if (pk.invalid > 0) sub += " · " + pk.invalid + " invalid skipped";
            info.addView(Ui.muted(this, sub));
            r.addView(info, Ui.weight(1));
            Switch sw = new Switch(this);
            sw.setChecked(Data.isEnabled(pk));
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override public void onCheckedChanged(CompoundButton b, boolean on) { Data.setEnabled(PacksActivity.this, pk, on); }
            });
            r.addView(sw);
            c.addView(r);
            if (!pk.builtin) {
                c.addView(Ui.button(this, "Delete this bank", false, new Runnable() {
                    @Override public void run() { confirmDelete(pk); }
                }), Ui.margins(this, 0, 10, 0, 0));
            }
            col.addView(c);
        }

        col.addView(Ui.h2(this, "Backup"));
        col.addView(Ui.muted(this, "Your answers, flags and mock results stay on this phone. Save a backup to move them to another phone or keep them safe."));
        col.addView(Ui.gap(this, 8));
        col.addView(Ui.button(this, "Save progress backup", false, new Runnable() {
            @Override public void run() {
                Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("application/json");
                i.putExtra(Intent.EXTRA_TITLE, "pmp-progress-" + new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(new java.util.Date()) + ".json");
                startActivityForResult(i, REQ_EXPORT);
            }
        }), Ui.margins(this, 0, 0, 0, 8));
        col.addView(Ui.button(this, "Restore progress from backup", false, new Runnable() {
            @Override public void run() { startActivityForResult(openIntent(false), REQ_RESTORE); }
        }), Ui.margins(this, 0, 0, 0, 8));
        col.addView(Ui.button(this, "Reset all progress", false, new Runnable() {
            @Override public void run() {
                new AlertDialog.Builder(PacksActivity.this).setTitle("Reset all progress?")
                    .setMessage("This deletes every answer, flag and mock result on this phone. Question banks are kept.")
                    .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface d, int w) {
                            Progress.reset(PacksActivity.this);
                            Session.clearSaved(PacksActivity.this);
                            Toast.makeText(PacksActivity.this, "Progress reset", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Cancel", null).show();
            }
        }));
    }

    private void confirmDelete(final Data.Pack pk) {
        new AlertDialog.Builder(this).setTitle("Delete \"" + pk.name + "\"?")
            .setMessage("Its " + pk.qs.size() + " questions are removed from the app. Your answer history is kept, and comes back if you import the same file again.")
            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface d, int w) { Data.deletePack(PacksActivity.this, pk); build(); }
            }).setNegativeButton("Cancel", null).show();
    }

    private Intent openIntent(boolean multiple) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        // Many file managers label .json as octet-stream, so do not filter by type.
        i.setType("*/*");
        if (multiple) i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        return i;
    }

    private void pickImport() {
        if (busy) return;
        startActivityForResult(openIntent(true), REQ_IMPORT);
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res != RESULT_OK || data == null) return;
        if (req == REQ_IMPORT) {
            List<Uri> uris = new ArrayList<Uri>();
            ClipData cd = data.getClipData();
            if (cd != null) for (int i = 0; i < cd.getItemCount(); i++) uris.add(cd.getItemAt(i).getUri());
            else if (data.getData() != null) uris.add(data.getData());
            importUris(uris);
        } else if (req == REQ_EXPORT && data.getData() != null) {
            try {
                OutputStream out = getContentResolver().openOutputStream(data.getData(), "wt");
                try { out.write(Progress.toJson().toString(1).getBytes(StandardCharsets.UTF_8)); } finally { out.close(); }
                Toast.makeText(this, "Backup saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                alert("Could not save", e.getMessage());
            }
        } else if (req == REQ_RESTORE && data.getData() != null) {
            try {
                Progress.restore(this, read(data.getData()));
                Toast.makeText(this, "Progress restored", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                alert("Could not restore", e.getMessage());
            }
        }
    }

    private List<String> texts(String t) { List<String> l = new ArrayList<String>(); l.add(t); return l; }
    private List<String> names(String n) { List<String> l = new ArrayList<String>(); l.add(n); return l; }

    private void importUris(final List<Uri> uris) {
        List<String> texts = new ArrayList<String>(), names = new ArrayList<String>();
        for (Uri u : uris) {
            try {
                texts.add(read(u));
                names.add(displayName(u));
            } catch (Exception e) {
                alert("Could not read the file", String.valueOf(e.getMessage()));
                return;
            }
        }
        importTexts(texts, names);
    }

    private void importTexts(final List<String> texts, final List<String> names) {
        busy = true;
        final android.app.ProgressDialog pd = android.app.ProgressDialog.show(this, null, "Importing…", true, false);
        new Thread(new Runnable() {
            @Override public void run() {
                final StringBuilder msg = new StringBuilder();
                int added = 0;
                for (int i = 0; i < texts.size(); i++) {
                    String name = names.get(i);
                    try {
                        Data.Pack pk = Data.importPack(getApplicationContext(), texts.get(i), name);
                        added += pk.qs.size();
                        msg.append("✓ ").append(pk.name).append(": ").append(pk.qs.size()).append(" questions added");
                        if (pk.skippedDuplicates > 0) msg.append(", ").append(pk.skippedDuplicates).append(" already in the app (skipped)");
                        if (pk.invalid > 0) {
                            msg.append(", ").append(pk.invalid).append(" invalid (skipped):\n   ").append(Data.join(pk.errors, "\n   "));
                        }
                        msg.append("\n\n");
                    } catch (Exception e) {
                        msg.append("✗ ").append(name).append(": ").append(e.getMessage()).append("\n\n");
                    }
                }
                final int fAdded = added;
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        busy = false;
                        pd.dismiss();
                        build();
                        alert(fAdded > 0 ? fAdded + " questions added" : "Nothing imported", msg.toString().trim());
                    }
                });
            }
        }).start();
    }

    private String read(Uri u) throws Exception {
        InputStream in = getContentResolver().openInputStream(u);
        if (in == null) throw new Exception("The file could not be opened.");
        try { return Data.readAll(in); } finally { in.close(); }
    }

    private String displayName(Uri u) {
        String name = null;
        try {
            Cursor c = getContentResolver().query(u, new String[] { OpenableColumns.DISPLAY_NAME }, null, null, null);
            if (c != null) {
                try { if (c.moveToFirst()) name = c.getString(0); } finally { c.close(); }
            }
        } catch (Exception e) { /* fall through */ }
        if (name == null) name = u.getLastPathSegment();
        return name == null ? "pack" : name;
    }

    private void alert(String title, String msg) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton("OK", null).show();
    }
}
