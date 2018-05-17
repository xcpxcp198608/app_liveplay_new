package com.live.play.model;

import com.live.play.pojo.SportEventScoresInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by patrick on 2018/5/17.
 * create time : 2:51 PM
 */
@RunWith(JUnit4.class)
public class SportEventScoresProviderTest {

    @Test
    public void load() throws Exception {
        SportEventScoresProvider sportEventScoresProvider = new SportEventScoresProvider();
        sportEventScoresProvider.load(new LoadService.OnLoadListener<List<SportEventScoresInfo>>() {
            @Override
            public void onLoad(boolean execute, List<SportEventScoresInfo> sportEventScoresInfos) {
                System.out.println(sportEventScoresInfos);
            }
        });
    }

}