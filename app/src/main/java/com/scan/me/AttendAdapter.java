package com.scan.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.scan.me.HomeScreen.UsersAdapter;
import com.scan.me.User.User;

import java.util.List;

/**
 * Created by mido on 11/04/18.
 */

public class AttendAdapter extends RecyclerView.Adapter<AttendAdapter.MyViewHolder> {



    List<UserAttend> userList;
    Context mContext;
    OnUserClickListener mOnUserClickListener;
    String userType;
    boolean attend;

    public AttendAdapter( Context mContext,List<UserAttend> userList, OnUserClickListener mOnUserClickListener, String userType, boolean attend) {
        this.userList = userList;
        this.mContext = mContext;
        this.mOnUserClickListener = mOnUserClickListener;
        this.userType = userType;
        this.attend = attend;
    }

    @Override
    public AttendAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_attend, parent, false);
        return new AttendAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttendAdapter.MyViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        TextView nameTextView;
        Switch attendASwitch;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.user_name);
            attendASwitch = (Switch) itemView.findViewById(R.id.attend);
            if (userType.equals(User.TUTOR)) {
                attendASwitch.setOnClickListener(this);
            } else if (attend && userType.equals(User.STUDENT)) {
                attendASwitch.setOnClickListener(this);
            }else {
                attendASwitch.setClickable(false);
            }


        }

        void bind(int position) {
            UserAttend user = userList.get(position);
            nameTextView.setText(user.getName());
            attendASwitch.setChecked(user.attend);


        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }

        @Override
        public void onClick(View v) {
            mOnUserClickListener.onSwitchClicked(getAdapterPosition());
        }
    }

    public interface OnUserClickListener {
        void onSwitchClicked(int position);
    }
}
