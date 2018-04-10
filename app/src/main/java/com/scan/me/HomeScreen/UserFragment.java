package com.scan.me.HomeScreen;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scan.me.AddRoom;
import com.scan.me.AddUser;
import com.scan.me.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserFragment extends Fragment
{

    @BindView (R.id.floating_user)
    FloatingActionButton floating_user;

    public UserFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.floating_user)
    void addUser()
    {
        Intent intent = new Intent (getActivity (), AddUser.class);
        startActivity (intent);
    }

}
