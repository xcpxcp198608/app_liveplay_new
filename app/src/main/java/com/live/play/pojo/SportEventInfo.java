package com.live.play.pojo;

import java.util.List;

/**
 * Created by patrick on 2018/5/8.
 * create time : 11:28 AM
 */

public class SportEventInfo {

    private String match_time;
    private String match_guest;
    private String match_master;
    private List<TvData> tv_data;

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

    public String getMatch_master() {
        return match_master;
    }

    public void setMatch_master(String match_master) {
        this.match_master = match_master;
    }

    public List<TvData> getTv_data() {
        return tv_data;
    }

    public void setTv_data(List<TvData> tv_data) {
        this.tv_data = tv_data;
    }


    @Override
    public String toString() {
        return "SportEventInfo{" +
                "match_time='" + match_time + '\'' +
                ", match_guest='" + match_guest + '\'' +
                ", match_master='" + match_master + '\'' +
                ", tv_data=" + tv_data +
                '}';
    }

    public class TvData{
        private String match_tv_name;
        private String match_tv_icon;
        private String match_tv_url;

        public String getMatch_tv_name() {
            return match_tv_name;
        }

        public void setMatch_tv_name(String match_tv_name) {
            this.match_tv_name = match_tv_name;
        }

        public String getMatch_tv_icon() {
            return match_tv_icon;
        }

        public void setMatch_tv_icon(String match_tv_icon) {
            this.match_tv_icon = match_tv_icon;
        }

        public String getMatch_tv_url() {
            return match_tv_url;
        }

        public void setMatch_tv_url(String match_tv_url) {
            this.match_tv_url = match_tv_url;
        }

        @Override
        public String toString() {
            return "TvData{" +
                    "match_tv_name='" + match_tv_name + '\'' +
                    ", match_tv_icon='" + match_tv_icon + '\'' +
                    ", match_tv_url='" + match_tv_url + '\'' +
                    '}';
        }
    }


}
