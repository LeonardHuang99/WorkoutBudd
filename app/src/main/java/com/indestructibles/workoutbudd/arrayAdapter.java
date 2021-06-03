package com.indestructibles.workoutbudd;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Léonard Huang on 31/05/2021.
 **/
public class arrayAdapter extends ArrayAdapter<Cards> {
    Context context;

    public arrayAdapter(Context context,int resourceId, List<Cards> items){
        super(context,resourceId,items);
        this.context = context;
    }


    public View getView(int position, View convertView, ViewGroup parent){
        Cards card_item =  getItem(position);
        //The convert view has all the layout
        if (convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            //LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.item,parent,false);
        }
        //That's why we are putting convertView before findViewById
        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(card_item.getName());

        switch (card_item.getProfileImageUrl()){
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                break;

            default:
                Glide.with(convertView.getContext()).clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;

        }

        return convertView;

    }
}
