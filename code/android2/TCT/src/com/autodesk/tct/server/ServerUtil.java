package com.autodesk.tct.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.autodesk.tct.authentication.User;
import com.autodesk.tct.authentication.UserUtility;
import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.brownbag.BrownBagManager.BrownbagDetailResponseHandler;
import com.autodesk.tct.brownbag.BrownBagManager.BrownbagDiscussionPostHandler;
import com.autodesk.tct.brownbag.BrownBagManager.BrownbagRegisterHandler;
import com.autodesk.tct.brownbag.BrownBagManager.DownloadBrownbagDiscussionHandler;
import com.autodesk.tct.brownbag.Discussion;
import com.autodesk.tct.brownbag.Registration;
import com.autodesk.tct.storage.PreferencesUtil;
import com.autodesk.tct.util.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressWarnings("deprecation")
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
	
    public static User getCurrentUser() {
        return sUser;
    }

	private static void checkUserValidation(Context context) {
		sSessionToken = PreferencesUtil.getUserAccessToken(context);
		String userId = PreferencesUtil.getUserId(context);
        String userEmail = PreferencesUtil.getUserEmail(context);
        long lastLoginTime = PreferencesUtil.getUserLoginTime(context);
        long currentTime = getCurrentTime();
        boolean active = currentTime > lastLoginTime && currentTime < lastLoginTime + ONE_WEEK_IN_MILLIS;
        if (sSessionToken != null && userId != null && userEmail != null && active) {
            sUser = new User(userId, userEmail);
        	fetchUser(userId, sSessionToken, false);
        } else {
        	clearUserInfo();
            onSignFailed();
        }
	}
    
    private static void saveUserInfo() {
    	PreferencesUtil.saveUserId(sApplicationContext, sUser.getId());
        PreferencesUtil.saveUserEmail(sApplicationContext, sUser.getEmail());
    	PreferencesUtil.saveUserLoginTime(sApplicationContext, getCurrentTime());
    }
	
	private static void clearUserInfo() {
		sSessionToken = null;
		PreferencesUtil.saveUserAccessToken(sApplicationContext, null);
		PreferencesUtil.saveUserId(sApplicationContext, String.valueOf(-1));
        PreferencesUtil.saveUserEmail(sApplicationContext, null);
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
                sSessionToken = response.optString("id");
                PreferencesUtil.saveUserAccessToken(sApplicationContext, sSessionToken);
				String userId = response.optString("userId");
                sUser = new User(userId, email);
                fetchUser(userId, sSessionToken, true);
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
        sUser = null;

		String url = getServerUrl() + "/logout";
		delete(sApplicationContext, url, null, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
            }
        });
	}
	
    public static void registerBrownbag(String brownbagId, final BrownbagRegisterHandler handler) {
		String url = getServerUrl() + "/brownbag-registrations/register-brownbags?access_token=" + sSessionToken;
		RequestParams params = new RequestParams();
		params.put("brownbagId", brownbagId);
		params.put("userId", sUser.getId());
        params.put("role", Registration.ROLE_AUDIENCE);
		post(url, params, new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("TCT", "registerBrownbag return success!");
				
				if (response != null) {
					if(response.optBoolean("success")){
                        onBrownbagRegisterSucceeded("0", handler);
					}
				}else{
                    onBrownbagRegisterFailed(handler);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject error) {
				Log.d("TCT", "registerBrownbag fail!");
                onBrownbagRegisterFailed(handler);
			}

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            	Log.d("TCT", "registerBrownbag fail!");
                onBrownbagRegisterFailed(handler);
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable,
                    org.json.JSONArray errorResponse) {
            	Log.d("TCT", "registerBrownbag fail!");
                onBrownbagRegisterFailed(handler);
            }
		});
	}
	
    public static void getBrownbagDetail(String brownbagId, final BrownbagDetailResponseHandler handler) {
		String url = getServerUrl() + "/board-brownbags/get-brownbag-by-id?id=" + brownbagId + "&access_token=" + sSessionToken;
		get(sApplicationContext, url, null, null, new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.d("TCT", "getBrownbagDetail success!");
				if (response != null) {
					try {
                        JSONObject brownbagObj = response.getJSONObject("brownbag");
                        BrownBag bb = BrownBag.fromJSONObject(brownbagObj);
                        onBrownbagDetailReceivedSucceed(bb, handler);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
//					Date startDate, endDate;
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//					try {
//						startDate = sdf.parse(starttime);
//						endDate = sdf.parse(endtime);
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
					
				}else{
                    onBrownbagDetailReceivedFailed(handler);
				}
				
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject error) {
				Log.d("TCT", "getBrownbagDetail fail!");
                onBrownbagDetailReceivedFailed(handler);
			}

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            	Log.d("TCT", "getBrownbagDetail fail!");
                onBrownbagDetailReceivedFailed(handler);
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable,
                    org.json.JSONArray errorResponse) {
            	Log.d("TCT", "getBrownbagDetail fail!");
                onBrownbagDetailReceivedFailed(handler);
            }
		});
	}
	
    public static void fetchUser(String userId, final String token, final boolean userTrigger) {
        String url = getServerUrl() + "/users/" + sUser.getId() + "?access_token=" + token;
        get(sApplicationContext, url, null, null, new JsonHttpResponseHandler() {

			@Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				if (response == null) {
					onSignFailed();
					return;
				}
                sUser = User.fromJSONObject(response);
                saveUserInfo();
				onSignSucceed(userTrigger);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject error) {
				onSignFailed();
			}

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                onSignFailed();
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable,
                    org.json.JSONArray errorResponse) {
                onSignFailed();
            }
		});
	}
	
    public static void downloadBrownbags(JsonHttpResponseHandler handler) {
        String url = getServerUrl() + "/board-brownbags/list-brownbags?access_token=" + sSessionToken;
        get(sApplicationContext, url, null, null, handler);
    }

    public static void getBrownbagDiscussions(final String brownbagId, final DownloadBrownbagDiscussionHandler handler) {
        String url = getServerUrl() + "/brownbag-discussions/get-brownbag-discussions?access_token=" + sSessionToken;
        RequestParams params = new RequestParams();
        params.put("brownbagId", brownbagId);
        post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TCT", "registerBrownbag return success!");

                if (response != null) {
                    try {
                        JSONArray array = response.getJSONArray("success");
                        List<Discussion> discussions = new ArrayList<Discussion>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Discussion discussion = Discussion.fromJSONObject(obj);
                            discussions.add(discussion);
                        }
                        onBrownbagDiccussionsReceivedSucceed(discussions, handler);
                        return;
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                onBrownbagDiccussionsReceivedFailed(handler);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                    Throwable throwable, JSONObject error) {
                Log.d("TCT", "registerBrownbag fail!");
                onBrownbagDiccussionsReceivedFailed(handler);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TCT", "registerBrownbag fail!");
                onBrownbagDiccussionsReceivedFailed(handler);
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable,
                    org.json.JSONArray errorResponse) {
                Log.d("TCT", "registerBrownbag fail!");
                onBrownbagDiccussionsReceivedFailed(handler);
            }
        });
    }

    public static void postBrownbagDiscussion(final String brownbagId, final String message,
            final BrownbagDiscussionPostHandler handler) {
        String url = getServerUrl() + "/brownbag-discussions?access_token=" + sSessionToken;
        RequestParams params = new RequestParams();
        params.put("brownbagId", brownbagId);
        params.put("userId", UserUtility.getCurrentUserId());
        params.put("message", message);
        params.put("postdate", Utility.getCurrentDateString());
        post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TCT", "post a reply succeed!");

                if (response != null) {
                    String id = response.optString("id");
                    String time = response.optString("postdate");
                    Discussion discussion = new Discussion(id, message, time, brownbagId, UserUtility.getCurrentUser());
                    onBrownbagDiscussionPostSucceed(discussion, handler);
                } else {
                    onBrownbagDiscussionPostFailed(handler);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                    Throwable throwable, JSONObject error) {
                Log.d("TCT", "post a reply fail!");
                onBrownbagDiscussionPostFailed(handler);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TCT", "post a reply fail!");
                onBrownbagDiscussionPostFailed(handler);
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable,
                    org.json.JSONArray errorResponse) {
                Log.d("TCT", "post a reply fail!");
                onBrownbagDiscussionPostFailed(handler);
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
    
    private static void onBrownbagDetailReceivedSucceed(BrownBag brownbag, BrownbagDetailResponseHandler handler) {
        if (handler != null) {
            handler.onBrownbagDetailReceivedSucceed(brownbag);
    	}
    }
    
    private static void onBrownbagDetailReceivedFailed(BrownbagDetailResponseHandler handler) {
        if (handler != null) {
            handler.onBrownbagDetailReceivedFailed();
    	}
    }
    
    private static void onBrownbagRegisterFailed(BrownbagRegisterHandler handler) {
        if (handler != null) {
            handler.onRegisterFailure();
        }
    }

    private static void onBrownbagRegisterSucceeded(String brownbagId, BrownbagRegisterHandler handler) {
        if (handler != null) {
            handler.onRegisterSucceess(brownbagId);
        }
    }

    private static void onBrownbagDiccussionsReceivedSucceed(List<Discussion> discussions,
            DownloadBrownbagDiscussionHandler handler) {
        if (handler != null) {
            handler.onBrownbagDiccussionsReceivedSucceed(discussions);
        }
    }

    private static void onBrownbagDiccussionsReceivedFailed(DownloadBrownbagDiscussionHandler handler) {
        if (handler != null) {
            handler.onBrownbagDiccussionsReceivedFailed();
        }
    }

    private static void onBrownbagDiscussionPostSucceed(Discussion discussion,
            BrownbagDiscussionPostHandler handler) {
        if (handler != null) {
            handler.onBrownbagDiscussionPostSucceed(discussion);
        }
    }

    private static void onBrownbagDiscussionPostFailed(BrownbagDiscussionPostHandler handler) {
        if (handler != null) {
            handler.onBrownbagDiscussionPostFailed();
        }
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
