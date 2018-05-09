package com.live.play.presenter;

import com.live.play.model.ChannelType2Provider;
import com.live.play.model.LoadServiceWithParam;
import com.live.play.model.SportEventProvider;
import com.live.play.pojo.ChannelType2Info;
import com.live.play.pojo.SportEventInfo;
import com.live.play.view.activity.ChannelType2;
import com.live.play.view.activity.SportEvent;

import java.util.List;

/**
 * Created by patrick on 14/08/2017.
 * create time : 2:10 PM
 */

public class SportEventPresenter extends BasePresenter<ChannelType2> {

    private SportEvent sportEvent;
    private SportEventProvider sportEventProvider;

    public SportEventPresenter(SportEvent sportEvent) {
        this.sportEvent = sportEvent;
        sportEventProvider = new SportEventProvider();
    }

    public void load(String param){
        sportEventProvider.load(param , new LoadServiceWithParam.OnLoadListener<List<SportEventInfo>>() {
            @Override
            public void onLoad(boolean execute, List<SportEventInfo> sportEventInfoList) {
                sportEvent.loadSportEvent(execute, sportEventInfoList);
            }
        });
    }

}
