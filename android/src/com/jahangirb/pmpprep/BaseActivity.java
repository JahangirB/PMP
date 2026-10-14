package com.jahangirb.pmpprep;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/** Makes sure content and progress are loaded, even when Android restores this screen directly. */
public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data.ensure(this);
        Progress.ensure(this);
        ActionBar ab = getActionBar();
        if (ab != null && showUp()) ab.setDisplayHomeAsUpEnabled(true);
    }

    protected boolean showUp() { return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
