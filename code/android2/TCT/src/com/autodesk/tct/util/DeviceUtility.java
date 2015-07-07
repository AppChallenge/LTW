
package com.autodesk.tct.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DeviceUtility {

    public static int getOsVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static int getDeviceWidthPixels() {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

}
