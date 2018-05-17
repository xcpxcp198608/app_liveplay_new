package com.live.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.live.play.R;

/**
 * Channel ViewHolder
 */

public class SportEventScoresViewHolder extends RecyclerView.ViewHolder {

    public TextView tvType;
    public TextView tvTime;
    public SuperTextView tvTeam1;
    public SuperTextView tvTeam2;

    public SportEventScoresViewHolder(View itemView) {
        super(itemView);
        tvType = itemView.findViewById(R.id.tvType);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvTeam1 = itemView.findViewById(R.id.tvTeam1);
        tvTeam2 = itemView.findViewById(R.id.tvTeam2);

    }
}
