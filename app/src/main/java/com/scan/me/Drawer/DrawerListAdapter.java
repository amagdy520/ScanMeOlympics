package com.scan.me.Drawer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scan.me.R;

import java.util.List;




/**
 * Created by mido on 5/1/17.
 */

class DrawerListAdapter extends ArrayAdapter<DrawerFragment.DrawerData> {


    DrawerListAdapter(Context context, List<DrawerFragment.DrawerData> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_row, parent, false);
        }
        ImageView vector = (ImageView) convertView.findViewById(R.id.navigation_image);
        TextView title = (TextView) convertView.findViewById(R.id.navigation_title);
        vector.setImageResource(getItem(position).image);
        title.setText(getItem(position).title);

        return convertView;
    }
}
