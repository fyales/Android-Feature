package com.fyales.android.demo.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.fyales.android.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Producer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;


public class SimpleRxActivity extends AppCompatActivity {

    @BindView(R.id.simple_subscriber_btn)
    Button simpleSubscriberBtn;
    @BindView(R.id.terminal_tv)
    TextView terminalTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_rx);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.simple_subscriber_btn)
    void simpleSubscriberBtnClick() {
        //订阅者
        Subscriber simpleSubsciber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                terminalTv.setText(terminalTv.getText() + "\n"  + "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                terminalTv.setText(terminalTv.getText() + "\n"  + "OnError!");
            }

            @Override
            public void onNext(String o) {
                terminalTv.setText(terminalTv.getText() + "\n"  + o);
            }
        };


        //被观察者
        Observable simpleObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello Rx");
                subscriber.onNext("Hi Rx");
                subscriber.onCompleted();
            }
        });

        //Single
        Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                singleSubscriber.onSuccess("hello");
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i("rx","Single:The value is " + s);
            }
        });

        //订阅者订阅
        simpleObservable.subscribe(simpleSubsciber);

        //构造被观察者的一些其他方法
//        Observable.just("one", "two", "three", "four").subscribe(simpleSubsciber);
//        Observable.from(new String[]{"China", "America", "Japan"}).subscribe(simpleSubsciber);

        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                terminalTv.setText(terminalTv.getText() + "\n"  + s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
                terminalTv.setText(terminalTv.getText() + "\n"  + "onError!");
            }
        };

        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                terminalTv.setText(terminalTv.getText() + "\n"  + "Completed!");
            }
        };

        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        simpleObservable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        simpleObservable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        simpleObservable.subscribe(onNextAction, onErrorAction, onCompletedAction);

    }
}
