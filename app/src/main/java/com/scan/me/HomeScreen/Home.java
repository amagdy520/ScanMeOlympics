package com.scan.me.HomeScreen;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.Data;
import com.scan.me.Drawer.DrawerFragment;
import com.scan.me.LecturesFragment;
import com.scan.me.LoginScreen.LoginActivity;
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
    @BindView (R.id.progressbar)
    ProgressBar progressBar;
    private User user;
    private DrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDrawerLayout();
        getUserData();
    }

    private void getUserData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Data", dataSnapshot.toString());

                progressBar.setVisibility (View.GONE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                }
                setTabLayout(user);
                drawerFragment.setUserData(user);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setTabLayout(User user) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        if (user.getType().equals(User.ADMIN)) {
            RoomsFragment roomsFragment = new RoomsFragment();
            roomsFragment.setUser(user);
            UserFragment userFragment = new UserFragment();
            userFragment.setUser(user);
            adapter.addFragment(roomsFragment, "Rooms");
            adapter.addFragment(userFragment, "Users");
        } else if (user.getType().equals(User.TUTOR)) {
            RoomsFragment roomsFragment = new RoomsFragment();
            roomsFragment.setUser(user);
            LecturesFragment lecturesFragment = new LecturesFragment();
            lecturesFragment.setUser(user,LecturesFragment.TODAY);
            adapter.addFragment(roomsFragment, "Rooms");
            adapter.addFragment(lecturesFragment, "Lectures");

        } else {
            LecturesFragment lecturesFragment = new LecturesFragment();
            lecturesFragment.setUser(user,LecturesFragment.TODAY);
            LecturesFragment lecturesFragment1 = new LecturesFragment();
            lecturesFragment1.setUser(user,LecturesFragment.HISTORY);
            adapter.addFragment(lecturesFragment, "Lectures");
            adapter.addFragment(lecturesFragment1, "History");

        }
        mViewPager.setAdapter(adapter);
//        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setTabTextColors(Color.WHITE, Color.parseColor("#01d277"));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void setDrawerLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawerFragment = (DrawerFragment) getSupportFragmentManager()
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
