package com.autodesk.tct;

import com.autodesk.tct.server.ServerUtil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BrownbagDetailActivity extends AppCompatActivity{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb_detail);
        Log.d("TCT", "Brownbag deatil activity started");
        
    }
}
