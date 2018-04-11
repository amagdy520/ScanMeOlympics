package com.scan.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mido on 11/04/18.
 */

public class LecturesAdapter extends RecyclerView.Adapter<LecturesAdapter.MyViewHolder> {
    Context mContext;
    List<Reservation> reservations;
    OnLectureClickListener mOnLectureClickListener;

    public LecturesAdapter(Context mContext, List<Reservation> reservations, OnLectureClickListener mOnLectureClickListener) {
        this.mContext = mContext;
        this.reservations = reservations;
        this.mOnLectureClickListener = mOnLectureClickListener;
    }

    @Override
    public LecturesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reservation_row, parent, false);
        return new LecturesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);

    }


    @Override
    public int getItemCount() {
        return reservations.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView, fromTextView, toTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.reservation_name);
            fromTextView = (TextView) itemView.findViewById(R.id.from_time);
            toTextView = (TextView) itemView.findViewById(R.id.to_time);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Reservation reservation = reservations.get(position);
            nameTextView.setText(reservation.getName());
            fromTextView.setText(reservation.getFrom());
            toTextView.setText(reservation.getTo());
        }

        @Override
        public void onClick(View v) {
            mOnLectureClickListener.onLectureClicked(getAdapterPosition());
        }
    }

    interface OnLectureClickListener {
        void onLectureClicked(int position);
    }
}
