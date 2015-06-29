package com.autodesk.tct.authentication;

/**
 * Created by wangya on 5/12/15.
 */
public class User {

	private final String mId;
    private final String mName;
    private final boolean mAdmin;

    public User(String id) {
    	this(id, null);
    }
    
    public User(String id, String name) {
    	this(id, name, false);
    }
    
    public User(String id, String name, boolean isAdmin) {
    	mId = id;
        mName = name;
        mAdmin = isAdmin;
    }
    
    public String getId() {
    	return mId;
    }

    public String getName() {
        return mName;
    }

    public boolean isAdmin() {
        return mAdmin;
    }
}
