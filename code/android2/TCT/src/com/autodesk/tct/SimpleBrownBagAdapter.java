package com.autodesk.tct;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.util.DeviceUtility;

public class SimpleBrownBagAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final static int ITEM_VIEW_TYPE_HEADER = 0;
    private final static int ITEM_VIEW_TYPE_FOOTER = 1;
    
    private final static int INVALID_POSITION = -1;

    private List<BrownBag> mBrownBags = new ArrayList<BrownBag>();

    private int mCurrentExpandedPosition = INVALID_POSITION;

    public SimpleBrownBagAdapter() {

    }

    public void setBrownBags(List<BrownBag> brownbags) {
        mBrownBags = new ArrayList<BrownBag>();
        mBrownBags.addAll(brownbags);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mBrownBags != null ? mBrownBags.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mBrownBags.size() - 1) {
            return ITEM_VIEW_TYPE_HEADER;
        }
        return -1;
    }

    public BrownBag getItem(int position) {
        return mBrownBags != null ? mBrownBags.get(position) : null;
    }

    @Override
    public void onBindViewHolder(ViewHolder arg0, int position) {
        int viewType = getItemViewType(position);
        final BrownBag brownBag = getItem(position);
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            final HeaderViewHolder viewHolder = (HeaderViewHolder) arg0;
            viewHolder.mTitleTextView.setText(brownBag.getTitle());
            viewHolder.mTypeTextView.setText("TCT BrownBag");

        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {

        } else {
            ItemViewHolder viewHolder = (ItemViewHolder) arg0;
            viewHolder.mTitleTextView.setText(brownBag.getTitle());
            viewHolder.mDescriptionTextView.setText(brownBag.getDescription());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return createHeaderViewHolder(parent);
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item, parent, false);
            return new ItemViewHolder(v);
        }
    }

    public ViewHolder createHeaderViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_header, parent, false);
        return new HeaderViewHolder(v);
    }

    public ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        return null;
    }

    class ItemViewHolder extends ViewHolder {
        ImageView mLogoImageView;
        TextView mTitleTextView;
        TextView mDescriptionTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mLogoImageView = (ImageView) itemView.findViewById(R.id.item_logo);
            mTitleTextView = (TextView) itemView.findViewById(R.id.item_title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.item_description);
        }

    }

    class HeaderViewHolder extends ViewHolder {
        TextView mTitleTextView;
        TextView mTypeTextView;

        public HeaderViewHolder(View view) {
            super(view);
            mTypeTextView = (TextView) view.findViewById(R.id.item_type);
            mTitleTextView = (TextView) view.findViewById(R.id.item_title);

            // Perhaps the first most crucial part. The ViewPager loses its width information when it is put
            // inside a RecyclerView. It needs to be explicitly resized, in this case to the width of the
            // screen. The height must be provided as a fixed value.
            view.getLayoutParams().width = DeviceUtility.getDeviceWidthPixels();
            view.requestLayout();
        }

    }



}
