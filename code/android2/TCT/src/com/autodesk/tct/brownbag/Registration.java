package com.autodesk.tct.brownbag;

import org.json.JSONObject;

import com.autodesk.tct.authentication.User;

public class Registration {
    
    public enum Role {
        Speaker,
        Audience
    }

    private final static String ID = "id";
    private final static String ROLE = "role";
    private final static String USERID = "userId";
    private final static String BROWNBAGID = "brownbagId";
    private final static String USER = "user";

    private final static String ROLE_SPEAKER = "speaker";
    private final static String ROLE_AUDIENCE = "audience";

    private final String mBrownbagId;
    private final User mUser;
    private final Role mUserRole;
    
    public Registration(String brownbagId, User user, Role userRole) {
        mBrownbagId = brownbagId;
        mUser = user;
        mUserRole = userRole;
    }

    public String getBrownbagId() {
        return mBrownbagId;
    }

    public Role getUserRole() {
        return mUserRole;
    }

    public boolean isSpeaker() {
        return getUserRole() == Role.Speaker;
    }

    public User getUser() {
        return mUser;
    }

    public static Registration fromJSONObject(JSONObject obj) {
        if (obj == null) {
            return null;
        }

        String brownbagId = obj.optString(BROWNBAGID);
        Role role = toRole(obj.optString(ROLE));
        JSONObject userObj = obj.optJSONObject(USER);
        User user = User.fromJSONObject(userObj);

        return new Registration(brownbagId, user, role);
    }

    private static Role toRole(String role) {
        if (ROLE_SPEAKER.equals(role)) {
            return Role.Speaker;
        } else if (ROLE_AUDIENCE.equals(role)) {
            return Role.Audience;
        } else {
            return null;
        }

    }
}
