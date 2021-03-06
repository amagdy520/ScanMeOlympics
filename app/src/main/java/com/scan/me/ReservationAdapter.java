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

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {
    Context mContext;
    List<Reservation> reservations;

    public ReservationAdapter(Context mContext, List<Reservation> reservations) {
        this.mContext = mContext;
        this.reservations = reservations;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reservation_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, fromTextView, toTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.reservation_name);
            fromTextView = (TextView) itemView.findViewById(R.id.from_time);
            toTextView = (TextView) itemView.findViewById(R.id.to_time);
        }

        void bind(int position) {
            Reservation reservation = reservations.get(position);
            nameTextView.setText(reservation.getName());
            fromTextView.setText(reservation.getFrom());
            toTextView.setText(reservation.getTo());
        }

    }

}
