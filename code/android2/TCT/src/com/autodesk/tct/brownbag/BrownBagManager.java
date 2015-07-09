package com.autodesk.tct.brownbag;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.autodesk.tct.server.ServerUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

public class BrownBagManager {

    public interface BrownBagsDownloadListener {
        void onAllBrownBagsDownloaded();

        void onBrownBagsDownloadFailed();
    }

    public interface BrownbagRegisterHandler {
        void onRegisterSucceess(String brownbagId);

        void onRegisterWaitingList(String brownbagId);

        void onRegisterFailure();
    }

    public interface BrownbagDetailResponseHandler {
        void onBrownbagDetailReceivedSucceed(BrownBag brownbag);

        void onBrownbagDetailReceivedFailed();
    }

    public interface DownloadBrownbagDiscussionHandler {
        void onBrownbagDiccussionsReceivedSucceed(List<Discussion> discussions);

        void onBrownbagDiccussionsReceivedFailed();
    }

    public interface BrownbagDiscussionPostHandler {
        void onBrownbagDiscussionPostSucceed(Discussion discussion);

        void onBrownbagDiscussionPostFailed();
    }

    private final static String TAG = "BrownBagManager";
    private final static String BROWNBAGS = "brownbags";


    private static BrownBagManager sInstance = null;
    
    private BrownBagsDownloadListener mBrownBagsDownloadListener = null;
    private BrownbagDetailResponseHandler mBrownbagDetailResponseHandler;
    private BrownbagRegisterHandler mBrownbagRegisterHandler;
    private DownloadBrownbagDiscussionHandler mDownloadBrownbagDiscussionHandler;
    private BrownbagDiscussionPostHandler mBrownbagDiscussionPostHandler;

    private List<BrownBag> mBrownBags = new ArrayList<BrownBag>();

    public static BrownBagManager getInstance() {
        if (sInstance == null) {
            sInstance = new BrownBagManager();
        }

        return sInstance;
    }

    public void setOnBrownBagsDownloadListener(BrownBagsDownloadListener l) {
        mBrownBagsDownloadListener = l;
    }

    public void setBrownbagDetailResponseHandler(BrownbagDetailResponseHandler handler) {
        mBrownbagDetailResponseHandler = handler;
    }

    public void setBrownbagRegisterHander(BrownbagRegisterHandler handler) {
        mBrownbagRegisterHandler = handler;
    }

    public void setDownloadBrownbagDiscussionHandler(DownloadBrownbagDiscussionHandler handler) {
        mDownloadBrownbagDiscussionHandler = handler;
    }
    
    public void setBrownbagDiscussionPostHandler(BrownbagDiscussionPostHandler handler) {
        mBrownbagDiscussionPostHandler = handler;
    }

    public List<BrownBag> getAllBrownBags() {
        return mBrownBags;
    }

    public BrownBag getBrownbagById(String id) {
        for (BrownBag brownbag : mBrownBags) {
            if (brownbag.getID().equals(id)) {
                return brownbag;
            }
        }

        return null;
    }

    public void downloadBrownBags() {
        ServerUtil.downloadBrownbags(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response == null) {
                    return;
                }
                JSONArray brownbagObjs = response.optJSONArray(BROWNBAGS);
                if (brownbagObjs == null) {
                    return;
                }
                // Make sure this is a valid manifest.
                List<BrownBag> brownBags = new ArrayList<BrownBag>();
                for (int i = 0; i < brownbagObjs.length(); i++) {
                    JSONObject brownbagObj = brownbagObjs.optJSONObject(i);
                    BrownBag brownbag = BrownBag.fromJSONObject(brownbagObj);
                    if (brownbag != null) {
                        brownBags.add(brownbag);
                    }
                }

                setBrownBags(brownBags);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                    Throwable throwable, JSONObject error) {

                if (mBrownBagsDownloadListener != null) {
                    mBrownBagsDownloadListener.onBrownBagsDownloadFailed();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (mBrownBagsDownloadListener != null) {
                    mBrownBagsDownloadListener.onBrownBagsDownloadFailed();
                }
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.Throwable throwable,
                    org.json.JSONArray errorResponse) {
                if (mBrownBagsDownloadListener != null) {
                    mBrownBagsDownloadListener.onBrownBagsDownloadFailed();
                }
            }
        });
    }

    private void setBrownBags(List<BrownBag> brownbags) {
        mBrownBags = new ArrayList<BrownBag>();
        mBrownBags.addAll(brownbags);
        if (mBrownBagsDownloadListener != null) {
            mBrownBagsDownloadListener.onAllBrownBagsDownloaded();
        }
    }

    public void registerABrownBag(String brownbagId) {
        ServerUtil.registerBrownbag(brownbagId, mBrownbagRegisterHandler);
    }

    public void getBrownbagDetail(String brownbagId) {
        ServerUtil.getBrownbagDetail(brownbagId, mBrownbagDetailResponseHandler);
    }

    public void getBrownbagDiscussions(String brownbagId) {
        ServerUtil.getBrownbagDiscussions(brownbagId, mDownloadBrownbagDiscussionHandler);
    }
    
    public void postBrownbagDiscussion(String brownbagId, String message) {
        ServerUtil.postBrownbagDiscussion(brownbagId, message, mBrownbagDiscussionPostHandler);
    }

}
