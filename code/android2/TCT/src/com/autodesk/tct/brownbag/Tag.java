package com.autodesk.tct.brownbag;

import org.json.JSONObject;

public class Tag {

    private final static String ID = "id";
    private final static String TAGNAME = "tagname";
    private final static String BROWNBAGID = "brownbagId";

    private final String mId;
    private final String mTagName;
    private final String mBrownbagId;

    public Tag(String id, String tagName, String brownbagId) {
        mId = id;
        mTagName = tagName;
        mBrownbagId = brownbagId;
    }

    public static Tag fromJSONObject(JSONObject obj) {
        if (obj == null) {
            return null;
        }

        String id = obj.optString(ID);
        String tagName = obj.optString(TAGNAME);
        String brownbagId = obj.optString(BROWNBAGID);

        return new Tag(id, tagName, brownbagId);
    }

    public String getID() {
        return mId;
    }

    public String getTagName() {
        return mTagName;
    }

    public String getBrownbagId() {
        return mBrownbagId;
    }
}
