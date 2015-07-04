package com.autodesk.tct;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.autodesk.tct.server.ServerUtil;
import com.autodesk.tct.server.ServerUtil.SignHandler;

public class SplashActivity extends Activity implements SignHandler {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private static boolean wantToShowBBDetail = false;
    private static String bbID = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ServerUtil.setSignHandler(this);
        ServerUtil.initialize(this);
    }
        
    public static void setWantToShowBrownbagDetail(boolean show){
    	wantToShowBBDetail = show;
    }
    
    public static void setBrownbagId(String id){
    	bbID = id;
    }

    @Override
    public void onSignSucceed(boolean userTrigger) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	if (wantToShowBBDetail){
            		Intent deatilIntent = new Intent(SplashActivity.this, BrownbagDetailActivity.class);
            		deatilIntent.putExtra("BROWNBAG_ID", bbID);
            		startActivity(deatilIntent);
            		setWantToShowBrownbagDetail(false);
            	}else{
            		Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
            	}
            	
                
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onSignFailed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
