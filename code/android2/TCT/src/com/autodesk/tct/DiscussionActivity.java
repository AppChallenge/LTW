package com.autodesk.tct;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.brownbag.BrownBagManager;
import com.autodesk.tct.brownbag.Discussion;

public class DiscussionActivity extends AppCompatActivity {
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

        initializeHeaderViews();
        initializeListViews();
    }

    private void initializeHeaderViews() {
        TextView typeTextView = (TextView) findViewById(R.id.item_type);
        typeTextView.setText("TCT BrownBag");
        TextView titleTextView = (TextView) findViewById(R.id.item_title);
        titleTextView.setText(mBrownbag.getTitle());
    }

    private void initializeListViews() {
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new BrownbagDiscussionAdapter(new ArrayList<Discussion>());
    }

    private void pullToRefresh() {
        BrownBagManager.getInstance().downloadBrownBags();
    }

}
