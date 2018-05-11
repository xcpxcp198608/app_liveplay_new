package com.live.play.instance;

import com.px.common.constant.CommonApplication;
import com.live.play.pojo.ChannelInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * application
 */

public class Application extends CommonApplication {

    public static String PATH_AD_IMAGE;
    public static String PATH_DOWNLOAD;
    private static ExecutorService executorService;
    private static Map<String, List<ChannelInfo>> channelInfoMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        PATH_AD_IMAGE = getExternalFilesDir("ad_images").getAbsolutePath();
        PATH_DOWNLOAD = getExternalFilesDir("download").getAbsolutePath();
        executorService = Executors.newCachedThreadPool();
    }

    public static ExecutorService getExecutorService(){
        return executorService;
    }

    public static List<ChannelInfo> getChannelInfoList() {
        return channelInfoMap.get("channelInfoList");
    }

    public static void setChannelInfoList(List<ChannelInfo> channelInfoList) {
        channelInfoMap.put("channelInfoList", channelInfoList);
    }

    public static void cleanChannelInfoList() {
        channelInfoMap.clear();
    }
}
