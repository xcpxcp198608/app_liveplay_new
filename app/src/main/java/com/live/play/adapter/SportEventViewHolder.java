package com.live.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.live.play.R;

/**
 * Channel ViewHolder
 */

public class SportEventViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTime;
    public TextView tvTeam1;
    public TextView tvTeam2;
    public TextView tvVS;
    public RecyclerView rcvData;

    public SportEventViewHolder(View itemView) {
        super(itemView);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvTeam1 = itemView.findViewById(R.id.tvTeam1);
        tvTeam2 = itemView.findViewById(R.id.tvTeam2);
        tvVS = itemView.findViewById(R.id.tvVS);
        rcvData = itemView.findViewById(R.id.rcvData);

    }
}
