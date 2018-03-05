package com.live.play.task;

import android.text.TextUtils;

import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.AESUtil;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by patrick on 14/09/2017.
 * create time : 5:26 PM
 */

public class TokenTask extends TimerTask {

    private static final String URL = "http://apius.protv.company/v1/get_token.do?";
    private static final String PRE="BTVi35C41E7";
    private static final String PWD="Ho2oMcqUZMMvFzqb";

    @Override
    public void run() {
        do{
            loadToken();
        }while (!NetUtil.isConnected());
    }

    private void loadToken(){
        long time = System.currentTimeMillis();
        time = time / 1000;
        String t = AESUtil.MD5(PRE+PWD+time);
        String url = URL + "reg_date="+time+"&token="+t;
        HttpMaster.get(url)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        try {
                            Logger.d(s);
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject data = jsonObject.getJSONObject("data");
                            String streamToken = data.getString("token");
//                            Logger.d(streamToken);
                            if(!TextUtils.isEmpty(streamToken)) {
                                SPUtil.put("streamToken", streamToken);
                            }
                        } catch (JSONException e) {
                            Logger.d("token json format error");
                            Logger.d(e.getMessage());
                            loadToken();
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
