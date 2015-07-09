package com.autodesk.tct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.autodesk.tct.notification.NotificationManager;

public abstract class BaseActivity extends AppCompatActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        setToolbar();
    }

    protected abstract int getLayoutResId();

    /**
     * Must call super method
     */
    protected void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); 
        setSupportActionBar(toolbar); 
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(this.toString(), "onResume");
        if (NotificationManager.getInstance().wantToShowBrownbagDetail()) {
            NotificationManager.getInstance().redirectToBrownbagDetail(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(this.toString(), "onStop");
        if (NotificationManager.getInstance().wantToShowBrownbagDetail()) {
            NotificationManager.getInstance().setWantToShowBrownbagDetail(false, "");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
