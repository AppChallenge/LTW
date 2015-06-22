package com.autodesk.tct.notification;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import com.igexin.sdk.PushManager;

public class NotificationManager {

	private static NotificationManager sInstance;
	private String mClientId;
	private String mAppId;
	private String mAppSecret;
	private String mAppKey;
	
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
	
	public void bindAlias(Context context, String alias) {
		PushManager.getInstance().bindAlias(context, alias);
	}
	
	public void unbindAlias(Context context, String alias) {
		PushManager.getInstance().unBindAlias(context, alias);
	}
	
}
