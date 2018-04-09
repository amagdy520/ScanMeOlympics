package com.scan.me.Drawer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.scan.me.R;
import com.scan.me.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {

    @BindView(R.id.drawer_list)
    ListView listView;//navigation List
    View view;
    private DrawerLayout mDrawerLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.balance)
    TextView balance;
    private boolean isOpen;
    private User user;

    public DrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_drawer, container, false);
        ButterKnife.bind(this, view);
        setList();
        return view;
    }

    private void setList() {

        final List<DrawerData> drawerDataList = new ArrayList<>();
//        drawerDataList.add(new DrawerData(R.string.my_cars, R.drawable.ic_cars));
//        drawerDataList.add(new DrawerData(R.string.my_requests, R.drawable.ic_history));
//        drawerDataList.add(new DrawerData(R.string.settings, R.drawable.ic_settings));
//        drawerDataList.add(new DrawerData(R.string.about_us, R.drawable.ic_info));
//        drawerDataList.add(new DrawerData(R.string.sign_out, R.drawable.ic_exit));

        DrawerListAdapter adapter = new DrawerListAdapter(getActivity(), drawerDataList);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });


    }

    /**
     * get Data from Main Map and set it to the Drawer
     *
     * @param userData :User Data
     */
    public void setUserData(User userData) {
//        user = userData;
//        if (user.getBalance() != null) {
//            balance.setText(user.getBalance() + " EGP");
//        } else {
//            balance.setText("00.0 EGP");
//        }
//        if (image != null) {
//            GlideApp.with(getActivity())
//                    .load(userData.getImage())
//                    .placeholder(R.drawable.user_pic)
//                    .error(R.drawable.user_pic)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(this.image);
//
//        }
//        this.name.setText(userData.getName());

    }

    public void setUpDrawer(DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        final ActionBarDrawerToggle mDrawerToggle =
                new ActionBarDrawerToggle(getActivity(),
                                          drawerLayout,
                                          toolbar,
                                          R.string.app_name,
                                          R.string.app_name
                ) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        isOpen = true;
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        isOpen = false;
                    }
                };
        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    public class DrawerData {

        int title;
        int image;

        public DrawerData(int title, int image) {
            this.title = title;
            this.image = image;
        }

    }
}