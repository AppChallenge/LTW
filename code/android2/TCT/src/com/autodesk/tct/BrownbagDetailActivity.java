
package com.autodesk.tct;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.brownbag.BrownBagManager;
import com.autodesk.tct.brownbag.BrownBagManager.BrownBagsDownloadListener;
import com.autodesk.tct.brownbag.BrownBagManager.BrownbagDetailResponseHandler;
import com.autodesk.tct.brownbag.BrownBagManager.BrownbagRegisterHandler;
import com.autodesk.tct.widget.CustomAlertDialog;
import com.autodesk.tct.widget.CustomAlertDialog.AlertDialogListener;


public class BrownbagDetailActivity extends AppCompatActivity implements BrownBagsDownloadListener,
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
        setContentView(R.layout.activity_bb_detail);

        // set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.action_bar_title_brownbag);

        initializeViews();

        mBrownbagId = getIntent().getStringExtra(BrownBag.EXTRA_BROWNBAG_ID);
        BrownBag brownbag = BrownBagManager.getInstance().getBrownbagById(mBrownbagId);
        if (brownbag != null) {
            setBrownBag(brownbag);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            BrownBagManager.getInstance().downloadBrownBags();
            BrownBagManager.getInstance().setBrownbagDetailResponseHandler(this);
            BrownBagManager.getInstance().getBrownbagDetail(mBrownbagId);
        }
        BrownBagManager.getInstance().setBrownbagRegisterHander(this);
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
    }

    @Override
    public void onAllBrownBagsDownloaded() {
        BrownBag brownbag = BrownBagManager.getInstance().getBrownbagById(mBrownbagId);
        setBrownBag(brownbag);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRegisterSucceess(String brownbagId) {
        // The brownbagId returned is hard code now and is not the right one.
        // if (!mBrownbag.getID().equals(brownbagId)) {
        // return;
        // }

        mProgressBar.setVisibility(View.GONE);
        CustomAlertDialog.getInstance().setMessage(R.string.msg_register_success);
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

    @Override
    public void onRegisterWaitingList(String brownbagId) {
        // The brownbagId returned is hard code now and is not the right one.
        // if (!mBrownbag.getID().equals(brownbagId)) {
        // return;
        // }

        mProgressBar.setVisibility(View.GONE);
        CustomAlertDialog.getInstance().setMessage(R.string.msg_register_waiting);
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
