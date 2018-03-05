package com.live.play.view.activity;

import com.live.play.pojo.ChannelTypeInfo;

import java.util.List;

/**
 * channel type
 */

public interface ChannelType extends Common {

    void onLoadChannelType(boolean execute, List<ChannelTypeInfo> channelTypeInfoList);
}
