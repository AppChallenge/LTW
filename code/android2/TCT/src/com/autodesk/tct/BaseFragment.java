package com.autodesk.tct;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

public abstract class BaseFragment extends Fragment {

    public abstract void onFragmentResume();

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected void setActionBarTitle(int resId) {
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resId);
        }
    }
}
