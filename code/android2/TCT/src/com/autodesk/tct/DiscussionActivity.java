package com.autodesk.tct;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.brownbag.BrownBagManager;
import com.autodesk.tct.brownbag.BrownBagManager.BrownbagDiscussionPostHandler;
import com.autodesk.tct.brownbag.BrownBagManager.DownloadBrownbagDiscussionHandler;
import com.autodesk.tct.brownbag.Discussion;

public class DiscussionActivity extends AppCompatActivity implements DownloadBrownbagDiscussionHandler,
        BrownbagDiscussionPostHandler {
    private static final String TAG = "DiscussionActivity";

    private BrownBag mBrownbag;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private BrownbagDiscussionAdapter mAdapter;


    public DiscussionActivity() {
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        // set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.action_bar_title_dicussion);

        String brownbagId = getIntent().getStringExtra(BrownBag.EXTRA_BROWNBAG_ID);
        mBrownbag = BrownBagManager.getInstance().getBrownbagById(brownbagId);

        initializeViews();
    }

    private void initializeViews() {
        TextView typeTextView = (TextView) findViewById(R.id.item_type);
        typeTextView.setText("TCT BrownBag");
        TextView titleTextView = (TextView) findViewById(R.id.item_title);
        titleTextView.setText(mBrownbag.getTitle());

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                pullToRefresh();
            }

        });
        mRecyclerView = (RecyclerView) findViewById(R.id.discussion_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BrownbagDiscussionAdapter(new ArrayList<Discussion>());
        mRecyclerView.setAdapter(mAdapter);

        final EditText inputBox = (EditText) findViewById(R.id.input_box);
        inputBox.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage(v.getText().toString());
                    v.setText("");
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void sendMessage(String message) {
        BrownBagManager.getInstance().postBrownbagDiscussion(mBrownbag.getID(), message);
    }

    @Override
    public void onStart() {
        super.onStart();
        BrownBagManager.getInstance().setDownloadBrownbagDiscussionHandler(this);
        BrownBagManager.getInstance().setBrownbagDiscussionPostHandler(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                BrownBagManager.getInstance().getBrownbagDiscussions(mBrownbag.getID());
            }
        });
    }

    private void pullToRefresh() {
        BrownBagManager.getInstance().getBrownbagDiscussions(mBrownbag.getID());
    }

    @Override
    public void onBrownbagDiscussionPostSucceed(Discussion discussion) {
        mAdapter.appendDiscussion(discussion);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onBrownbagDiscussionPostFailed() {

    }

    @Override
    public void onBrownbagDiccussionsReceivedSucceed(List<Discussion> discussions) {
        mAdapter.setDiscussions(discussions);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBrownbagDiccussionsReceivedFailed() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
