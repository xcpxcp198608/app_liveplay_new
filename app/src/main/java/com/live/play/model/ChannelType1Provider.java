package com.live.play.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.play.pojo.ChannelInfo;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.http.pojo.ResultInfo;
import com.px.common.utils.Logger;
import com.live.play.instance.Constant;
import com.live.play.pojo.ChannelType1Info;

import java.io.IOException;
import java.util.List;

/**
 * Created by patrick on 14/08/2017.
 * create time : 2:07 PM
 */

public class ChannelType1Provider implements LoadServiceWithParam<List<ChannelType1Info>> {
    @Override
    public void load(String param , final OnLoadListener<List<ChannelType1Info>> onLoadListener) {
//        Logger.d(param);
        HttpMaster.get(Constant.url.channel_type1 + param + Constant.url.token)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
//                        Logger.d(s);
                        ResultInfo<ChannelType1Info> resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo<ChannelType1Info>>(){}.getType());
                        if(resultInfo == null){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        List<ChannelType1Info> channelType1InfoList = resultInfo.getDataList();
                        if(channelType1InfoList != null && channelType1InfoList.size() > 0){
                            onLoadListener.onLoad(true, channelType1InfoList);
                        }else{
                            onLoadListener.onLoad(false, null);
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                        Logger.d(e);
                    }
                });
    }
}
