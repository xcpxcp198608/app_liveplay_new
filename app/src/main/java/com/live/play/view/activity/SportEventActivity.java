package com.live.play.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.live.play.R;
import com.live.play.adapter.SportEventAdapter;
import com.live.play.databinding.ActivitySportEventBinding;
import com.live.play.instance.Constant;
import com.live.play.pojo.ImageInfo;
import com.live.play.pojo.SportEventInfo;
import com.live.play.presenter.SportEventPresenter;

import java.util.List;

public class SportEventActivity extends BaseActivity<SportEventPresenter> implements SportEvent {

    private ActivitySportEventBinding binding;
    private SportEventAdapter adapter;

    @Override
    protected SportEventPresenter createPresenter() {
        return new SportEventPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sport_event);
        String key = getIntent().getStringExtra("key");
        presenter.load(key);
    }

    @Override
    public void loadSportEvent(boolean upgrade, List<SportEventInfo> sportEventInfoList) {
        if(!upgrade){
            binding.pbLoading.setVisibility(View.GONE);
            binding.tvLoading.setText(getString(R.string.sprot_event_data_load_error));
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        if(adapter == null){
            adapter = new SportEventAdapter(SportEventActivity.this, sportEventInfoList);
            binding.rcvSportEvent.setAdapter(adapter);
        }
        binding.rcvSportEvent.setLayoutManager(new LinearLayoutManager(this));
    }
}
