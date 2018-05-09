package com.live.play.adapter;

import android.content.Context;
import android.view.View;

import com.live.play.R;
import com.live.play.pojo.ChannelTypeInfo;
import com.live.play.pojo.SportEventInfo;
import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.image.ImageMaster;

import java.util.List;

/**
 * channel type adapter
 */

public class SportEventDataAdapter extends BaseRecycleAdapter<SportEventDataViewHolder> {

    private List<SportEventInfo.TvData> tvDataList;

    public SportEventDataAdapter(List<SportEventInfo.TvData> tvDataList) {
        this.tvDataList = tvDataList;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_rcv_sport_event_data;
    }

    @Override
    protected SportEventDataViewHolder createHolder(View view) {
        return new SportEventDataViewHolder(view);
    }

    @Override
    protected void bindHolder(SportEventDataViewHolder holder, int position) {
        SportEventInfo.TvData tvData = tvDataList.get(position);
        holder.tvTVName.setText(tvData.getMatch_tv_name());
        ImageMaster.load(tvData.getMatch_tv_icon(), holder.ivTVIcon, R.drawable.img_hold1 ,
                R.drawable.img_hold1);
    }

    @Override
    public int getItemCounts() {
        return tvDataList.size();
    }
}
