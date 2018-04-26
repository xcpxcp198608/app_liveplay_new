package com.live.play.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.px.common.http.HttpMaster;
import com.px.common.http.listener.DownloadListener;
import com.px.common.http.pojo.DownloadInfo;
import com.px.common.image.ImageMaster;
import com.px.common.utils.AppUtil;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.FileUtil;
import com.live.play.R;
import com.live.play.databinding.ActivityUpgradeBinding;
import com.live.play.instance.Application;
import com.live.play.pojo.ImageInfo;
import com.live.play.pojo.UpgradeInfo;
import com.live.play.presenter.SplashPresenter;

/**
 * upgrade app
 */

public class UpgradeActivity extends BaseActivity<SplashPresenter> implements Splash {

    private ActivityUpgradeBinding binding;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upgrade);
        presenter.loadAdImage();
        UpgradeInfo upgradeInfo = getIntent().getParcelableExtra("upgradeInfo");
        if(upgradeInfo == null){
            return;
        }
        FileUtil.delete(Application.PATH_DOWNLOAD, getPackageName());
        FileUtil.delete(Application.PATH_DOWNLOAD, getPackageName()+".apk");
        startUpgrade(upgradeInfo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isSubscribe = false;
    }

    private void startUpgrade(UpgradeInfo upgradeInfo) {
        HttpMaster.download(UpgradeActivity.this)
                .url(upgradeInfo.getUrl())
                .name(upgradeInfo.getPackageName())
                .path(Application.PATH_DOWNLOAD)
                .startDownload(new DownloadListener() {
                    @Override
                    public void onPending(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onStart(DownloadInfo downloadInfo) {
                        binding.llProgress.setVisibility(View.VISIBLE);
                        binding.tvProgress.setText("0%");
                        binding.pbUpgrade.setProgress(0);
                    }

                    @Override
                    public void onPause(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {
                        binding.tvProgress.setText(downloadInfo.getProgress()+"%");
                        binding.pbUpgrade.setProgress(downloadInfo.getProgress());
                    }

                    @Override
                    public void onFinished(DownloadInfo downloadInfo) {
                        binding.tvProgress.setText("100%");
                        binding.pbUpgrade.setProgress(100);
                        binding.llProgress.setVisibility(View.GONE);
                        if(AppUtil.isApkCanInstall(downloadInfo.getPath(), downloadInfo.getName())){
                            AppUtil.installApk(downloadInfo.getPath(), downloadInfo.getName(), "com.live.play.fileprovider");
                        }else{
                            EmojiToast.show("download error", EmojiToast.EMOJI_SAD);
                        }
                    }

                    @Override
                    public void onCancel(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onError(DownloadInfo downloadInfo) {

                    }
                });
    }

    @Override
    public void onLoadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivUpgrade, R.drawable.img_hold,
                    R.drawable.img_hold);
        }
    }

    //ignore this
    @Override
    public void checkUpgrade(boolean upgrade, UpgradeInfo upgradeInfo) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
