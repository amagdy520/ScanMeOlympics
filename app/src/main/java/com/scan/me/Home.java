package com.scan.me;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.scan.me.Drawer.DrawerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setDrawerLayout();


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

}
