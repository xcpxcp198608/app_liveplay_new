package com.live.play.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.live.play.R;

/**
 * ChannelTypeViewHolder
 */

public class PlayChannelViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public PlayChannelViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.textView);

    }
}
