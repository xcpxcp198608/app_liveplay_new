package com.live.play.presenter;

import com.live.play.model.AdImageProvider;
import com.live.play.model.ChannelTypeProvider;
import com.live.play.model.LoadService;
import com.live.play.model.LoadServiceWithParam;
import com.live.play.pojo.ChannelTypeInfo;
import com.live.play.pojo.ImageInfo;
import com.live.play.view.activity.ChannelType;

import java.util.List;

/**
 * channel type presenter
 */

public class ChannelTypePresenter extends BasePresenter<ChannelType> {

    private ChannelType channelType;
    AdImageProvider adImageProvider;
    ChannelTypeProvider channelTypeProvider;


    public ChannelTypePresenter(ChannelType channelType) {
        this.channelType = channelType;
        adImageProvider = new AdImageProvider();
        channelTypeProvider = new ChannelTypeProvider();
    }

    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    channelType.onLoadAdImage(execute, imageInfo);
                }
            });
        }
    }

    public void loadChannelType(String type){
        if(channelTypeProvider != null){
            channelTypeProvider.load(type, new LoadServiceWithParam.OnLoadListener<List<ChannelTypeInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelTypeInfo> channelTypeInfoList) {
                    channelType.onLoadChannelType(execute , channelTypeInfoList);
                }
            });
        }
    }
}
