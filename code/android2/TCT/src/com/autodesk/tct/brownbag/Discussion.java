package com.autodesk.tct.brownbag;

import org.json.JSONObject;

import com.autodesk.tct.authentication.User;

public class Discussion {

    public final static int BROWNBAG_DEBUG = 0;
    public final static int BROWNBAG_LIVE = 1;
    public final static int BROWNBAG_OUTDATE = 2;

    private final static String ID = "id";
    private final static String TITLE = "title";
    private final static String MESSAGE = "message";
    private final static String TIME = "time";
    private final static String END_TIME = "endtime";
    private final static String LOCATION = "location";
    private final static String STATUS = "status";
    private final static String REGISTRATIONS = "brownbag-registrations";

    private final String mId;
    private final String mMessage;

    private User mPoster;

    // private final Date m
    public Discussion(String id, String message) {
        mId = id;
        mMessage = message;
    }

    public static Discussion fromJSONObject(JSONObject obj) {
        if (obj == null) {
            return null;
        }

        return null;

        // String id = obj.optString(ID);
        // String title = obj.optString(TITLE);
        // String description = obj.optString(DESCRIPTION);
        // String startTime = obj.optString(START_TIME);
        // String endTime = obj.optString(END_TIME);
        // String location = obj.optString(LOCATION);
        // int status = obj.optInt(STATUS);
        //
        // JSONArray regObjs = obj.optJSONArray(REGISTRATIONS);
        // List<Registration> registrations = new ArrayList<Registration>();
        // for (int i = 0; i < regObjs.length(); i++) {
        // JSONObject regObj = regObjs.optJSONObject(i);
        // Registration registration = Registration.fromJSONObject(regObj);
        // if (registration != null) {
        // registrations.add(registration);
        // }
        // }
        //
        // return new Discussion(id, title, description, startTime, endTime, location, status, registrations);
    }

    public String getID() {
        return mId;
    }
}
