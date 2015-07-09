
package com.autodesk.tct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autodesk.tct.RecyclerViewItemTouchListener.OnItemClickListener;
import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.brownbag.BrownBagManager;
import com.autodesk.tct.brownbag.BrownBagManager.BrownBagsDownloadListener;

public class SimpleFeedsFragment extends BaseFragment implements BrownBagsDownloadListener, OnItemClickListener {
    private static final String TAG = "Feeds";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SimpleBrownBagAdapter mAdapter;

    public SimpleFeedsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_feeds_simple, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                pullToRefresh();
            }

        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.feeds);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // this is really important in order to save the state across screen
        // configuration changes for example
        setRetainInstance(true);

        // Set toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.actionbar_title_feeds);

        mAdapter = new SimpleBrownBagAdapter();
        mAdapter.setBrownBags(BrownBagManager.getInstance().getAllBrownBags());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerViewItemTouchListener listener = new RecyclerViewItemTouchListener(getActivity());
        listener.setOnItemClickListener(this);
        mRecyclerView.addOnItemTouchListener(listener);

        BrownBagManager.getInstance().setOnBrownBagsDownloadListener(this);

        // initiate the loader to do the background work
        // getLoaderManager().initLoader(0, null, this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                BrownBagManager.getInstance().downloadBrownBags();
            }
        });
    }

    @Override
    public void onFragmentResume() {
        // Set toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.actionbar_title_feeds);
    }

    private void pullToRefresh() {
        BrownBagManager.getInstance().downloadBrownBags();
    }

    @Override
    public void onAllBrownBagsDownloaded() {
        mAdapter.setBrownBags(BrownBagManager.getInstance().getAllBrownBags());
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClicked(int position) {
        BrownBag brownbag = mAdapter.getItem(position);
        Intent deatilIntent = new Intent(getActivity(), BrownbagDetailActivity.class);
        deatilIntent.putExtra(BrownBag.EXTRA_BROWNBAG_ID, brownbag.getID());
        startActivity(deatilIntent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.feeds, menu);
    }

}
