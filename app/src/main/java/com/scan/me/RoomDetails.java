package com.scan.me;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.User.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoomDetails extends AppCompatActivity {
    public static final int RESERVE = 201;
    public static final String ROOM_ID = "RoomId";
    public static final String USER = "User";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String ROOM_RESERVE = "Room Reserve";
    private String roomId;
    private Room room;
//    private User user;
    List<Reservation> reservations;

    @BindView(R.id.room_number)
    TextView roomNumberTextView;
    @BindView(R.id.room_floor)
    TextView roomFloor;
    @BindView(R.id.room_latitude)
    TextView room_latitude;
    @BindView(R.id.room_longitude)
    TextView room_longitude;
    @BindView(R.id.reservation_recycler)
    RecyclerView reservationRecyclerView;
    private DatabaseReference reference;
    private String today;
    @BindView(R.id.room_image)
    ImageView roomImage;
    @BindView(R.id.reserve)
    FloatingActionButton reservebutButton;
    String type,name,userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);
        userId = getIntent().getExtras().getString(USER);
        type = getIntent().getExtras().getString(TYPE);
        name = getIntent().getExtras().getString(NAME);
        ButterKnife.bind(this);
        roomId = getIntent().getExtras().getString(ROOM_ID);
        Calendar mcurrentTime = Calendar.getInstance();
        int year = mcurrentTime.get(Calendar.YEAR);
        int month = mcurrentTime.get(Calendar.MONTH);
        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        today = year + "-" + month + "-" + day;
        if(!type.equals(User.TUTOR)){
            reservebutButton.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getRoomData();
    }

    private void getRoomData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.ROOMS).child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                room = dataSnapshot.getValue(Room.class);
                setRoomData();
                reservations = new ArrayList<>();
                for (DataSnapshot reservationSnapshot : dataSnapshot.child(Data.RESERVATION).child(today).getChildren()) {
                    reservations.add(reservationSnapshot.getValue(Reservation.class));
                }
                setReservationRecycler();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setReservationRecycler() {
        ReservationAdapter adapter = new ReservationAdapter(this, reservations);
        reservationRecyclerView.setAdapter(adapter);
        reservationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    private void setRoomData() {
        roomNumberTextView.setText(room.getType() + " " + room.getNumber());
        roomFloor.setText("Floor: " + room.getFloor());
        room_latitude.setText("Latitude: " + room.getLatitude());
        room_longitude.setText("Longitude: " + room.getLatitude());
        if (room.getType().equals(Room.HALL)) {
            Glide.with(this).load(R.drawable.hall).into(roomImage);

        } else if (room.getType().equals(Room.LAB)) {
            Glide.with(this).load(R.drawable.lab).into(roomImage);
        } else {
            Glide.with(this).load(R.drawable.stage).into(roomImage);

        }
    }



    @OnClick(R.id.reserve)
    void openReserve() {
        startActivityForResult(new Intent(this, ReserveActivity.class), RESERVE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESERVE && resultCode == RESULT_OK) {
            getReserveStudents(data);
        }
    }

    private void getReserveStudents(Intent data) {
        final Reservation reservation = data.getExtras().getParcelable(ROOM_RESERVE);
        reservation.setTutorId(userId);
        reservation.setTutorName(name);
        reservation.setFloor(room.getFloor());
        reservation.setRoomNumber(room.getNumber());

        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).orderByChild("hash").equalTo(reservation.getHash())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<UserAttend> users = new ArrayList<UserAttend>();
                        for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                            User user = usersSnapshot.getValue(User.class);
                            users.add(new UserAttend(user.getId(), user.getUid(), user.getName(), user.getImage(), user.getHash(), false));
                        }
                        reserve(users, reservation);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    private void reserve(ArrayList<UserAttend> users, Reservation reservation) {

        //reserve in room
        reservation.setDate(today);
        reference.keepSynced(true);
        reference.child(Data.ROOMS)
                .child(roomId)
                .child(Data.RESERVATION)
                .child(today)
                .push()
                .setValue(reservation);
        // add lecture
        String key = reference.child(Data.LECTURES)
                .push().getKey();
        reference.child(Data.LECTURES)
                .child(key)
                .setValue(reservation);

        reference.child(Data.USERS)
                .child(userId)
                .child(Data.LECTURES)
                .child(key)
                .setValue(reservation);

        reservation.setId(key);
        for (UserAttend userAttend : users) {
            reference.child(Data.LECTURES)
                    .child(key)
                    .child(Data.STUDENTS)
                    .child(userAttend.getUid())
                    .setValue(userAttend);
            reference.child(Data.USERS)
                    .child(userAttend.getId())
                    .child(Data.LECTURES)
                    .child(key)
                    .setValue(reservation);
        }
        Toast.makeText(this, "Reserved", Toast.LENGTH_SHORT).show();


    }
}
