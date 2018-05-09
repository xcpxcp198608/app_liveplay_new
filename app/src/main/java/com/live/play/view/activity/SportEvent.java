package com.live.play.view.activity;

import com.live.play.pojo.SportEventInfo;

import java.util.List;

/**
 * splash activity interface
 */

public interface SportEvent {
    
    void loadSportEvent (boolean upgrade, List<SportEventInfo> sportEventInfoList);
}
