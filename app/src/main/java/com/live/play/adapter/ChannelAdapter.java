package com.live.play.adapter;

import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.image.ImageMaster;
import com.live.play.R;
import com.live.play.pojo.ChannelInfo;

import java.util.List;

/**
 * channel adapter
 */

public class ChannelAdapter extends BaseRecycleAdapter<ChannelViewHolder> {

    private List<ChannelInfo> channelInfoList;

    public ChannelAdapter(List<ChannelInfo> channelInfoList) {
        this.channelInfoList = channelInfoList;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_rcv_channel;
    }

    @Override
    protected ChannelViewHolder createHolder(View view) {
        return new ChannelViewHolder(view);
    }

    @Override
    protected void bindHolder(final ChannelViewHolder holder, final int position) {
        ChannelInfo channelInfo = channelInfoList.get(position);
        holder.textView.setText(channelInfo.getName());
        ImageMaster.load(channelInfo.getIcon(), holder.imageView, R.drawable.img_hold3,
                R.drawable.img_hold3);
    }

    @Override
    public int getItemCounts() {
        return channelInfoList.size();
    }
}
