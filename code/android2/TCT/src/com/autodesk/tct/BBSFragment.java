package com.autodesk.tct;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class BBSFragment extends Fragment {

    private static final String TAG = "BBS";

    // private View mBrownBagCategory;
    // private View mTrainingCategory;
    // private View mOthersCategory;

    public BBSFragment() {
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_bbs, container, false);
        rootView.findViewById(R.id.brownbag).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "brownbag", Toast.LENGTH_SHORT).show();
            }

        });

        rootView.findViewById(R.id.training).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "training", Toast.LENGTH_SHORT).show();
            }

        });

        rootView.findViewById(R.id.others).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "others", Toast.LENGTH_SHORT).show();
            }

        });

        return rootView;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
		setRetainInstance(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Category");
	}


}
