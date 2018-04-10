package com.scan.me.HomeScreen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.scan.me.Drawer.DrawerFragment;
import com.scan.me.R;
import com.scan.me.User.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

//    @BindView(R.id.fab)
//    FloatingActionButton floatingActionButton;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setDrawerLayout();
        setTabLayout(null);



    }

    private void setTabLayout(User user) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
//        if(user.getType().equals(User.ADMIN)){
            adapter.addFragment(new RoomsFragment(),"Rooms");
            adapter.addFragment(new UserFragment(),"Users");
//        }
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setTabTextColors(Color.WHITE, Color.parseColor("#FF4081"));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    private void setDrawerLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerFragment drawerFragment = (DrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.drawer_fragment);
        drawerFragment.setUpDrawer(drawerLayout, toolbar);

    }




    class PagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragments = new ArrayList<>();
        List<String> mTitles = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mTitles.add(title);
            mFragments.add(fragment);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

}
