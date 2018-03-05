package com.live.play.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.AppUtil;
import com.live.play.entity.ResultInfo;
import com.live.play.instance.Constant;
import com.live.play.pojo.UpgradeInfo;

import java.io.IOException;

import javax.inject.Inject;

/**
 * upgrade provider
 */

public class UpgradeProvider implements LoadService<UpgradeInfo> {

    @Inject
    public UpgradeProvider() {
        
    }

    @Override
    public void load(final OnLoadListener<UpgradeInfo> onLoadListener) {
        HttpMaster.get(Constant.url.upgrade)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        ResultInfo<UpgradeInfo> resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo<UpgradeInfo>>(){}.getType());
                        if(resultInfo == null){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        if(resultInfo.getCode() != ResultInfo.CODE_OK){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        UpgradeInfo upgradeInfo = resultInfo.getData().get(0);
                        if(upgradeInfo == null){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(AppUtil.isNeedUpgrade(upgradeInfo.getCode()), upgradeInfo);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                    }
                });
    }
}
