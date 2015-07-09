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

    public static boolean isCurrentUser(User user) {
        return getCurrentUser().getId().equals(user.getId());
    }

    public static void signout() {
        ServerUtil.signout();
    }
}
