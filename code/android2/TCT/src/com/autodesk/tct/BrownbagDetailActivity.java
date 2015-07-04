package com.autodesk.tct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BrownbagDetailActivity extends AppCompatActivity{
	private String brownbagId;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb_detail);
        Log.d("TCT", "Brownbag deatil activity started");
        Intent intent = getIntent();
        brownbagId = intent.getStringExtra("id");
    }
}
