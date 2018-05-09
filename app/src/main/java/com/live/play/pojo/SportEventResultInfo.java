package com.live.play.pojo;

import java.util.List;

/**
 * Created by patrick on 2018/5/8.
 * create time : 11:28 AM
 */

public class SportEventResultInfo {

    private int error_code;
    private String error_msg;
    private int match_total;
    private List<SportEventInfo> data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public int getMatch_total() {
        return match_total;
    }

    public void setMatch_total(int match_total) {
        this.match_total = match_total;
    }

    public List<SportEventInfo> getData() {
        return data;
    }

    public void setData(List<SportEventInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SportEventResultInfo{" +
                "error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                ", match_total=" + match_total +
                ", data=" + data +
                '}';
    }
}
