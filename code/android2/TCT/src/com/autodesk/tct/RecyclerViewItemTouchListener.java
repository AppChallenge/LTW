package com.autodesk.tct;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerViewItemTouchListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    private Context mContext;
    private GestureDetector mGestureDetector;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerViewItemTouchListener(Context context) {
        mContext = context;
        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

        if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
            int position = recyclerView.getChildAdapterPosition(child);
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClicked(position);
            }
            return true;

        }

        return false;
    }
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean arg0) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        // TODO Auto-generated method stub
        
    }
    
}

