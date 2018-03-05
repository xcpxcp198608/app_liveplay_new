package com.live.play.view.activity;

import com.live.play.entity.ResultInfo;
import com.live.play.pay.PayResultInfo;
import com.live.play.pojo.ChannelInfo;
import com.live.play.pojo.LiveChannelInfo;

import java.util.List;

/**
 * channel
 */

public interface Channel extends Common {

    void loadChannel(boolean execute, List<ChannelInfo> channelInfoList);
    void loadLiveChannel(boolean execute, List<LiveChannelInfo> liveChannelInfoList);
    void loadFavorite(boolean execute, List<ChannelInfo> channelInfoList);
    void loadHistory(boolean execute, List<ChannelInfo> channelInfoList);
    void loadSearch(boolean execute, List<ChannelInfo> channelInfoList);
    void onPayVerify(boolean execute, ResultInfo<PayResultInfo> resultInfo);
}
