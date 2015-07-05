package com.autodesk.tct;

import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.server.ServerUtil;
import com.autodesk.tct.server.ServerUtil.BrownbagDetailResponseHandler;
import com.autodesk.tct.server.ServerUtil.BrownbagRegisterResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BrownbagDetailActivity extends AppCompatActivity implements BrownbagDetailResponseHandler, BrownbagRegisterResponseHandler{
	private static BrownBag currentBrownbag;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb_detail);
        Log.d("TCT", "Brownbag deatil activity started");
        Intent intent = getIntent();
        String brownbagId = intent.getStringExtra("BROWNBAG_ID");
        ServerUtil.setBrownbagDetailResponseHandler(this);
        ServerUtil.setBrownbagRegisterResponseHander(this);
        ServerUtil.getBrownbagDetail(brownbagId);
    }
	
	public void registerBrownbag(View view){
		ServerUtil.registerBrownbag(currentBrownbag.getID());
	}
	
	@Override
	public void onBrownbagDetailReceivedSucceed(BrownBag brownbag) {
		currentBrownbag = brownbag;
		TextView titleTextView = (TextView) findViewById(R.id.bb_titile);
		TextView descriptionTextView = (TextView) findViewById(R.id.bb_description);
		titleTextView.setText(currentBrownbag.getTitle());
		descriptionTextView.setText(currentBrownbag.getDescription());
	}
	
	@Override
	public void onBrownbagDetailReceivedFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBrownbagRegisterResponseSucceed(String brownbagId) {
		Toast.makeText(BrownbagDetailActivity.this, "Register Succeeded!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBrownbagRegisterResponseFailed() {
		// TODO Auto-generated method stub
		
	}
}
