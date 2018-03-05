package com.live.play.presenter;

import com.live.play.model.AdImageProvider;
import com.live.play.model.LoadService;
import com.live.play.model.UpgradeProvider;
import com.live.play.pojo.ImageInfo;
import com.live.play.view.activity.Common;

/**
 * splash presenter
 */

public class MainPresenter extends BasePresenter<Common> {

    AdImageProvider adImageProvider;
    UpgradeProvider upgradeProvider;
    private Common common;

    public MainPresenter(Common common){
        this.common = common;
        adImageProvider = new AdImageProvider();
        upgradeProvider = new UpgradeProvider();
    }

    //调用model - AdImageProvider 获取需要的Image文件
    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    common.onLoadAdImage(execute, imageInfo);
                }
            });
        }
    }

}
