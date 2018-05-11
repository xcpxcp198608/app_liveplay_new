package com.live.play.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.live.play.R;
import com.live.play.pojo.SportEventInfo;
import com.live.play.view.activity.PlaySportEventActivity;
import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.px.common.constant.CommonApplication;
import java.util.List;

/**
 * channel type adapter
 */

public class SportEventAdapter extends BaseRecycleAdapter<SportEventViewHolder> {

    private List<SportEventInfo> sportEventInfoList;
    private Context mContext;
    private String key;

    public SportEventAdapter(Context context, List<SportEventInfo> sportEventInfoList, String key) {
        this.sportEventInfoList = sportEventInfoList;
        this.mContext = context;
        this.key = key;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_rcv_sport_event;
    }

    @Override
    protected SportEventViewHolder createHolder(View view) {
        return new SportEventViewHolder(view);
    }

    @Override
    protected void bindHolder(SportEventViewHolder holder, int position) {
        final SportEventInfo sportEventInfo = sportEventInfoList.get(position);
        holder.tvTime.setText(sportEventInfo.getMatch_time());
        holder.tvTeam1.setText(sportEventInfo.getMatch_guest());
        holder.tvTeam2.setText(sportEventInfo.getMatch_master());
        holder.rcvData.setLayoutManager(new GridLayoutManager(CommonApplication.getContext(), 5));

        if("36".equals(key)){
            holder.tvVS.setVisibility(View.GONE);
        }

        List<SportEventInfo.TvData> tvDataList = sportEventInfo.getTv_data();
        if(tvDataList != null && tvDataList.size() >0) {
            SportEventDataAdapter adapter = new SportEventDataAdapter(sportEventInfo.getTv_data());
            holder.rcvData.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    SportEventInfo.TvData tvData = sportEventInfo.getTv_data().get(i);
                    Intent intent = new Intent(mContext, PlaySportEventActivity.class);
                    intent.putExtra("url", tvData.getMatch_tv_url());
                    mContext.startActivity(intent);
                }
            });
            adapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
                @Override
                public void onFocus(View view, int position, boolean hasFocus) {
                    if(hasFocus){
                        view.setSelected(true);
                    }else{
                        view.setSelected(false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCounts() {
        return sportEventInfoList.size();
    }
}
