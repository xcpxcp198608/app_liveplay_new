package com.live.play.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.http.pojo.ResultInfo;
import com.px.common.utils.AESUtil;
import com.px.common.utils.AppUtil;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;
import com.px.common.utils.TimeUtil;
import com.live.play.R;
import com.live.play.adapter.PlayChannelAdapter;
import com.live.play.databinding.ActivityPlayBinding;
import com.live.play.instance.Application;
import com.live.play.instance.Constant;
import com.live.play.manager.PlayManager;
import com.live.play.model.UserContentResolver;
import com.live.play.pojo.ChannelInfo;
import com.live.play.sql.FavoriteChannelDao;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * play
 */

public class PlayActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        PlayManager.PlayListener,View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private ActivityPlayBinding binding;
    private SurfaceHolder surfaceHolder;
    private PlayManager playManager;
    private MediaPlayer mediaPlayer;
    private FavoriteChannelDao favoriteChannelDao;
    private String errorMessage = "";
    private boolean send = true;
    private int currentPlayPosition = 0;
    private String tag = "";
    private boolean viewChannel = false;
    private int targetPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play);
        surfaceHolder = binding.surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        List<ChannelInfo> channelInfoList = Application.getChannelInfoList();
        int position = getIntent().getIntExtra("position", 0);
        playManager = new PlayManager(channelInfoList, position);
        playManager.setPlayListener(this);
        favoriteChannelDao = FavoriteChannelDao.getInstance();
        binding.flPlay.setOnClickListener(this);
        binding.ibtStartStop.setOnClickListener(this);
        binding.ibtFastRewind.setOnClickListener(this);
        binding.ibtFastForward.setOnClickListener(this);
        binding.ibtReport.setOnClickListener(this);
        binding.cbFavorite.setOnCheckedChangeListener(this);
        binding.btGo.setOnClickListener(this);
        binding.btClose.setOnClickListener(this);
        showFavoriteStatus();
        showChannelList(channelInfoList);
    }

    private void showFavoriteStatus(){
        if(favoriteChannelDao.exists(playManager.getChannelInfo())){
            binding.cbFavorite.setChecked(true);
        }else{
            binding.cbFavorite.setChecked(false);
        }
    }

    private void showChannelList(final List<ChannelInfo> channelInfoList){
        if(channelInfoList.get(0).getCountry().equals("LIVETV")) {
            return;
        }
        viewChannel = true;
        PlayChannelAdapter playChannelAdapter = new PlayChannelAdapter(channelInfoList);
        binding.rcvChannel.setAdapter(playChannelAdapter);
        binding.rcvChannel.setLayoutManager(new LinearLayoutManager(PlayActivity.this));
        playChannelAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(tag != null){
                    playManager.stopView(tag);
                }
                playManager.setChannelInfo(channelInfoList.get(position));
                playManager.dispatchChannel();
            }
        });
        playChannelAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    view.setSelected(true);
                }else{
                    view.setSelected(false);
                }
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        playManager.dispatchChannel();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseMediaPlayer();
    }

    @Override
    public void play(final String url) {
        showFavoriteStatus();
        playVideo(PlayManager.parseUrl(url));
        tag = AESUtil.MD5(System.currentTimeMillis() + playManager.getChannelInfo().getName());
        playManager.startView(tag);
    }

    @Override
    public void playAd() {
        startActivity(new Intent(PlayActivity.this, AdScreenActivity.class));
        finish();
    }

    @Override
    public void launchApp(String packageName) {
        if(AppUtil.isInstalled(packageName)) {
            AppUtil.launchApp(PlayActivity.this, packageName);
        }else{
            EmojiToast.show(getString(R.string.notice1), EmojiToast.EMOJI_SAD);
            AppUtil.launchApp(PlayActivity.this, Constant.packageName.market);
        }
        finish();
    }

    private void playVideo(final List<String> urlList) {
        sendNetSpeed();
        binding.pbPlay.setVisibility(View.VISIBLE);
        try {
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            String url = urlList.get(currentPlayPosition);
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
                    EmojiToast.show(playManager.getChannelInfo().getName() + " playing" , EmojiToast.EMOJI_SMILE);
                    binding.tvNetSpeed.setVisibility(View.GONE);
                    mediaPlayer.start();
                    setDuration();
                    showLastPosition();
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
                    playOtherUrlOnVideo(urlList);
                    binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Logger.d("completion");
                    currentPlayPosition = 0;
                    playOtherUrlOnVideo(urlList);
//                    playManager.nextChannel();
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    setCurrentPosition();
                    mediaPlayer.start();
                }
            });
        } catch (Exception e) {
            Logger.d(e.getMessage());
        }
    }

    private void playOtherUrlOnVideo(List<String> urlList){
        currentPlayPosition ++;
        if(currentPlayPosition >= urlList.size()){
            currentPlayPosition =0 ;
        }
        playVideo(urlList);
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
        playManager.stopView(tag);
        Application.cleanChannelInfoList();
    }

    private void showErrorReportDialog(){
        errorMessage = getString(R.string.error_msg1);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        if(window == null) return;
        dialog.setContentView(R.layout.dialog_error_report);
        RadioGroup radioGroup = (RadioGroup) window.findViewById(R.id.radioGroup);
        radioGroup.check(R.id.rbMessage1);
        Button button = (Button) window.findViewById(R.id.btSend);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rbMessage1:
                        errorMessage = getString(R.string.error_msg1);
                        break;
                    case R.id.rbMessage2:
                        errorMessage = getString(R.string.error_msg2);
                        break;
                    case R.id.rbMessage3:
                        errorMessage = getString(R.string.error_msg3);
                    case R.id.rbMessage4:
                        errorMessage = getString(R.string.error_msg4);
                        break;
                    default:
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendErrorReport(errorMessage);
                dialog.dismiss();
            }
        });
    }

    private void sendErrorReport(String message) {
        String userName = UserContentResolver.get("userName");
        if(TextUtils.isEmpty(userName)) userName = "test";
        String country = playManager.getChannelInfo().getCountry();
        if(country.contains("&")){
            country = country.replaceAll("&", " ");
        }
        String name = playManager.getChannelInfo().getName();
        if(name.contains("&")){
            name = name.replaceAll("&", "");
        }
        HttpMaster.post(Constant.url.channel_send_error_report)
                .param("userName",userName)
                .param("channelName",country + "-" + name)
                .param("message", message)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        ResultInfo resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo>(){}.getType());
                        if(resultInfo == null) return;
                        if(resultInfo.getCode() == 200) {
                            EmojiToast.show(resultInfo.getMessage(), EmojiToast.EMOJI_SMILE);
                        }else{
                            EmojiToast.show(resultInfo.getMessage(), EmojiToast.EMOJI_SAD);
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cbFavorite:
                ChannelInfo channelInfo = playManager.getChannelInfo();
                if(isChecked){
                    if(favoriteChannelDao.insertOrUpdate(channelInfo)){
                        binding.cbFavorite.setChecked(true);
                    }
                }else{
                    favoriteChannelDao.delete(channelInfo);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.flPlay:
                if(binding.llController.getVisibility() == View.VISIBLE){
                    binding.llController.setVisibility(View.GONE);
                    binding.rcvChannel.setVisibility(View.GONE);
                }else{
                    if(mediaPlayer == null){
                        return;
                    }
                    if(mediaPlayer.isPlaying()){
                        binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_pause);
                    }else{
                        binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_play);
                    }
                    binding.llController.setVisibility(View.VISIBLE);
                    if(viewChannel) {
                        binding.rcvChannel.setVisibility(View.VISIBLE);
                    }
                    binding.ibtStartStop.requestFocus();
                }
                break;
            case R.id.ibtReport:
                showErrorReportDialog();
                break;
            case R.id.ibtStartStop:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_play);
                }else{
                    mediaPlayer.start();
                    binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_pause);
                }
                break;
            case R.id.ibtFastRewind:
                if(mediaPlayer.getDuration() <= 0 ) return;
                int t1 = mediaPlayer.getCurrentPosition() - 5000;
                if(t1 < 0 ) t1 = 0;
                mediaPlayer.seekTo(t1);
                break;
            case R.id.ibtFastForward:
                if(mediaPlayer.getDuration() <= 0 ) return;
                int t2 = mediaPlayer.getCurrentPosition() + 5000;
                if(t2 > mediaPlayer.getDuration()) t2 = mediaPlayer.getDuration();
                mediaPlayer.seekTo(t2);
                break;
            case R.id.btGo:
                if(targetPosition > 0){
                    mediaPlayer.seekTo(targetPosition);
                    binding.llViewPosition.setVisibility(View.GONE);
                }
                break;
            case R.id.btClose:
                binding.llViewPosition.setVisibility(View.GONE);
                break;
            default:
                break;
        }
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
        if((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP &&
                binding.llController.getVisibility() == View.GONE) ||
                event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            currentPlayPosition = 0;
            playManager.previousChannel();
        }
        if((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN &&
                binding.llController.getVisibility() == View.GONE) ||
                event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT){
            currentPlayPosition = 0;
            playManager.nextChannel();
        }
        if(event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_play);
            }else{
                mediaPlayer.start();
                binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_pause);
            }
        }
        if(event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_STOP){
            releaseMediaPlayer();
            playManager.stopView(tag);
            this.finish();
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
                    if(playManager.getChannelInfo().isLocked() && playManager.getLevel() <= 2 &&
                            !playManager.isExperience()) {
                        long lastExperienceTime = (long) SPUtil.get("lastExperienceTime", 0L);
                        if (lastExperienceTime + 60000 < System.currentTimeMillis()) {
                            boolean isLastExperience = (boolean) SPUtil.get("isLastExperience", true);
                            SPUtil.put("isLastExperience", !isLastExperience);
                            SPUtil.put("lastExperienceTime", System.currentTimeMillis());
                            EmojiToast.showLong(getString(R.string.notice3), EmojiToast.EMOJI_SAD);
                            finish();
                        }
                    }
                    setCurrentPosition();
                    break;
                case 2:
                    binding.llViewPosition.setVisibility(View.GONE);
            }
        }
    };

    private void setCurrentPosition(){
        if(mediaPlayer == null || !mediaPlayer.isPlaying()) return;
        int currentPosition = mediaPlayer.getCurrentPosition();
        if(currentPosition > 0){
            SPUtil.put(playManager.getChannelInfo().getTag() + "position", currentPosition);
            String sPosition = TimeUtil.getMediaTime(currentPosition);
            binding.tvCurrentPosition.setText(sPosition);
        }
    }

    private void setDuration(){
        if(mediaPlayer == null || !mediaPlayer.isPlaying()) return;
        int duration = mediaPlayer.getDuration();
        if(duration > 0){
            binding.tvDuration.setText(TimeUtil.getMediaTime(duration));
        }
    }

    private void showLastPosition(){
        int lastPosition = (int) SPUtil.get(playManager.getChannelInfo().getTag() + "position", 0);
        targetPosition = lastPosition - 3000;
        int duration = mediaPlayer.getDuration();
        if(duration > 0 && targetPosition > 0){
            String p = TimeUtil.getMediaTime(targetPosition);
            binding.tvLastPosition.setText(p);
            binding.llViewPosition.setVisibility(View.VISIBLE);
            binding.btGo.requestFocus();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(2);
                }
            }, 8000);
        }
    }
}
