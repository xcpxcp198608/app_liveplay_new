package com.live.play.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.utils.Logger;
import com.live.play.R;
import com.live.play.adapter.ChannelTypeBVisionAdapter;
import com.live.play.adapter.LDFamAdapter;
import com.live.play.databinding.ActivityBvisionBinding;
import com.live.play.instance.Application;
import com.live.play.instance.Constant;
import com.live.play.pojo.ChannelTypeInfo;
import com.live.play.pojo.ImageInfo;
import com.live.play.pojo.LDFamInfo;
import com.live.play.presenter.BVisionPresenter;
import com.live.play.task.TokenTask;
import com.live.play.view.custom_view.LDFamListView;

import java.util.List;

public class BVisionActivity extends BaseActivity<BVisionPresenter> implements BVision, View.OnClickListener  {

    private ActivityBvisionBinding binding;
    private LDFamAdapter ldFamAdapter;

    @Override
    protected BVisionPresenter createPresenter() {
        return new BVisionPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bvision);
        binding.llLdFam.setOnClickListener(this);
        presenter.loadChannelType(2+"");
        presenter.loadLDFam();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_ld_fam:
                Logger.d("sdf");
                binding.pbLdFam.setVisibility(View.VISIBLE);
                presenter.loadLDFam();
                break;
        }
    }

    @Override
    public void onLoadAdImage(boolean isSuccess, ImageInfo imageInfo) {

    }

    @Override
    public void onLoadChannelType(boolean execute, final List<ChannelTypeInfo> channelTypeInfoList) {
        if(!execute){
            binding.pbLoading.setVisibility(View.GONE);
            binding.tvLoading.setText(getString(R.string.data_load_error));
            binding.btRetry.setVisibility(View.VISIBLE);
            binding.btRetry.requestFocus();
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        ChannelTypeBVisionAdapter channelTypeBVisionAdapter = new ChannelTypeBVisionAdapter(channelTypeInfoList);
        binding.rcvChannelType.setAdapter(channelTypeBVisionAdapter);
        binding.rcvChannelType.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        binding.rcvChannelType.requestFocus();
        channelTypeBVisionAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Application.getExecutorService().execute(new TokenTask());
                ChannelTypeInfo channelTypeInfo = channelTypeInfoList.get(position);
                showChannel(channelTypeInfo);
            }
        });
    }

    private void showChannel(ChannelTypeInfo channelTypeInfo){
        if(channelTypeInfo.getFlag() == 1){
            Intent intent = new Intent(BVisionActivity.this, ChannelTypeActivity1.class);
            intent.putExtra("type", channelTypeInfo.getTag());
            startActivity(intent);
        }else if(channelTypeInfo.getFlag() == 2){
            Intent intent = new Intent(BVisionActivity.this, ChannelTypeActivity2.class);
            intent.putExtra("type", channelTypeInfo.getTag());
            startActivity(intent);
        }else {
            Intent intent = new Intent(BVisionActivity.this, ChannelActivity.class);
            intent.putExtra(Constant.key.channel_type, channelTypeInfo.getTag());
            startActivity(intent);
        }
    }

    @Override
    public void onLoadLDFam(boolean execute, List<LDFamInfo> ldFamInfoList) {
        binding.pbLdFam.setVisibility(View.GONE);
        if(ldFamAdapter == null){
            ldFamAdapter = new LDFamAdapter(this, ldFamInfoList);
        }
        binding.lvLdFam.setAdapter(ldFamAdapter);
        ldFamAdapter.notifyChange(ldFamInfoList);
        binding.lvLdFam.start();
        binding.lvLdFam.setOnScrollFinishedListener(new LDFamListView.OnScrollFinishedListener() {
            @Override
            public void onFinished(boolean isFinished, int position) {
                if(presenter != null){
                    presenter.loadLDFam();
                }
            }
        });
    }
}
