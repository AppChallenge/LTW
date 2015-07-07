package com.autodesk.tct;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autodesk.tct.brownbag.BrownBagManager;
import com.marshalchen.ultimaterecyclerview.CustomUltimateRecyclerview;

public class FeedsFragment extends Fragment {
    private static final String TAG = "Feeds";

    private CustomUltimateRecyclerview mRecyclerView;
    private SimpleBrownBagAdapter mAdapter;

    private final WeakReference<Context> mContext;

    public FeedsFragment() {
        mContext = new WeakReference<Context>(getActivity());
        BrownBagManager.getInstance().downloadBrownBags();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_feeds, container, false);
        initializeViews(rootView, inflater);
        return rootView;
    }

    private void initializeViews(View rootView, LayoutInflater inflater) {
        mRecyclerView = (CustomUltimateRecyclerview) rootView.findViewById(R.id.ultimate_recyclerview);
        // load more
        mRecyclerView.enableLoadmore();

        // mRecyclerView.setCustomLoadMoreView(inflater.inflate(R.layout.custom_bottom_progressbar, null));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext.get(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new SimpleBrownBagAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setCustomSwipeToRefresh();
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
		setRetainInstance(true);
	}
}
