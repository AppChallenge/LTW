package com.autodesk.tct.brownbag;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.android.common.logger.Log;
import com.autodesk.tct.server.ServerUtil;
import com.autodesk.tct.util.AsyncTaskHelper;
import com.autodesk.tct.util.NetworkUtility;
import com.autodesk.tct.util.Utility;

public class BrownBagManager {

    public interface BrownBagsDownloadListener {
        void onAllBrownBagsDownloaded();
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
    
    private Context mContext;
    private DownloadBrownBagTask mDownloadBrownBagTask = null;
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

    public void initialize(Context context) {
        mContext = context.getApplicationContext();
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
        if (isDownloadingBrownBags()) {
            return;
        }

        mDownloadBrownBagTask = new DownloadBrownBagTask();
        AsyncTaskHelper.execute(mDownloadBrownBagTask);
    }

    private boolean isDownloadingBrownBags() {
        return mDownloadBrownBagTask != null;
    }

    private void cancelDownloadBrownBags() {
        if (mDownloadBrownBagTask != null) {
            mDownloadBrownBagTask.cancel(true);
            mDownloadBrownBagTask = null;
        }
    }

    private class DownloadBrownBagTask extends AsyncTask<Void, Void, Void> {
        List<BrownBag> mBrownBags;

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "DownloadBrownBagTask onPreExecute");
            if (!NetworkUtility.isOnline(mContext)) {
                cancelDownloadBrownBags();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "DownloadBrownBagTask doInBackground");
            if (isCancelled()) {
                return null;
            }
            String jsonString = ServerUtil.downloadBrownbags();
            JSONArray brownbagObjs = Utility.getJSONArray(jsonString, BROWNBAGS);
            if (brownbagObjs == null) {
                return null;
            }
            // Make sure this is a valid manifest.
            mBrownBags = new ArrayList<BrownBag>();
            for (int i = 0; i < brownbagObjs.length(); i++) {
                JSONObject brownbagObj = brownbagObjs.optJSONObject(i);
                BrownBag brownbag = BrownBag.fromJSONObject(brownbagObj);
                if (brownbag != null) {
                    mBrownBags.add(brownbag);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, "DownloadBrownBagTask onPostExecute");
            if (mBrownBags != null) {
                setBrownBags(mBrownBags);
                mBrownBags = null;
            }

            mDownloadBrownBagTask = null;
        }

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
