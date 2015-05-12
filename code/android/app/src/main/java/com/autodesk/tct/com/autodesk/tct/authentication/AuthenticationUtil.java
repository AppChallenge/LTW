package com.autodesk.tct.com.autodesk.tct.authentication;

/**
 * Created by wangya on 5/12/15.
 */
public class AuthenticationUtil {

    private static User sUser = null;
    public AuthenticationUtil() {
    }

    public static boolean isSignedIn() {
        return sUser != null && sUser.getName() != null;
    }


}
