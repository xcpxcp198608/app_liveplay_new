package com.live.play.view.activity;

import com.live.play.R;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.live.play.adapter.SportEventAdapter;
import com.live.play.databinding.ActivitySportEventBinding;
import com.live.play.pojo.SportEventInfo;
import com.live.play.pojo.SportEventScoresInfo;
import com.live.play.presenter.SportEventPresenter;
import com.live.play.view.custom_view.SportScoresMarqueeFactory;
import com.px.common.utils.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SportEventActivity extends BaseActivity<SportEventPresenter> implements SportEvent {

    private ActivitySportEventBinding binding;
    private SportEventAdapter adapter;
    private Disposable disposable;
    private String key = "";
    private int flag = 0;
    private int currentPosition = 0;

    @Override
    protected SportEventPresenter createPresenter() {
        return new SportEventPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sport_event);
        key = getIntent().getStringExtra("key");
        flag = getIntent().getIntExtra("flag", 3);
        presenter.load(key);
        presenter.loadScores();
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
            adapter = new SportEventAdapter(SportEventActivity.this, sportEventInfoList, flag);
            binding.rcvSportEvent.setAdapter(adapter);
        }
        binding.rcvSportEvent.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void loadSportEventScores(boolean upgrade, final List<SportEventScoresInfo> sportEventScoresInfoList) {
        if (upgrade && sportEventScoresInfoList != null && sportEventScoresInfoList.size() > 0){
//            refreshScores(sportEventScoresInfoList.get(0));
//            disposable = Observable.interval(2000, TimeUnit.MILLISECONDS)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Object>() {
//                        @Override
//                        public void accept(Object o) throws Exception {
//                            if (currentPosition >= sportEventScoresInfoList.size()) {
//                                currentPosition = 0;
//                            }
//                            SportEventScoresInfo scoresInfo = sportEventScoresInfoList.get(currentPosition);
//                            refreshScores(scoresInfo);
//                            currentPosition++;
//                        }
//                    });
            SportScoresMarqueeFactory scoresMarqueeFactory = new SportScoresMarqueeFactory(this);
            scoresMarqueeFactory.setData(sportEventScoresInfoList);
            binding.marqueeView.setMarqueeFactory(scoresMarqueeFactory);
            binding.marqueeView.startFlipping();
            binding.marqueeView.setVisibility(View.VISIBLE);
        }else{
            binding.marqueeView.setVisibility(View.GONE);
        }
    }

    private void refreshScores(SportEventScoresInfo scoresInfo){
//        binding.stvScores.setLeftString(scoresInfo.getMatch_type());
//        binding.stvScores.setLeftBottomString(scoresInfo.getMatch_time());
//        binding.stvScores.setCenterTopString(scoresInfo.getMatch_guest());
//        binding.stvScores.setCenterString(scoresInfo.getMatch_guest_score());
//        binding.stvScores.setRightTopString(scoresInfo.getMatch_master());
//        binding.stvScores.setRightString(scoresInfo.getMatch_master_score());
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.marqueeView.stopFlipping();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }
    }
}
