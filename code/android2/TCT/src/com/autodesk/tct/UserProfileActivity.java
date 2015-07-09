package com.autodesk.tct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.autodesk.tct.authentication.User;
import com.autodesk.tct.authentication.UserUtility;
import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.brownbag.BrownBagManager;


public class UserProfileActivity extends AppCompatActivity {
    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        // set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.action_bar_title_userprofile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String userId = getIntent().getStringExtra(User.EXTRA_USER_ID);
        String brownbagId = getIntent().getStringExtra(BrownBag.EXTRA_BROWNBAG_ID);
        BrownBag brownbag = BrownBagManager.getInstance().getBrownbagById(brownbagId);
        mUser = brownbag.getSpeakerById(userId);
        gotoUserProfile();
    	
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return UserUtility.isCurrentUser(mUser);
    }

    private void gotoUserProfile() {
        getSupportFragmentManager().beginTransaction().add(R.id.container, new UserProfileFragment(mUser))
                .commit();
    }
}
