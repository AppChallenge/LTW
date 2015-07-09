package com.autodesk.tct.brownbag;

import org.json.JSONObject;

import com.autodesk.tct.authentication.User;
import com.autodesk.tct.authentication.UserUtility;

public class Discussion {

    public final static int BROWNBAG_DEBUG = 0;
    public final static int BROWNBAG_LIVE = 1;
    public final static int BROWNBAG_OUTDATE = 2;

    private final static String ID = "id";
    private final static String MESSAGE = "message";
    private final static String POSTDATE = "postdate";
    private final static String BROWNBAGID = "brownbagId";
    private final static String USERID = "userId";
    private final static String USER = "user";

    private final String mId;
    private final String mMessage;
    private final String mPostTime;
    private final String mBrownbagId;
    private final User mPoster;

    public Discussion(String id, String message, String time, String brownbagId, User user) {
        mId = id;
        mMessage = message;
        mPostTime = time;
        mBrownbagId = brownbagId;
        mPoster = user;
    }

    public static Discussion fromJSONObject(JSONObject obj) {
        if (obj == null) {
            return null;
        }

        String id = obj.optString(ID);
        String message = obj.optString(MESSAGE);
        String time = obj.optString(POSTDATE);
        String brownbagId = obj.optString(BROWNBAGID);
        JSONObject userObj = obj.optJSONObject(USER);
        User user = User.fromJSONObject(userObj);

        return new Discussion(id, message, time, brownbagId, user);
    }

    public String getID() {
        return mId;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getPostTime() {
        return mPostTime;
    }

    public String getUserName() {
        return mPoster != null ? mPoster.getName() : "Anonymous User";
    }

    public boolean isMyOwnReply() {
        if (mPoster == null) {
            return false;
        }

        String currentUserId = UserUtility.getCurrentUserId();
        if (currentUserId != null && currentUserId.equals(mPoster.getId())) {
            return true;
        }

        return false;
    }
}
