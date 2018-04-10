package com.scan.me.HomeScreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scan.me.R;
import com.scan.me.User.User;

import java.util.List;

/**
 * Created by mido on 10/04/18.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    List<User> userList;
    Context mContext;
    OnUserClickListener mOnUserClickListener;

    public UsersAdapter( Context mContext,List<User> userList, OnUserClickListener mOnUserClickListener) {
        this.userList = userList;
        this.mContext = mContext;
        this.mOnUserClickListener = mOnUserClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, type;

        public MyViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.user_name);
            type= (TextView) itemView.findViewById(R.id.user_type);
            itemView.setOnClickListener(this);

        }
        void bind(int position){
            User user=userList.get(position);
            name.setText(user.getName());
            type.setText(user.getType());
        }

        @Override
        public void onClick(View v) {
            mOnUserClickListener.onUserClicked(getAdapterPosition());
        }
    }

    public interface OnUserClickListener {
        void onUserClicked(int position);
    }
}