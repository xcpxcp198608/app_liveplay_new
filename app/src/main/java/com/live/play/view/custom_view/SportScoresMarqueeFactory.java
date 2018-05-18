package com.live.play.view.custom_view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongwen.marqueen.MarqueeFactory;
import com.live.play.R;
import com.live.play.pojo.SportEventScoresInfo;

/**
 * Created by patrick on 2018/5/18.
 * create time : 9:39 AM
 */

public class SportScoresMarqueeFactory extends MarqueeFactory<FrameLayout, SportEventScoresInfo> {

    private LayoutInflater inflater;

    public SportScoresMarqueeFactory(Context mContext) {
        super(mContext);
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    protected FrameLayout generateMarqueeItemView(SportEventScoresInfo data) {
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.item_rcv_sport_event_scores1, null);
        TextView tvType = view.findViewById(R.id.tvType);
        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView tvTeam1 = view.findViewById(R.id.tvTeam1);
        TextView tvTeam1Score = view.findViewById(R.id.tvTeam1Score);
        TextView tvTeam2 = view.findViewById(R.id.tvTeam2);
        TextView tvTeam2Score = view.findViewById(R.id.tvTeam2Score);
        TextView tvStatus = view.findViewById(R.id.tvStatus);
        tvType.setText(data.getMatch_type());
        tvTime.setText(data.getMatch_time());
        tvTeam1.setText(data.getMatch_guest());
        tvTeam1Score.setText(data.getMatch_guest_score());
        tvTeam2.setText(data.getMatch_master());
        tvTeam2Score.setText(data.getMatch_master_score());
        if("2".equals(data.getMatch_status_code())) {
            tvStatus.setText("In Progress");
            tvStatus.setTextColor(Color.RED);
        }else if("4".equals(data.getMatch_status_code())){
            tvStatus.setText("Finished");
            tvStatus.setTextColor(Color.BLUE);
        }
        return view;
    }
}
