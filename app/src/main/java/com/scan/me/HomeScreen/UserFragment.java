package com.scan.me.HomeScreen;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.AddUser;
import com.scan.me.Data;
import com.scan.me.R;
import com.scan.me.User.User;
import com.scan.me.User.UserDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserFragment extends Fragment implements UsersAdapter.OnUserClickListener
{
    @BindView(R.id.floating_user)
    FloatingActionButton floating_user;
    List<User> users = new ArrayList<>();
    @BindView(R.id.users_recycler)
    RecyclerView userRecyclerView;
    User user;

    public void setUser(User user) {
        this.user = user;
    }

    public UserFragment()
    {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        getUsers();
        return view;
    }

    private void getUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(Data.USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user=userSnapshot.getValue(User.class);
                    user.setId(userSnapshot.getKey());
                    users.add(user);
                }
                setUserAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUserAdapter()
    {
        UsersAdapter usersAdapter = new UsersAdapter(getActivity(), users, this);
        userRecyclerView.setAdapter(usersAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @OnClick(R.id.floating_user)
    void addUser() {
        Intent intent = new Intent(getActivity(), AddUser.class);
        startActivity(intent);
    }

    @Override
    public void onUserClicked(int position) {
        User user=users.get(position);
        Intent intent=new Intent(getActivity(),UserDetails.class);
        Bundle bundle=new Bundle();
        bundle.putString(UserDetails.USER_ID,user.getId());
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
