package com.example.brumor.geofiretest;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by brumor on 11/6/17.
 */

public class GeomessagesListAdapter extends ArrayAdapter<GeoMessage> {

    public GeomessagesListAdapter(@NonNull Context context, int resource, List<GeoMessage> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView  = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.geomessage, parent, false);

        }

        GeoMessage current = getItem(position);

        TextView message = convertView.findViewById(R.id.geomessage_textview);

        String geomessage = current.getContent();

        message.setText(geomessage);

        return convertView;
    }
}
