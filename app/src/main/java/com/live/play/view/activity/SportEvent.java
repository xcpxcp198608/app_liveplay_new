package com.live.play.view.activity;

import com.live.play.pojo.SportEventInfo;
import com.live.play.pojo.SportEventScoresInfo;

import java.util.List;

/**
 * splash activity interface
 */

public interface SportEvent {
    
    void loadSportEvent (boolean upgrade, List<SportEventInfo> sportEventInfoList);
    void loadSportEventScores (boolean upgrade, List<SportEventScoresInfo> sportEventScoresInfoList);
}
