package com.fyales.android.demo.rx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.fyales.android.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LiftActivity extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lift);
        ButterKnife.bind(this);

        Observable.just(R.drawable.cat)
                .map(new Func1<Integer, Bitmap>() {
                    @Override
                    public Bitmap call(Integer integer) {
                        Log.i("rx","OnSubscriber --> Current Thread is " + Thread.currentThread().getName());
                        return BitmapFactory.decodeResource(getResources(), R.drawable.cat);
                    }
                })
                .subscribeOn(Schedulers.io())       //指定subscriber()放生在io线程
                .observeOn(AndroidSchedulers.mainThread())  //Subscriber的回调发生在主线程
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        Log.i("rx","Subscriber --> Current Thread is " + Thread.currentThread().getName());
                        img.setImageBitmap(bitmap);
                    }
                });
    }
}
