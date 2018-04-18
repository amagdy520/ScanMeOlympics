package com.scan.me.HomeScreen;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scan.me.R;
import com.scan.me.Room;
import com.scan.me.User.User;

import java.util.List;

/**
 * Created by COYG on 2018/04/11.
 */

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.MyViewHolder> {
    Context context;
    List<Room> roomList;
    OnRoomClickListener mOnRoomClickListener;


    public RoomsAdapter(Context context, List<Room> roomList, OnRoomClickListener mOnRoomClickListener) {
        this.context = context;
        this.roomList = roomList;
        this.mOnRoomClickListener = mOnRoomClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.room_row, parent, false);
        return new RoomsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView number;
        ImageView roomImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            roomImage = (ImageView) itemView.findViewById(R.id.room_image);
            number = (TextView) itemView.findViewById(R.id.room_number);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Room room = roomList.get(position);

            number.setText(room.getType()+" "+room.getNumber());
            if (room.getType().equals(Room.HALL)) {
                Glide.with(context).load(R.drawable.hall).into(roomImage);

            } else if (room.getType().equals(Room.LAB)) {
                Glide.with(context).load(R.drawable.lab).into(roomImage);
            } else {
                Glide.with(context).load(R.drawable.stage).into(roomImage);
            // absent
            }
        }

        @Override
        public void onClick(View v) {
            mOnRoomClickListener.onRoomClicked(getAdapterPosition());
        }
    }
    interface OnRoomClickListener{
        void onRoomClicked(int position);
    }
}
