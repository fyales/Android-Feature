package com.fyales.android.demo.rx;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.fyales.android.demo.R;
import com.fyales.android.demo.rx.event.SimpleEvent;
import com.fyales.android.demo.rx.tool.MainBoard;
import com.fyales.android.demo.rx.tool.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RxBusObservableActivity extends AppCompatActivity {

    @BindView(R.id.send_tv)
    Button sendTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus_observable);
        ButterKnife.bind(this);

        MainBoard.getInstance().getObsevable().subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                Log.i("rx","The String is " + o);
            }
        });

        MainBoard.getInstance().notifyDataChanged("hello");



        //背压
        Observable.just(1)
                .onBackpressureBuffer()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {

                    }
                });


        demoThread();


    }

    @OnClick(R.id.send_tv)
    void sendTv(){
        RxBus.getInstance().post(new SimpleEvent(SimpleEvent.SUCCESS,"原力与你同在"));
    }

    private void demoThread(){
        //线程切换
        Observable<String> firstO = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.i("rx","firstO:" + Thread.currentThread());
                subscriber.onNext("one");
                subscriber.onCompleted();
            }
        });

        Observable<String> secondO = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.i("rx","SecondO:" + Thread.currentThread());
                subscriber.onNext("two");
                subscriber.onCompleted();
            }
        });

        Observable.zip(
                firstO.observeOn(Schedulers.computation()),
                secondO.observeOn(Schedulers.computation()),
                new Func2<String, String, String>() {
                    @Override
                    public String call(String s, String s2) {
                        Log.i("rx","Func2:" + Thread.currentThread());
                        return s + s2;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.i("rx","Func1" + Thread.currentThread());
                        return "map:" + s;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("rx","Action1" + Thread.currentThread());
                    }
                });
    }
}
