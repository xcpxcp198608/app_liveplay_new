package com.live.play.pojo;

import java.util.List;

/**
 * Created by patrick on 2018/5/8.
 * create time : 11:28 AM
 */

public class SportEventScoresInfo {

    private String match_type;
    private String match_time;
    private String match_guest;
    private String match_guest_score;
    private String match_master;
    private String match_master_score;

    public String getMatch_type() {
        return match_type;
    }

    public void setMatch_type(String match_type) {
        this.match_type = match_type;
    }

    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public String getMatch_guest() {
        return match_guest;
    }

    public void setMatch_guest(String match_guest) {
        this.match_guest = match_guest;
    }

    public String getMatch_guest_score() {
        return match_guest_score;
    }

    public void setMatch_guest_score(String match_guest_score) {
        this.match_guest_score = match_guest_score;
    }

    public String getMatch_master() {
        return match_master;
    }

    public void setMatch_master(String match_master) {
        this.match_master = match_master;
    }

    public String getMatch_master_score() {
        return match_master_score;
    }

    public void setMatch_master_score(String match_master_score) {
        this.match_master_score = match_master_score;
    }

    @Override
    public String toString() {
        return "SportEventScoresInfo{" +
                "match_type='" + match_type + '\'' +
                ", match_time='" + match_time + '\'' +
                ", match_guest='" + match_guest + '\'' +
                ", match_guest_score='" + match_guest_score + '\'' +
                ", match_master='" + match_master + '\'' +
                ", match_master_score='" + match_master_score + '\'' +
                '}';
    }
}
