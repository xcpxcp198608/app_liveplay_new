package com.live.play.adapter;

import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.image.ImageMaster;
import com.live.play.R;
import com.live.play.pojo.ChannelType1Info;

import java.util.List;

/**
 * channel adapter
 */

public class ChannelType1Adapter extends BaseRecycleAdapter<ChannelType1ViewHolder> {

    private List<ChannelType1Info> channelType1InfoList;

    public ChannelType1Adapter(List<ChannelType1Info> channelType1InfoList) {
        this.channelType1InfoList = channelType1InfoList;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_rcv_channel_type;
    }

    @Override
    protected ChannelType1ViewHolder createHolder(View view) {
        return new ChannelType1ViewHolder(view);
    }

    @Override
    protected void bindHolder(final ChannelType1ViewHolder holder, final int position) {
        ChannelType1Info channelType1Info = channelType1InfoList.get(position);
        holder.textView.setText(channelType1Info.getName());
        ImageMaster.load(channelType1Info.getUrl(), holder.imageView, R.drawable.bg_button_holder,
                R.drawable.bg_button_holder);
    }

    @Override
    public int getItemCounts() {
        return channelType1InfoList.size();
    }
}
