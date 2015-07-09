package com.autodesk.tct;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.autodesk.tct.authentication.User;
import com.autodesk.tct.brownbag.BrownBag;
import com.autodesk.tct.brownbag.BrownBagManager;


public class SpeakerActivity extends BaseActivity {
    private String mBrownbagId;
    private GridView mGridView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

        mBrownbagId = getIntent().getStringExtra(BrownBag.EXTRA_BROWNBAG_ID);
        BrownBag brownbag = BrownBagManager.getInstance().getBrownbagById(mBrownbagId);

        initializeViews(brownbag);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_speaker;
    }

    @Override
    protected void setToolbar() {
        super.setToolbar();
        getSupportActionBar().setTitle(R.string.action_bar_title_speaker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initializeViews(BrownBag brownbag) {
        mGridView = (GridView) findViewById(R.id.gridview);

        List<User> speakers = new ArrayList<User>();
        if (brownbag != null) {
            speakers.addAll(brownbag.getSpeakers());
        }
        mGridView.setAdapter(new ImageAdapter(speakers));

        mGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                User user = (User) parent.getAdapter().getItem(position);
                gotoUserProfile(user.getId());
            }
        });
    }

    private void gotoUserProfile(String userId) {
        Intent intent = new Intent(SpeakerActivity.this, UserProfileActivity.class);
        intent.putExtra(BrownBag.EXTRA_BROWNBAG_ID, mBrownbagId);
        intent.putExtra(User.EXTRA_USER_ID, userId);
        startActivity(intent);
    }

    private static class ImageAdapter extends BaseAdapter {
        private final static int[] AVATAR_RESOUCES = {
                R.drawable.avatar_4_large, R.drawable.avatar_5_large
        };
        private List<User> mSpeakers;

        public ImageAdapter(List<User> speakers) {
            mSpeakers = new ArrayList<User>();
            mSpeakers.addAll(speakers);
        }

        public int getCount() {
            return mSpeakers != null ? mSpeakers.size() : 0;
        }

        public Object getItem(int position) {
            return mSpeakers != null ? mSpeakers.get(position) : null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            User speaker = mSpeakers.get(position);
            TextView itemView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                Context context = parent.getContext();
                itemView = (TextView)LayoutInflater.from(context).inflate(R.layout.speaker_list_item, null);
                itemView.setCompoundDrawablesWithIntrinsicBounds(0, getRandomAvatarResourceId(), 0, 0);
            } else {
                itemView = (TextView) convertView;
            }
            itemView.setText(speaker.getName());
            return itemView;
        }

        private int getRandomAvatarResourceId() {
            int randomIndex = 1;// new Random().nextInt(AVATAR_RESOUCES.length);
            return AVATAR_RESOUCES[randomIndex];
        }
    }
}
