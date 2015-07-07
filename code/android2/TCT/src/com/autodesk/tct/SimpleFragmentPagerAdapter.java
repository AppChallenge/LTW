package com.autodesk.tct;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import com.autodesk.tct.authentication.UserUtility;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private final static int FEEDS_POSITION = 0;
    private final static int BBS_POSITION = 1;
    private final static int USERPROFILE_POSITION = 2;
    private final static int[] TAB_ICON_RESOUCE = {
            R.drawable.level_1_headline, R.drawable.level_1_bbsall, R.drawable.level_1_userprofile
    };
    final int PAGE_COUNT = 3;
    private Context mContext;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FEEDS_POSITION:
                return new SimpleFeedsFragment();
            case BBS_POSITION:
                return new BBSFragment();
            case USERPROFILE_POSITION:
                return new UserProfileFragment(UserUtility.getCurrentUser());
        }

        return null;
    }

    // This method doesn't work.
    // @Override
    // public CharSequence getPageTitle(int position) {
    // // Return a SpannableString which contains icons in ImageSpan to add icon for tab.
    // Drawable image = ContextCompat.getDrawable(mContext, TAB_ICON_RESOUCE[position]);
    // image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
    // SpannableString sb = new SpannableString(" ");
    // ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
    // sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    // return sb;
    //
    // }

    public View getTabView(int position) {
        ImageView img = new ImageView(mContext);
        img.setImageResource(TAB_ICON_RESOUCE[position]);
        return img;
    }
}
