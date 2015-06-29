package com.autodesk.tct.server;

import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;

import com.autodesk.tct.authentication.User;
import com.autodesk.tct.storage.PreferencesUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ServerUtil {
	
	public interface SignHandler {
		void onSignSucceed(boolean userTrigger);
		void onSignFailed();
	}
	
	private final static long ONE_WEEK_IN_MILLIS = 7 * 24 * 60 * 60 * 1000;
	private final static String SERVER_URL = "http://iratao-pretzel.daoapp.io:80/api";

    private final static AsyncHttpClient ASYNC_HTTP_CLIENT = new AsyncHttpClient();
	private static Context sApplicationContext;
	
	private static User sUser;
	private static String sSessionToken;
	
	private static SignHandler sSignHandler;
	
	public static void initialize(Context context) {
		sApplicationContext = context.getApplicationContext();
		checkUserValidation(sApplicationContext);
	}
	
	public static void setSignHandler(SignHandler handler) {
		sSignHandler = handler;
	}
	
	private static void checkUserValidation(Context context) {
		sSessionToken = PreferencesUtil.getUserAccessToken(context);
		String userId = PreferencesUtil.getUserId(context);
        String userName = PreferencesUtil.getUserName(context);
        long lastLoginTime = PreferencesUtil.getUserLoginTime(context);
        long currentTime = getCurrentTime();
        boolean active = currentTime > lastLoginTime && currentTime < lastLoginTime + ONE_WEEK_IN_MILLIS;
        if (sSessionToken != null && userId != null && userName != null && active) {
        	sUser = new User(userId, userName);
        	fetchUser(userId, sSessionToken, false);
        } else {
        	clearUserInfo();
            onSignFailed();
        }
	}
    
    private static void saveUserInfo() {
    	PreferencesUtil.saveUserId(sApplicationContext, sUser.getId());
    	PreferencesUtil.saveUserName(sApplicationContext, sUser.getName());
    	PreferencesUtil.saveUserLoginTime(sApplicationContext, getCurrentTime());
    }
	
	private static void clearUserInfo() {
		sSessionToken = null;
		PreferencesUtil.saveUserAccessToken(sApplicationContext, null);
		PreferencesUtil.saveUserId(sApplicationContext, String.valueOf(-1));
        PreferencesUtil.saveUserName(sApplicationContext, null);
        PreferencesUtil.saveUserLoginTime(sApplicationContext, 0);
	}
	
	private static String getServerUrl() {
		return SERVER_URL;
	}

    public static boolean isSignedIn() {
        return true; //sUser != null && sUser.getName() != null;
    }
	
	public static void signUp(final String email, final String password) {
		String signUpUrl = getServerUrl() + "/users";
		RequestParams params = new RequestParams();
		params.add("email", email);
		params.add("password", password);
		
		post(signUpUrl, params, new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response == null) {
                	onSignFailed();
                    return;
                }
    			signIn(email, password);
            }
			
			@Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
				onSignFailed();
            }
		});
	}
	
	public static void signIn(final String email, final String password) {
		String signUpUrl = getServerUrl() + "/users/login";
		RequestParams params = new RequestParams();
		params.add("email", email);
		params.add("password", password);
		
		post(signUpUrl, params, new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response == null) {
                	onSignFailed();
                    return;
                }
				String sessionToken = response.optString("id");
				PreferencesUtil.saveUserAccessToken(sApplicationContext, sessionToken);
				String userId = response.optString("userId");
				sUser = new User(userId);
				fetchUser(userId, sessionToken, true);
            }
			
			@Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
				onSignFailed();
            }
		});
	}
	
	/**
	 * TODO: not test
	 */
	public static void signout() {
		clearUserInfo();
		
		String url = getServerUrl() + "/logout";
		delete(sApplicationContext, url, null, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
            }
        });
	}
	
	/**
	 * TODO: not test
	 */
	public static void fetchUser(String userId, String token, final boolean userTrigger) {
		String url = getServerUrl() + "users/" + sUser.getId() + "?access_token=" + token;
		post(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (response == null) {
					onSignFailed();
					return;
				}
				String id = response.optString("id");
				String name = response.optString("username");
				sUser = new User(id, name);
                saveUserInfo();
				onSignSucceed(userTrigger);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject error) {
				onSignFailed();
			}
		});
	}
		
    private static void get(Context context, String url, Header[] headers, RequestParams params,
            AsyncHttpResponseHandler responseHandler) {
    	ASYNC_HTTP_CLIENT.get(context, url, headers, params, responseHandler);
    }

    private static void delete(Context context, String url, Header[] headers, RequestParams params,
            AsyncHttpResponseHandler responseHandler) {
    	ASYNC_HTTP_CLIENT.delete(context, url, headers, params, responseHandler);
    }

    private static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	ASYNC_HTTP_CLIENT.post(url, params, responseHandler);
    }
    
    private static long getCurrentTime() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }
    
    private static void onSignSucceed(boolean userTrigger) {
    	if(sSignHandler != null) {
    		sSignHandler.onSignSucceed(userTrigger);;
    	}
    }
    
    private static void onSignFailed() {
    	if(sSignHandler != null) {
    		sSignHandler.onSignFailed();
    	}
    }
    
}
