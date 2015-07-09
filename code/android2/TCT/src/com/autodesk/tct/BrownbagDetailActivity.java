
package com.autodesk.tct;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autodesk.tct.authentication.UserUtility;
import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.brownbag.BrownBagManager;
import com.autodesk.tct.brownbag.BrownBagManager.BrownBagsDownloadListener;
import com.autodesk.tct.brownbag.BrownBagManager.BrownbagDetailResponseHandler;
import com.autodesk.tct.brownbag.BrownBagManager.BrownbagRegisterHandler;
import com.autodesk.tct.brownbag.Registration;
import com.autodesk.tct.brownbag.Registration.Role;
import com.autodesk.tct.widget.CustomAlertDialog;
import com.autodesk.tct.widget.CustomAlertDialog.AlertDialogListener;


public class BrownbagDetailActivity extends BaseActivity implements BrownBagsDownloadListener,
        BrownbagRegisterHandler, BrownbagDetailResponseHandler {
    private String mBrownbagId;
    private BrownBag mBrownbag;

    private TextView mTimeView;
    private TextView mTitleView;
    private TextView mSummaryView;
    private TextView mSpeakersView;
    private FloatingActionButton mRegisterBtn;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeViews();

        getSupportActionBar().setTitle(R.string.action_bar_title_brownbag);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mBrownbagId = getIntent().getStringExtra(BrownBag.EXTRA_BROWNBAG_ID);
        BrownBag brownbag = BrownBagManager.getInstance().getBrownbagById(mBrownbagId);
        if (brownbag != null) {
            setBrownBag(brownbag);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            BrownBagManager.getInstance().setOnBrownBagsDownloadListener(this);
            BrownBagManager.getInstance().downloadBrownBags();
            // BrownBagManager.getInstance().setBrownbagDetailResponseHandler(this);
            // BrownBagManager.getInstance().getBrownbagDetail(mBrownbagId);
        }
        BrownBagManager.getInstance().setBrownbagRegisterHander(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bb_detail;
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

    private void initializeViews() {
        mTimeView = (TextView) findViewById(R.id.item_date);
        mTitleView = (TextView) findViewById(R.id.item_title);
        mSummaryView = (TextView) findViewById(R.id.item_summary);
        mSpeakersView = (TextView) findViewById(R.id.item_speakers);
        findViewById(R.id.speakers_group).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrownbagDetailActivity.this, SpeakerActivity.class);
                intent.putExtra(BrownBag.EXTRA_BROWNBAG_ID, mBrownbagId);
                startActivity(intent);
            }

        });
        findViewById(R.id.materials_group).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }

        });
        findViewById(R.id.discussion_group).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrownbagDetailActivity.this, DiscussionActivity.class);
                intent.putExtra(BrownBag.EXTRA_BROWNBAG_ID, mBrownbagId);
                startActivity(intent);
            }

        });
        mRegisterBtn = (FloatingActionButton) findViewById(R.id.register_btn);
        mRegisterBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mBrownbag != null) {
                    registerBrownbag();
                }
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    private void setBrownBag(BrownBag brownbag) {
        mBrownbag = brownbag;
        initializeData();
    }

    private void initializeData() {
        if (mBrownbag == null) {
            return;
        }
        mTimeView.setText(mBrownbag.getDateString());
        mTitleView.setText(mBrownbag.getTitle());
        mSummaryView.setText(mBrownbag.getDescription());
        mSpeakersView.setText(mBrownbag.getSpeakersName());
        
        if (mBrownbag.isAllowToRegister()) {
            showRegisterButton();
        } else {
            hideRegisterButton();
        }
    }

    private void showRegisterButton() {
        Animation fabIn = AnimationUtils.loadAnimation(this, R.anim.fab_in);
        fabIn.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mRegisterBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRegisterBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

        });
        mRegisterBtn.startAnimation(fabIn);
    }

    private void hideRegisterButton() {
        Animation fabOut = AnimationUtils.loadAnimation(this, R.anim.fab_out);
        fabOut.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRegisterBtn.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

        });
        mRegisterBtn.startAnimation(fabOut);
    }

    private void writeToCalendar() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(Events.TITLE, mBrownbag.getTitle());
        intent.putExtra(Events.DESCRIPTION, mBrownbag.getDescription());
        intent.putExtra(Events.EVENT_LOCATION, mBrownbag.getLocation());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, mBrownbag.getStartDate().getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, mBrownbag.getEndDate().getTime());
        intent.putExtra(Events.ALL_DAY, true);
        startActivity(intent);
    }

    @Override
    public void onAllBrownBagsDownloaded() {
        BrownBag brownbag = BrownBagManager.getInstance().getBrownbagById(mBrownbagId);
        setBrownBag(brownbag);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBrownBagsDownloadFailed() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRegisterSucceess(String brownbagId) {
        // The brownbagId returned is hard code now and is not the right one.
        // if (!mBrownbag.getID().equals(brownbagId)) {
        // return;
        // }

        Registration reg = new Registration(mBrownbag.getID(), UserUtility.getCurrentUser(), Role.Audience);
        mBrownbag.appendRegistration(reg);

        mProgressBar.setVisibility(View.GONE);
        CustomAlertDialog.getInstance().setMessage(R.string.msg_register_success);
        CustomAlertDialog.getInstance().setButton(R.string.addToMyCalender, R.string.isee, new AlertDialogListener() {

            @Override
            public void onDialogPositiveBtnClicked(DialogFragment dialog) {
                hideRegisterButton();
                writeToCalendar();
                dialog.dismiss();
            }

            @Override
            public void onDialogNegtiveBtnClicked(DialogFragment dialog) {
                hideRegisterButton();
                dialog.dismiss();
            }

        });
        showCustomDialog(CustomAlertDialog.getInstance());
    }

    @Override
    public void onRegisterWaitingList(String brownbagId) {
        // The brownbagId returned is hard code now and is not the right one.
        // if (!mBrownbag.getID().equals(brownbagId)) {
        // return;
        // }

        Registration reg = new Registration(mBrownbag.getID(), UserUtility.getCurrentUser(), Role.Audience);
        mBrownbag.appendRegistration(reg);

        mProgressBar.setVisibility(View.GONE);
        CustomAlertDialog.getInstance().setMessage(R.string.msg_register_waiting);
        CustomAlertDialog.getInstance().setButton(R.string.addToMyCalender, R.string.isee, new AlertDialogListener() {

            @Override
            public void onDialogPositiveBtnClicked(DialogFragment dialog) {
                hideRegisterButton();
                writeToCalendar();
                dialog.dismiss();
            }

            @Override
            public void onDialogNegtiveBtnClicked(DialogFragment dialog) {
                hideRegisterButton();
                dialog.dismiss();
            }

        });
        showCustomDialog(CustomAlertDialog.getInstance());
    }

    @Override
    public void onRegisterFailure() {
        mProgressBar.setVisibility(View.GONE);
        CustomAlertDialog.getInstance().setMessage(R.string.msg_register_failure);
        CustomAlertDialog.getInstance().setHasActionView(false);
        CustomAlertDialog.getInstance().setButton(R.string.addToMyCalender, R.string.isee, new AlertDialogListener() {

            @Override
            public void onDialogPositiveBtnClicked(DialogFragment dialog) {
                dialog.dismiss();
            }

            @Override
            public void onDialogNegtiveBtnClicked(DialogFragment dialog) {
                dialog.dismiss();
            }

        });
        showCustomDialog(CustomAlertDialog.getInstance());

    }

    private void showCustomDialog(CustomAlertDialog dialog) {
        dialog.show(getFragmentManager(), CustomAlertDialog.TAG);
    }
	
    public void registerBrownbag() {
        mProgressBar.setVisibility(View.VISIBLE);
        BrownBagManager.getInstance().registerABrownBag(mBrownbag.getID());
	}
	
	@Override
	public void onBrownbagDetailReceivedSucceed(BrownBag brownbag) {
        mBrownbag = brownbag;
	}
	
	@Override
	public void onBrownbagDetailReceivedFailed() {
		
	}
}
