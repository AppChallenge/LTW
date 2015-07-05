package com.autodesk.tct;

import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.server.ServerUtil;
import com.autodesk.tct.server.ServerUtil.BrownbagDetailResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class BrownbagDetailActivity extends AppCompatActivity implements BrownbagDetailResponseHandler{
	private String brownbagId;
	private static BrownBag currentBrownbag;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb_detail);
        Log.d("TCT", "Brownbag deatil activity started");
        Intent intent = getIntent();
        brownbagId = intent.getStringExtra("BROWNBAG_ID");
        ServerUtil.setBrownbagDetailResponseHandler(this);
        ServerUtil.getBrownbagDetail(brownbagId);
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
}
