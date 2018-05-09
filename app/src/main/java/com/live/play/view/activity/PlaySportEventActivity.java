package com.live.play.view.activity;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;
import com.live.play.R;
import com.live.play.databinding.ActivityPlayBinding;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * play
 */

public class PlaySportEventActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private ActivityPlayBinding binding;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private boolean send = true;
    private String tag = "";
    private String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play);
        surfaceHolder = binding.surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        url = getIntent().getStringExtra("url");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (url.contains("protv.company")){
            String streamToken = (String) SPUtil.get("streamToken", "123");
            url = url.trim() + "?token=" + streamToken;
        }
        playVideo(url);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseMediaPlayer();
    }

    private void playVideo(final String url) {
        sendNetSpeed();
        binding.pbPlay.setVisibility(View.VISIBLE);
        try {
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            Logger.d(url);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_pause);
                    binding.pbPlay.setVisibility(View.GONE);
                    binding.tvNetSpeed.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                        binding.pbPlay.setVisibility(View.VISIBLE);
                        binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    }
                    if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                        binding.pbPlay.setVisibility(View.GONE);
                        binding.tvNetSpeed.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Logger.d("error");
                    playVideo(url);
                    binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Logger.d("completion");
                    playVideo(url);
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
    }

    private void releaseMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        send = false;
        releaseMediaPlayer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(binding.llController.getVisibility() == View.VISIBLE){
                binding.llController.setVisibility(View.GONE);
                binding.rcvChannel.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendNetSpeed(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (send){
                    int s1 = NetUtil.getNetSpeedBytes();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int s2 = NetUtil.getNetSpeedBytes();
                    float f  = (s2-s1)/2/1024F;
                    DecimalFormat decimalFormat = new DecimalFormat("##0.00");
                    String s = decimalFormat.format(f);
                    Message m = handler.obtainMessage();
                    m.what = 1;
                    m.obj = s;
                    handler.sendMessage(m);
                }
            }
        }).start();
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String s = msg.obj.toString();
                    binding.tvNetSpeed.setText(s + "kbs");
                    break;
            }
        }
    };

}
