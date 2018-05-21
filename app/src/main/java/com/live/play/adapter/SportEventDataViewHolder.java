package com.live.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.live.play.R;

/**
 * Channel ViewHolder
 */

public class SportEventDataViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivTVIcon;
    public TextView tvTVName;
    public LinearLayout llSportEventData;

    public SportEventDataViewHolder(View itemView) {
        super(itemView);
        ivTVIcon = itemView.findViewById(R.id.ivTVIcon);
        tvTVName = itemView.findViewById(R.id.tvTVName);
        llSportEventData = itemView.findViewById(R.id.llSportEventData);

    }
}
