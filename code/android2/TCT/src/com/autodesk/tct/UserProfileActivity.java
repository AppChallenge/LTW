package com.autodesk.tct;

import android.os.Bundle;
import android.view.Menu;

import com.autodesk.tct.authentication.User;
import com.autodesk.tct.authentication.UserUtility;
import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.brownbag.BrownBagManager;


public class UserProfileActivity extends BaseActivity {
    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

        String userId = getIntent().getStringExtra(User.EXTRA_USER_ID);
        String brownbagId = getIntent().getStringExtra(BrownBag.EXTRA_BROWNBAG_ID);
        BrownBag brownbag = BrownBagManager.getInstance().getBrownbagById(brownbagId);
        mUser = brownbag.getSpeakerById(userId);
        gotoUserProfile();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_userprofile;
    }

    @Override
    protected void setToolbar() {
        super.setToolbar();
        getSupportActionBar().setTitle(R.string.action_bar_title_userprofile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
