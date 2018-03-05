package com.live.play.view.activity;

import com.live.play.pojo.LDFamInfo;

import java.util.List;

/**
 * Created by patrick on 16/11/2017.
 * create time : 11:00 AM
 */

public interface BVision extends ChannelType {

    void onLoadLDFam(boolean execute, List<LDFamInfo> ldFamInfoList);
}
