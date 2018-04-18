package com.scan.me.HomeScreen;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.AddRoom;
import com.scan.me.Data;
import com.scan.me.R;
import com.scan.me.Room;
import com.scan.me.RoomDetails;
import com.scan.me.User.User;
import com.scan.me.User.UserDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoomsFragment extends Fragment implements RoomsAdapter.OnRoomClickListener {
    @BindView(R.id.floating_room)
    FloatingActionButton floating_room;
    @BindView(R.id.room_recycler)
    RecyclerView roomsRecyclerView;
    List<Room> rooms = new ArrayList<>();
    User user;

    public void setUser(User user) {
        this.user = user;
    }

    public RoomsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        ButterKnife.bind(this, view);
        getRooms();
        if(!user.getType().equals(User.ADMIN)){
            floating_room.setVisibility(View.GONE);
        }
        return view;
    }

    private void getRooms() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Data.ROOMS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rooms = new ArrayList<>();

                for (DataSnapshot roomsSnapshot : dataSnapshot.getChildren()) {
                    Room room = roomsSnapshot.getValue(Room.class);
                    room.setId(roomsSnapshot.getKey());
                    rooms.add(room);
                }
                setRoomAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setRoomAdapter() {
        RoomsAdapter roomsAdapter = new RoomsAdapter(getActivity(), rooms, this);
        roomsRecyclerView.setAdapter(roomsAdapter);
        roomsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2,LinearLayoutManager.VERTICAL,false));
    }

    @OnClick(R.id.floating_room)
    void addRoom() {
        Intent intent = new Intent(getActivity(), AddRoom.class);
        startActivity(intent);
    }

    @Override
    public void onRoomClicked(int position) {
        Room room = rooms.get(position);
        Intent intent = new Intent(getActivity(), RoomDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString(RoomDetails.ROOM_ID, room.getId());
        bundle.putString(RoomDetails.USER, user.getId());
        bundle.putString(RoomDetails.TYPE, user.getType());
        bundle.putString(RoomDetails.NAME, user.getName());
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
