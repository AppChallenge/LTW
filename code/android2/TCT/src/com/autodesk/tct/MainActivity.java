package com.autodesk.tct;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.autodesk.tct.brownbag.BrownBagManager;
import com.autodesk.tct.notification.NotificationManager;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabsView;
    private ViewPager mViewPager;

    private SimpleFragmentPagerAdapter mFragmentPagerAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); 
        setSupportActionBar(toolbar);

        initializeTabViews();

        NotificationManager.getInstance().initialize(this);
        BrownBagManager.getInstance().initialize(this);
    }

    private void initializeTabViews() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                int position = mViewPager.getCurrentItem();
                BaseFragment currFrag = (BaseFragment) mFragmentPagerAdapter.getItem(position);
                currFrag.onFragmentResume();
            }

        });

        mTabsView = (TabLayout) findViewById(R.id.tabs);
        mTabsView.setupWithViewPager(mViewPager);
        // Customize tab view.
        for (int i = 0; i < mFragmentPagerAdapter.getCount(); i++) {
            mTabsView.getTabAt(i).setCustomView(mFragmentPagerAdapter.getTabView(i));
        }

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
