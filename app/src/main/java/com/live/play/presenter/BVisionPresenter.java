package com.live.play.presenter;

import com.live.play.model.AdImageProvider;
import com.live.play.model.BVisionProvider;
import com.live.play.model.ChannelTypeProvider;
import com.live.play.model.LoadService;
import com.live.play.model.LoadServiceWithParam;
import com.live.play.pojo.ChannelTypeInfo;
import com.live.play.pojo.ImageInfo;
import com.live.play.pojo.LDFamInfo;
import com.live.play.view.activity.BVision;

import java.util.List;

/**
 * channel type presenter
 */

public class BVisionPresenter extends BasePresenter<BVision> {

    private BVision bVision;
    AdImageProvider adImageProvider;
    ChannelTypeProvider channelTypeProvider;
    BVisionProvider bVisionProvider;


    public BVisionPresenter(BVision bVision) {
        this.bVision = bVision;
        adImageProvider = new AdImageProvider();
        channelTypeProvider = new ChannelTypeProvider();
        bVisionProvider = new BVisionProvider();
    }

    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    bVision.onLoadAdImage(execute, imageInfo);
                }
            });
        }
    }

    public void loadChannelType(String type){
        if(channelTypeProvider != null){
            channelTypeProvider.load(type, new LoadServiceWithParam.OnLoadListener<List<ChannelTypeInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelTypeInfo> channelTypeInfoList) {
                    bVision.onLoadChannelType(execute , channelTypeInfoList);
                }
            });
        }
    }

    public void loadLDFam(){
        if(bVisionProvider != null){
            bVisionProvider.load(new LoadService.OnLoadListener<List<LDFamInfo>>() {
                @Override
                public void onLoad(boolean execute, List<LDFamInfo> ldFamInfoList) {
                    bVision.onLoadLDFam(execute, ldFamInfoList);
                }
            });
        }
    }
}
