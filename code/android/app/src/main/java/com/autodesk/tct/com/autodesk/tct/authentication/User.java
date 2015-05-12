package com.autodesk.tct.com.autodesk.tct.authentication;

/**
 * Created by wangya on 5/12/15.
 */
public class User {

    private final String mName;
    private final boolean mAdmin;

    public User(String name, boolean isAdmin) {
        mName = name;
        mAdmin = isAdmin;
    }

    public String getName() {
        return mName;
    }

    public boolean isAdmin() {
        return mAdmin;
    }
}
