package com.live.play.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.Logger;
import com.live.play.entity.ResultInfo;
import com.live.play.instance.Constant;
import com.live.play.pojo.ChannelInfo;
import com.live.play.pojo.LiveChannelInfo;
import com.live.play.sql.FavoriteChannelDao;
import com.live.play.sql.HistoryChannelDao;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * channel provider
 */

public class ChannelProvider implements ChannelLoadService<List<ChannelInfo>> {

    @Inject
    public ChannelProvider() {

    }

    @Override
    public void load(String type, final OnLoadListener<List<ChannelInfo>> onLoadListener) {
        HttpMaster.get(Constant.url.channel+type+Constant.url.token)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        ResultInfo<ChannelInfo> resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo<ChannelInfo>>(){}.getType());
                        if(resultInfo == null){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        List<ChannelInfo> channelInfoList = resultInfo.getData();
                        if(channelInfoList == null || channelInfoList.size() <= 0){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(true, channelInfoList);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                    }
                });
    }

    public void loadLiveChannel(final OnLoadListener<List<LiveChannelInfo>> onLoadListener){
        HttpMaster.get(Constant.url.live_channel)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        List<LiveChannelInfo> liveChannelInfoList = new Gson().fromJson(s,
                                new TypeToken<List<LiveChannelInfo>>(){}.getType());
                        if(liveChannelInfoList == null || liveChannelInfoList.size() <= 0){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(true, liveChannelInfoList);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                    }
                });
    }

    @Override
    public void loadFavorite(OnLoadListener<List<ChannelInfo>> onLoadListener) {
        FavoriteChannelDao favoriteChannelDao = FavoriteChannelDao.getInstance();
        List<ChannelInfo> channelInfoList = favoriteChannelDao.queryAll();
        if(channelInfoList == null || channelInfoList.size() <= 0){
            onLoadListener.onLoad(false, null);
            return;
        }
        onLoadListener.onLoad(true, channelInfoList);
    }

    @Override
    public void loadHistory(OnLoadListener<List<ChannelInfo>> onLoadListener) {
        try{
            HistoryChannelDao historyChannelDao = HistoryChannelDao.getInstance();
            historyChannelDao.delete();
            List<ChannelInfo> channelInfoList = historyChannelDao.queryAll();
            if(channelInfoList == null || channelInfoList.size() <= 0){
                onLoadListener.onLoad(false, null);
                return;
            }
            onLoadListener.onLoad(true, channelInfoList);
        }catch(Exception e){
            Logger.d(e.getMessage());
            onLoadListener.onLoad(false, null);
        }
    }

    @Override
    public void loadSearch(String key, final OnLoadListener<List<ChannelInfo>> onLoadListener) {
        if(TextUtils.isEmpty(key)){
            onLoadListener.onLoad(false, null);
            return;
        }
        HttpMaster.get(Constant.url.channel_search + key + Constant.url.token)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        ResultInfo<ChannelInfo> resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo<ChannelInfo>>(){}.getType());
                        if(resultInfo == null){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        List<ChannelInfo> channelInfoList = resultInfo.getData();
                        if(channelInfoList == null || channelInfoList.size() <= 0){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(true, channelInfoList);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                    }
                });
    }
}
