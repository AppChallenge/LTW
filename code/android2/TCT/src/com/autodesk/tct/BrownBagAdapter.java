package com.autodesk.tct;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.autodesk.tct.brownbag.BrownBag;

public class BrownBagAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<BrownBag> mBrownBags = new ArrayList<BrownBag>();

    public void setBrownBags(List<BrownBag> brownbags) {
        mBrownBags = new ArrayList<BrownBag>();
        mBrownBags.addAll(brownbags);
    }

    @Override
    public int getItemCount() {
        return mBrownBags.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        return null;
    }

}
