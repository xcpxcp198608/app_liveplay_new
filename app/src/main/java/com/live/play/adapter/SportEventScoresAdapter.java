package com.live.play.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.view.View;

import com.live.play.R;
import com.live.play.pojo.SportEventInfo;
import com.live.play.pojo.SportEventScoresInfo;
import com.live.play.view.activity.PlaySportEventActivity;
import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.constant.CommonApplication;

import java.util.List;

/**
 * channel type adapter
 */

public class SportEventScoresAdapter extends BaseRecycleAdapter<SportEventScoresViewHolder> {

    //String str1=String.format("价格 ：<font color=\"#d40000\">%s", String.format("￥%1$.2f元", item.getPrice()));

    private List<SportEventScoresInfo> sportEventScoresInfoList;
    private Context mContext;

    public SportEventScoresAdapter(Context context, List<SportEventScoresInfo> sportEventScoresInfoList) {
        this.sportEventScoresInfoList = sportEventScoresInfoList;
        this.mContext = context;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_rcv_sport_event_scores;
    }

    @Override
    protected SportEventScoresViewHolder createHolder(View view) {
        return new SportEventScoresViewHolder(view);
    }

    @Override
    protected void bindHolder(SportEventScoresViewHolder holder, int position) {
        final SportEventScoresInfo sportEventScoresInfo = sportEventScoresInfoList.get(position);
        holder.tvType.setText(sportEventScoresInfo.getMatch_type());
        holder.tvTime.setText(sportEventScoresInfo.getMatch_time());
        holder.tvTeam1.setCenterTopString(sportEventScoresInfo.getMatch_guest());
        holder.tvTeam1.setCenterString(sportEventScoresInfo.getMatch_guest_score());
        holder.tvTeam2.setCenterTopString(sportEventScoresInfo.getMatch_master());
        holder.tvTeam2.setCenterString(sportEventScoresInfo.getMatch_master_score());
    }

    @Override
    public int getItemCounts() {
        return sportEventScoresInfoList.size();
    }
}
