package com.scan.me.HomeScreen;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scan.me.R;
import com.scan.me.Room;
import com.scan.me.User.User;

import java.util.List;

/**
 * Created by COYG on 2018/04/11.
 */

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.MyViewHolder>
{
    List<Room> roomList;
    Context context;

    public RoomsAdapter(Context context, List<Room> roomList)
    {
        this.roomList = roomList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new RoomsAdapter.MyViewHolder (view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.bind(position);
    }

    @Override
    public int getItemCount()
    {
        return roomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView number,type;

        public MyViewHolder(View itemView)
        {
            super (itemView);
            type= (TextView) itemView.findViewById(R.id.user_name);
            number= (TextView) itemView.findViewById(R.id.user_type);
        }

        void bind(int position)
        {
            Room room=roomList.get(position);
            type.setText(room.getType ());
            number.setText(room.getNumber ());
        }

    }
}
