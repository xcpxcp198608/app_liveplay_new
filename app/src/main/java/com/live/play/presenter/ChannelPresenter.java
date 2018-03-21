package com.live.play.presenter;

import com.live.play.model.AdImageProvider;
import com.live.play.model.ChannelLoadService;
import com.live.play.model.ChannelProvider;
import com.live.play.model.LoadService;
import com.live.play.pay.PayProvider;
import com.live.play.pay.PayResultInfo;
import com.live.play.pojo.ChannelInfo;
import com.live.play.pojo.ImageInfo;
import com.live.play.pojo.LiveChannelInfo;
import com.live.play.view.activity.Channel;
import com.px.common.http.pojo.ResultInfo;

import java.util.List;

/**
 * channel presenter
 */

public class ChannelPresenter extends BasePresenter {

    private Channel channel;
    AdImageProvider adImageProvider;
    ChannelProvider channelProvider;
    PayProvider payProvider;

    public ChannelPresenter(Channel channel) {
        this.channel = channel;
        adImageProvider= new AdImageProvider();
        channelProvider = new ChannelProvider();
        payProvider = new PayProvider();
    }

    //调用model - loadService 获取需要的Image文件
    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    channel.onLoadAdImage(execute, imageInfo);
                }
            });
        }
    }

    //调用model - channelLoadService 获取需要的Image文件
    public void loadChannel(String type){
        if(channelProvider != null){
            channelProvider.load(type, new ChannelLoadService.OnLoadListener<List<ChannelInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelInfo> channelInfos) {
                    channel.loadChannel(execute, channelInfos);
                }
            });
        }
    }

    public void loadLiveChannel(){
        if(channelProvider != null){
            channelProvider.loadLiveChannel(new ChannelLoadService.OnLoadListener<List<LiveChannelInfo>>() {
                @Override
                public void onLoad(boolean execute, List<LiveChannelInfo> liveChannelInfoList) {
                    channel.loadLiveChannel(execute, liveChannelInfoList);
                }
            });
        }
    }

    public void loadFavorite(){
        if(channelProvider != null){
            channelProvider.loadFavorite(new ChannelLoadService.OnLoadListener<List<ChannelInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelInfo> channelInfos) {
                    channel.loadFavorite(execute, channelInfos);
                }
            });
        }
    }

    public void loadHistory(){
        if(channelProvider != null){
            channelProvider.loadHistory(new ChannelLoadService.OnLoadListener<List<ChannelInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelInfo> channelInfos) {
                    channel.loadHistory(execute, channelInfos);
                }
            });
        }
    }

    public void loadSearch(String key){
        if(channelProvider != null){
            channelProvider.loadSearch(key, new ChannelLoadService.OnLoadListener<List<ChannelInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelInfo> channelInfos) {
                    channel.loadSearch(execute, channelInfos);
                }
            });
        }
    }

    public void verifyPay(String payerName, int publisherId, String paymentId){
        if(payProvider != null){
            payProvider.payVerify(payerName, publisherId, paymentId, new LoadService.OnLoadListener<ResultInfo<PayResultInfo>>() {
                @Override
                public void onLoad(boolean execute, ResultInfo<PayResultInfo> payResultInfoResultInfo) {
                    channel.onPayVerify(execute, payResultInfoResultInfo);
                }
            });
        }
    }
}
