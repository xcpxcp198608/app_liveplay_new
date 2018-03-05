package com.live.play.presenter;

import com.live.play.model.AdImageProvider;
import com.live.play.model.UpgradeProvider;
import com.live.play.model.LoadService;
import com.live.play.pojo.ImageInfo;
import com.live.play.pojo.UpgradeInfo;
import com.live.play.view.activity.Splash;

/**
 * splash presenter
 */

public class SplashPresenter extends BasePresenter<Splash> {

    AdImageProvider adImageProvider;
    UpgradeProvider upgradeProvider;
    private Splash splash;

    public SplashPresenter(Splash splash){
        this.splash = splash;
        adImageProvider = new AdImageProvider();
        upgradeProvider = new UpgradeProvider();
    }

    //调用model - AdImageProvider 获取需要的Image文件
    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    splash.onLoadAdImage(execute, imageInfo);
                }
            });
        }
    }

    //检查app upgradeProvider
    public void checkUpgrade(){
            upgradeProvider.load(new LoadService.OnLoadListener<UpgradeInfo>() {
                @Override
                public void onLoad(boolean execute, UpgradeInfo upgradeInfo) {
                    splash.checkUpgrade(execute, upgradeInfo);
                }
            });
    }
}
