package com.autodesk.tct;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FeedsFragment extends Fragment implements OnRefreshListener {
    private static final String TAG = "Feeds";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private BrownBagAdapter mAdapter;

    private final WeakReference<Context> mContext;

    public FeedsFragment() {
        mContext = new WeakReference<Context>(getActivity());
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_feeds, container, false);
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext.get(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new BrownBagAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
		setRetainInstance(true);

		// you only need to instantiate these the first time your fragment is
		// created; then, the method above will do the rest
        // if (mAdapter == null) {
        // mItems = new ArrayList<String>();
        // mAdapter = new MyAdapter(getActivity(), mItems);
        // }
        // getListView().setAdapter(mAdapter);
        //
        // // initiate the loader to do the background work
        // getLoaderManager().initLoader(0, null, this);
	}

    @Override
    public void onRefresh() {
        Toast.makeText(mContext.get(), "Start to refresh", Toast.LENGTH_SHORT).show();
    }

    private void pullToRefresh() {

    }

    private void onRefreshFinished() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // @Override
    // public Loader<Void> onCreateLoader(int id, Bundle args) {
    // AsyncTaskLoader<Void> loader = new AsyncTaskLoader<Void>(getActivity()) {
    //
    // @Override
    // public Void loadInBackground() {
    // try {
    // // simulate some time consuming operation going on in the
    // // background
    // Thread.sleep(SLEEP);
    // } catch (InterruptedException e) {
    // }
    // return null;
    // }
    // };
    // // somehow the AsyncTaskLoader doesn't want to start its job without
    // // calling this method
    // loader.forceLoad();
    // return loader;
    // }

    // @Override
    // public void onLoadFinished(Loader<Void> loader, Void result) {
    //
    // // add the new item and let the adapter know in order to refresh the
    // // views
    // mItems.add(TabsFragment.TAB_WORDS.equals(mTag) ? WORDS[mPosition]
    // : NUMBERS[mPosition]);
    // mAdapter.notifyDataSetChanged();
    //
    // // advance in your list with one step
    // mPosition++;
    // if (mPosition < mTotal - 1) {
    // getLoaderManager().restartLoader(0, null, this);
    // Log.d(TAG, "onLoadFinished(): loading next...");
    // } else {
    // Log.d(TAG, "onLoadFinished(): done loading!");
    // }
    // }
    //
    // @Override
    // public void onLoaderReset(Loader<Void> loader) {
    // }

    // private class MyAdapter extends ArrayAdapter<String> {
    //
    // public MyAdapter(Context context, List<String> objects) {
    // super(context, R.layout.list_item, R.id.text, objects);
    // }
    //
    // @Override
    // public View getView(int position, View convertView, ViewGroup parent) {
    // View view = convertView;
    // Wrapper wrapper;
    //
    // if (view == null) {
    // view = mInflater.inflate(R.layout.list_item, null);
    // wrapper = new Wrapper(view);
    // view.setTag(wrapper);
    // } else {
    // wrapper = (Wrapper) view.getTag();
    // }
    //
    // wrapper.getTextView().setText(getItem(position));
    // wrapper.getBar().setBackgroundColor(
    // mTag == TabsFragment.TAB_WORDS ? getResources().getColor(
    // wordBarColor) : getResources().getColor(
    // numberBarColor));
    // return view;
    // }
    //
    // }
    //
    // // use an wrapper (or view holder) object to limit calling the
    // // findViewById() method, which parses the entire structure of your
    // // XML in search for the ID of your view
    // private class Wrapper {
    // private final View mRoot;
    // private TextView mText;
    // private View mBar;
    //
    // public Wrapper(View root) {
    // mRoot = root;
    // }
    //
    // public TextView getTextView() {
    // if (mText == null) {
    // mText = (TextView) mRoot.findViewById(R.id.text);
    // }
    // return mText;
    // }
    //
    // public View getBar() {
    // if (mBar == null) {
    // mBar = mRoot.findViewById(R.id.bar);
    // }
    // return mBar;
    // }
    // }
}
