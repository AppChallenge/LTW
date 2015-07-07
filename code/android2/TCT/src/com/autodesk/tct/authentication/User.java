package com.autodesk.tct.authentication;

import org.json.JSONObject;

/**
 * Created by wangya on 5/12/15.
 */
public class User {

    public final static String EXTRA_USER_ID = "extra.user.id";

    private final static String ID = "id";
    private final static String USERNAME = "username";
    private final static String EMAIL = "email";

	private final String mId;
    private final String mName;
    private final String mEmail;
    private final boolean mAdmin;
    
    public User(String id, String email) {
        this(id, null, email);
    }

    public User(String id, String name, String email) {
    	mId = id;
        mName = name;
        mEmail = email;
        mAdmin = false;
    }
    
    public static User fromJSONObject(JSONObject obj) {
        if (obj == null) {
            return null;
        }

        String id = obj.optString(ID);
        String name = obj.optString(USERNAME);
        String email = obj.optString(EMAIL);

        if (name == null || name.equals("null")) {
            name = generateUserNameFromEmail(email);
        }

        return new User(id, name, email);
    }

    public String getId() {
    	return mId;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public boolean isAdmin() {
        return mAdmin;
    }

    private static String generateUserNameFromEmail(String email) {
        if (email == null || email.equals("")) {
            return null;
        }

        String[] strs = email.split("@");
        return strs[0];
    }
}
