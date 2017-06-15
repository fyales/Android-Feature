package com.fyales.android.demo.rx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.fyales.android.demo.R;
import com.fyales.android.demo.rx.event.SimpleEvent;
import com.fyales.android.demo.rx.tool.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class RxBusObserverActivity extends AppCompatActivity {

    @BindView(R.id.observer_tv)
    TextView observerTv;
    @BindView(R.id.next_btn)
    Button nextBtn;

    Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus_observer);
        ButterKnife.bind(this);

        mSubscription = RxBus.getInstance().tObservable(SimpleEvent.class).subscribe(new Action1<SimpleEvent>() {
            @Override
            public void call(SimpleEvent simpleEvent) {
                if (simpleEvent.getMsgCode().equals(SimpleEvent.SUCCESS)) {
                    observerTv.setText(simpleEvent.getMsg());
                }
            }
        });
    }

    @OnClick(R.id.next_btn)
    void nextBtnClick(){
        startActivity(new Intent(this,RxBusObservableActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件订阅
        if (!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }
}
