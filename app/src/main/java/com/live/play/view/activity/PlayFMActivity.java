package com.live.play.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.image.ImageMaster;
import com.px.common.utils.AppUtil;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;
import com.live.play.R;
import com.live.play.databinding.ActivityFmPlayBinding;
import com.live.play.entity.ResultInfo;
import com.live.play.instance.Application;
import com.live.play.instance.Constant;
import com.live.play.manager.PlayManager;
import com.live.play.pojo.ChannelInfo;
import com.live.play.sql.FavoriteChannelDao;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class PlayFMActivity extends AppCompatActivity implements PlayManager.PlayListener,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private ActivityFmPlayBinding binding;
    private PlayManager playManager;
    private MediaPlayer mediaPlayer;
    private FavoriteChannelDao favoriteChannelDao;
    private String errorMessage = "";
    private Disposable disposable;
    private boolean send = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fm_play);
        List<ChannelInfo> channelInfoList = Application.getChannelInfoList();
        int position = getIntent().getIntExtra("position", 0);
        playManager = new PlayManager(channelInfoList, position);
        playManager.setPlayListener(this);
        favoriteChannelDao = FavoriteChannelDao.getInstance();
        binding.flPlay.setOnClickListener(this);
        binding.cbFavorite.setOnCheckedChangeListener(this);
        binding.ibtReport.setOnClickListener(this);
        showFavoriteStatus();
    }

    private void showFavoriteStatus(){
        if(favoriteChannelDao.exists(playManager.getChannelInfo())){
            binding.cbFavorite.setChecked(true);
        }else{
            binding.cbFavorite.setChecked(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        playManager.dispatchChannel();
    }

    @Override
    public void play(String url) {
        showImage();
        playFM(url);
    }

    @Override
    public void playAd() {
        startActivity(new Intent(PlayFMActivity.this, AdScreenActivity.class));
        finish();
    }

    @Override
    public void launchApp(String packageName) {
        if(AppUtil.isInstalled(packageName)) {
            AppUtil.launchApp(PlayFMActivity.this, packageName);
        }else{
            EmojiToast.show(getString(R.string.notice1), EmojiToast.EMOJI_SAD);
            AppUtil.launchApp(PlayFMActivity.this, Constant.packageName.market);
        }
        finish();
    }

    private void showImage(){
        String icon = playManager.getChannelInfo().getIcon();
        ImageMaster.load(PlayFMActivity.this, icon, binding.ivFM, R.drawable.img_hold3, R.drawable.img_hold3);
    }

    private void playFM(final String url) {
        sendNetSpeed();
        if(disposable != null){
            disposable.dispose();
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        try {
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.vsvFM.setVisibility(View.VISIBLE);
                    voiceViewStart();
                    EmojiToast.show(playManager.getChannelInfo().getName()+" playing" , EmojiToast.EMOJI_SMILE);
                    binding.tvNetSpeed.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    Logger.d("onInfo:" + what + "/" + extra);
                    if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    }
                    if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                        binding.tvNetSpeed.setVisibility(View.GONE);
                        binding.vsvFM.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Logger.d("onError:" + what + "/" + extra);
                    binding.vsvFM.setVisibility(View.GONE);
                    binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    playFM(url);
                    return true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    binding.vsvFM.setVisibility(View.GONE);
                    binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    Logger.d("onCompletions");
                    playFM(url);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void voiceViewStart(){
        disposable = Observable.interval(0,200 , TimeUnit.MILLISECONDS)
                .repeat()
                .map(new Function<Long, Object>() {
                    @Override
                    public Object apply(Long aLong) {
                        binding.vsvFM.start();
                        return "";
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {

                    }
                });
    }

    private void releaseMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(disposable != null){
            disposable.dispose();
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
        releaseMediaPlayer();
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
        String userName = (String) SPUtil.get("userName", "test");
        HttpMaster.post(Constant.url.channel_send_error_report)
                .param("userName",userName)
                .param("channelName",playManager.getChannelInfo().getName())
                .param("message", message)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        ResultInfo resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo>(){}.getType());
                        if(resultInfo == null){
                            return;
                        }
                        if(resultInfo.getCode() == ResultInfo.CODE_OK) {
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
                }else{
                    binding.llController.setVisibility(View.VISIBLE);
                    binding.cbFavorite.requestFocus();
                }
                break;
            case R.id.ibtReport:
                showErrorReportDialog();
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
                return true;
            }
        }
        if((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT &&
                binding.llController.getVisibility() == View.GONE) ||
                event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            binding.vsvFM.setVisibility(View.GONE);
            playManager.previousChannel();
        }
        if((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT &&
                binding.llController.getVisibility() == View.GONE) ||
                event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT){
            binding.vsvFM.setVisibility(View.GONE);
            playManager.nextChannel();
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
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int s2 = NetUtil.getNetSpeedBytes();
                    float f  = (s2-s1)/2/1024F;
                    DecimalFormat decimalFormat = new DecimalFormat("##0.00");
                    String s = decimalFormat.format(f);
                    Message m = handler.obtainMessage();
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
            String s = msg.obj.toString();
            Logger.d(s);
            binding.tvNetSpeed.setText(s+"kbs");
        }
    };
}
