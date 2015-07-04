package com.autodesk.tct.brownbag;

import org.json.JSONObject;

public class BrownBag {

    public final static int BROWNBAG_DEBUG = 0;
    public final static int BROWNBAG_LIVE = 1;
    public final static int BROWNBAG_OUTDATE = 2;

    private final static String ID = "id";
    private final static String TITLE = "title";
    private final static String DESCRIPTION = "description";
    private final static String START_TIME = "starttime";
    private final static String END_TIME = "endtime";
    private final static String LOCATION = "location";
    private final static String STATUS = "status";

    private final String mId;
    private final String mTitle;
    private final String mDescription;
    private final String mStartTime;
    private final String mEndTime;
    private final String mLocation;
    private final int mStatus;

    // private final Date m
    public BrownBag(String id, String title, String description, String startTime, String endTime, String location,
            int status) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mStartTime = startTime;
        mEndTime = endTime;
        mLocation = location;
        mStatus = status;
    }

    public BrownBag fromJSONObject(JSONObject obj) {
        if (obj == null) {
            return null;
        }

        String id = obj.optString(ID);
        String title = obj.optString(TITLE);
        String description = obj.optString(DESCRIPTION);
        String startTime = obj.optString(START_TIME);
        String endTime = obj.optString(END_TIME);
        String location = obj.optString(LOCATION);
        int status = obj.optInt(STATUS);

        return new BrownBag(id, title, description, startTime, endTime, location, status);
    }

    public String getID() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

}
