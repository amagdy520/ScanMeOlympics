package com.scan.me;


import android.content.Intent;
import android.os.Bundle;
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
import com.scan.me.User.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class LecturesFragment extends Fragment implements LecturesAdapter.OnLectureClickListener {

    public static final String HISTORY = "History";
    public static final String TODAY = "TODAY";
    @BindView(R.id.lecture_recycler)
    RecyclerView lectureRecyclerView;
    User user;
    String type;
    private String today;

    public void setUser(User user,String type) {
        this.user = user;
        this.type=type;
    }

    List<Reservation> reservations = new ArrayList<>();

    public LecturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lectures, container, false);
        ButterKnife.bind(this, view);
        Calendar mcurrentTime = Calendar.getInstance();
        int year = mcurrentTime.get(Calendar.YEAR);
        int month = mcurrentTime.get(Calendar.MONTH);
        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        today = year + "-" + month + "-" + day;
        if(type.equals(HISTORY)){
            getHistoryLectureDate();
        }else {
            getLectureDate();
        }
        return view;
    }

    private void getLectureDate() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
//        String child = "";
//        String value = "";
//        if (user.getType().equals(User.TUTOR)) {
//            child = "tutorId";
//            value = user.getId();
//        } else if (user.getType().equals(User.STUDENT)) {
//            child = "hash";
//            value = user.getHash();
//        }
        reference.child(Data.USERS)
                .child(user.getId())
                .child(Data.LECTURES)
                .orderByChild("date")
                .equalTo(today)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        reservations = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reservation reservation = snapshot.getValue(Reservation.class);
                            reservation.setId(snapshot.getKey());
                            reservations.add(reservation);

                        }
                        setLectureRecycler();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    private void getHistoryLectureDate() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
//        String child = "";
//        String value = "";
//        if (user.getType().equals(User.TUTOR)) {
//            child = "tutorId";
//            value = user.getId();
//        } else if (user.getType().equals(User.STUDENT)) {
//            child = "hash";
//            value = user.getHash();
//        }
        reference.child(Data.USERS)
                .child(user.getId())
                .child(Data.LECTURES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        reservations = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reservation reservation = snapshot.getValue(Reservation.class);
                            reservation.setId(snapshot.getKey());
                            reservations.add(reservation);

                        }
                        setLectureRecycler();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setLectureRecycler() {
        LecturesAdapter adapter = new LecturesAdapter(getActivity(), reservations, this);
        lectureRecyclerView.setAdapter(adapter);
        lectureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onLectureClicked(int position) {
        Reservation reservation = reservations.get(position);
        Intent intent = new Intent(getActivity(), LectureDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString(LectureDetails.USER_TYPE, user.getType());
        bundle.putString(LectureDetails.LECTURE_ID, reservation.getId());
        intent.putExtras(bundle);
        startActivity(intent);


    }
}
