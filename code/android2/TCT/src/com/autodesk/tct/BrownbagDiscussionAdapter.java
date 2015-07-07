
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

import com.autodesk.tct.brownbag.Discussion;

public class BrownbagDiscussionAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final static int INVALID_POSITION = -1;

    private List<Discussion> mDiscussions = new ArrayList<Discussion>();

    private int mCurrentExpandedPosition = INVALID_POSITION;

    public BrownbagDiscussionAdapter(List<Discussion> discussions) {
        setDiscussions(discussions);
    }

    public void setDiscussions(List<Discussion> discussions) {
        mDiscussions = new ArrayList<Discussion>();
        mDiscussions.addAll(discussions);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDiscussions != null ? mDiscussions.size() : 0;
    }

    public Discussion getItem(int position) {
        return mDiscussions != null ? mDiscussions.get(position) : null;
    }

    @Override
    public void onBindViewHolder(ViewHolder arg0, int position) {
        int viewType = getItemViewType(position);
        final Discussion discussion = getItem(position);
        ItemViewHolder viewHolder = (ItemViewHolder) arg0;
        // viewHolder.mTitleTextView.setText(brownBag.getTitle());
        // viewHolder.mDescriptionTextView.setText(brownBag.getDescription());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item, parent, false);
        return new ItemViewHolder(v);
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

}
