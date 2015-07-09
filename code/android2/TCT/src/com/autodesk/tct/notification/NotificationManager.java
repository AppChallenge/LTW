package com.autodesk.tct.notification;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.autodesk.tct.BrownbagDetailActivity;
import com.autodesk.tct.brownbag.BrownBag;
import com.igexin.sdk.PushManager;

public class NotificationManager {

	private static NotificationManager sInstance;
	private String mClientId;
	private String mAppId;
	private String mAppSecret;
	private String mAppKey;
	
    private boolean mWantToShowBrownbagDetail = false;
    private String mWantToShowBrownbagId = "";

	public static NotificationManager getInstance() {
		if(sInstance == null) {
			sInstance = new NotificationManager();
		}
		
		return sInstance;
	}
	
	private NotificationManager() {		
	}
	
	public void initialize(Context context) {
		Context applicationContext = context.getApplicationContext();
		// initialize push service
		PushManager.getInstance().initialize(applicationContext);
		
		mClientId = PushManager.getInstance().getClientid(applicationContext);
//		Toast.makeText(context, "ClientId: " + mClientId, Toast.LENGTH_LONG).show();
		// Read application configuration
        String packageName = applicationContext.getPackageName();
        try {
            ApplicationInfo appInfo = applicationContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
            	mAppId = appInfo.metaData.getString("PUSH_APPID");
            	mAppSecret = appInfo.metaData.getString("PUSH_APPSECRET");
            	mAppKey = (appInfo.metaData.get("PUSH_APPKEY") != null) ? appInfo.metaData.get("PUSH_APPKEY").toString() : null;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        
	}
	
    public void setWantToShowBrownbagDetail(boolean toShow, String brownbagId) {
        mWantToShowBrownbagDetail = toShow;
        mWantToShowBrownbagId = brownbagId;
    }

    public boolean wantToShowBrownbagDetail() {
        return mWantToShowBrownbagDetail && !"".equals(mWantToShowBrownbagId);
    }

    public String getWantToShowBrownbagId() {
        return mWantToShowBrownbagId;
    }

    public void redirectToBrownbagDetail(Context packageContext) {
        Intent deatilIntent = new Intent(packageContext, BrownbagDetailActivity.class);
        deatilIntent.putExtra(BrownBag.EXTRA_BROWNBAG_ID, NotificationManager.getInstance()
                .getWantToShowBrownbagId());
        packageContext.startActivity(deatilIntent);
        NotificationManager.getInstance().setWantToShowBrownbagDetail(false, "");
    }

	public void bindAlias(Context context, String alias) {
		PushManager.getInstance().bindAlias(context, alias);
	}
	
	public void unbindAlias(Context context, String alias) {
		PushManager.getInstance().unBindAlias(context, alias);
	}
	
}
