package com.live.play.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.play.instance.Constant;
import com.live.play.pojo.SportEventInfo;
import com.live.play.pojo.SportEventResultInfo;
import com.live.play.pojo.SportEventScoresInfo;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.Logger;

import java.io.IOException;
import java.util.List;


/**
 * channel provider
 */

public class SportEventScoresProvider implements LoadService<List<SportEventScoresInfo>> {

    @Override
    public void load(final OnLoadListener<List<SportEventScoresInfo>> onLoadListener) {
        HttpMaster.get(Constant.url.sports_event_scores)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        SportEventResultInfo<SportEventScoresInfo> sportEventResultInfo = new Gson().fromJson(s,
                                new TypeToken<SportEventResultInfo<SportEventScoresInfo>>(){}.getType());
//                        Logger.d(sportEventResultInfo.toString());
                        if(sportEventResultInfo.getError_code() != 0) {
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        List<SportEventScoresInfo> sportEventScoresInfoList = sportEventResultInfo.getData();
                        if (sportEventScoresInfoList == null || sportEventScoresInfoList.size() <= 0) {
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(true, sportEventScoresInfoList);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                    }
                });
    }
}
