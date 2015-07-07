package com.autodesk.tct.brownbag;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.autodesk.tct.authentication.User;

public class BrownBag {

    public final static String EXTRA_BROWNBAG_ID = "extra.brownbag.id";

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
    private final static String REGISTRATIONS = "brownbag-registrations";

    private final String mId;
    private final String mTitle;
    private final String mDescription;
    private final String mStartTime;
    private final String mEndTime;
    private final String mLocation;
    private final int mStatus;
    private final List<Registration> mRegistration;

    // private final List<User> mSpeakers;
    // private final List<User> mRegisteredUsers;

    // private final Date m
    public BrownBag(String id, String title, String description, String startTime, String endTime, String location,
            int status, List<Registration> registerations) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mStartTime = startTime;
        mEndTime = endTime;
        mLocation = location;
        mStatus = status;
        mRegistration = new ArrayList<Registration>();
        mRegistration.addAll(registerations);
    }

    public static BrownBag fromJSONObject(JSONObject obj) {
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

        JSONArray regObjs = obj.optJSONArray(REGISTRATIONS);
        List<Registration> registrations = new ArrayList<Registration>();
        for (int i = 0; i < regObjs.length(); i++) {
            JSONObject regObj = regObjs.optJSONObject(i);
            Registration registration = Registration.fromJSONObject(regObj);
            if (registration != null) {
                registrations.add(registration);
            }
        }

        return new BrownBag(id, title, description, startTime, endTime, location, status, registrations);
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

    public String getDateString() {
        return mStartTime + "-" + mEndTime;
    }

    public User getSpeakerById(String id) {
        for (Registration registration : mRegistration) {
            if (registration.isSpeaker()) {
                User speaker = registration.getUser();
                if (speaker != null && speaker.getId().equals(id)) {
                    return speaker;
                }
            }
        }
        return null;
    }

    public List<User> getSpeakers() {
        List<User> speakers = new ArrayList<User>();
        for(Registration registration : mRegistration) {
            if(registration.isSpeaker()) {
                speakers.add(registration.getUser());
            }
        }
        return speakers;
    }

    public String getSpeakersName() {
        String names = "Speakers: ";
        for(Registration registration : mRegistration) {
            if(registration.isSpeaker()) {
                names += registration.getUser().getName() + ", ";
            }
        }
        int lastIndex = names.lastIndexOf(",");
        if (lastIndex > 0 && lastIndex < names.length()) {
            names = names.substring(0, lastIndex);
        }
        return names;
    }

    public boolean isAllowToRegister() {
        return true;
    }
}
