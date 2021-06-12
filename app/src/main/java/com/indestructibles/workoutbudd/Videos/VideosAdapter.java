package com.indestructibles.workoutbudd.Videos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.indestructibles.workoutbudd.R;

import java.util.ArrayList;

/**
 * Created by Léonard Huang on 12/06/2021.
 **/
public class VideosAdapter extends ArrayAdapter<Videos> {

    private Context mContext;
    private int mResource;

    public VideosAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Videos> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource,parent,false);

        ImageView imageView = convertView.findViewById(R.id.miniature);

        TextView txtDescription = convertView.findViewById(R.id.descriptionText);

        TextView txtAuteur = convertView.findViewById(R.id.auteurText);

        imageView.setImageResource(getItem(position).getImage());

        txtDescription.setText(getItem(position).getDescription());

        txtAuteur.setText(getItem(position).getAuthor());

        return convertView;
    }
}
