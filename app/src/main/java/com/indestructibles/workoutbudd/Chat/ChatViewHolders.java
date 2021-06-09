package com.indestructibles.workoutbudd.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indestructibles.workoutbudd.R;

import org.jetbrains.annotations.NotNull;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mMessage;
    public LinearLayout mContainer;

    public ChatViewHolders(@NonNull @NotNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);

    }

    @Override
    public void onClick(View view) {

    }
}
