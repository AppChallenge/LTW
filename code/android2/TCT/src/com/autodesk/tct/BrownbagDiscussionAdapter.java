
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
    private final static int DISCUSSION_TYPE_MINE = 0;
    private final static int DISCUSSION_TYPE_OTHERS = 1;

    private List<Discussion> mDiscussions = new ArrayList<Discussion>();

    public BrownbagDiscussionAdapter(List<Discussion> discussions) {
        setDiscussions(discussions);
    }

    public void setDiscussions(List<Discussion> discussions) {
        mDiscussions = new ArrayList<Discussion>();
        mDiscussions.addAll(discussions);
        notifyDataSetChanged();
    }

    public void appendDiscussion(Discussion discussion) {
        int position = mDiscussions.size() - 1;
        mDiscussions.add(discussion);
        notifyItemInserted(mDiscussions.size() - 1);
    }

    @Override
    public int getItemCount() {
        return mDiscussions != null ? mDiscussions.size() : 0;
    }

    public Discussion getItem(int position) {
        return mDiscussions != null ? mDiscussions.get(position) : null;
    }

    @Override
    public int getItemViewType(int position) {
        Discussion discussion = getItem(position);
        if (discussion.isMyOwnReply()) {
            return DISCUSSION_TYPE_MINE;
        } else {
            return DISCUSSION_TYPE_OTHERS;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder arg0, int position) {
        final Discussion discussion = getItem(position);
        ItemViewHolder viewHolder = (ItemViewHolder) arg0;
        viewHolder.mNameTextView.setText(discussion.getUserName());
        viewHolder.mMessageTextView.setText(discussion.getMessage());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == DISCUSSION_TYPE_MINE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_list_item_mine, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussion_list_item, parent, false);
        }
        return new ItemViewHolder(v);
    }

    class ItemViewHolder extends ViewHolder {
        ImageView mAvatarImageView;
        TextView mNameTextView;
        TextView mMessageTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mAvatarImageView = (ImageView) itemView.findViewById(R.id.item_avatar);
            mNameTextView = (TextView) itemView.findViewById(R.id.poster_name);
            mMessageTextView = (TextView) itemView.findViewById(R.id.item_message);
        }

    }

}
