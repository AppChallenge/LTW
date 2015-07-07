package com.autodesk.tct.authentication;

import com.autodesk.tct.server.ServerUtil;

public class UserUtility {

    public static User getCurrentUser() {
        return ServerUtil.getCurrentUser();
    }

    public static String getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }
}
