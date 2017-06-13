package com.fyales.android.demo.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.fyales.android.demo.R;
import com.fyales.android.demo.rx.event.SimpleEvent;
import com.fyales.android.demo.rx.tool.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RxBusObservableActivity extends AppCompatActivity {

    @BindView(R.id.send_tv)
    Button sendTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus_observable);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.send_tv)
    void sendTv(){
        RxBus.getInstance().post(new SimpleEvent(SimpleEvent.SUCCESS,"原力与你同在"));
    }
}
