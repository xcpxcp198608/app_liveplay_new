package com.live.play.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.play.instance.Constant;
import com.live.play.pojo.LDFamInfo;
import com.live.play.pojo.SportEventInfo;
import com.live.play.pojo.SportEventResultInfo;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.Logger;

import java.io.IOException;
import java.util.List;


/**
 * channel provider
 */

public class SportEventProvider implements LoadServiceWithParam<List<SportEventInfo>> {

    @Override
    public void load(String param, final OnLoadListener<List<SportEventInfo>> onLoadListener) {
        HttpMaster.get(Constant.url.sports_event + param)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        SportEventResultInfo sportEventResultInfo = new Gson().fromJson(s,
                                new TypeToken<SportEventResultInfo>(){}.getType());
//                        Logger.d(sportEventResultInfo.toString());
                        if(sportEventResultInfo.getError_code() != 0) {
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        List<SportEventInfo> sportEventInfoList = sportEventResultInfo.getData();
                        if (sportEventInfoList == null || sportEventInfoList.size() <= 0) {
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(true, sportEventInfoList);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                    }
                });
    }
}
