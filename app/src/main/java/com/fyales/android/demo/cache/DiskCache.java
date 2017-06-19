package com.fyales.android.demo.cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.fyales.android.demo.utils.FileUtils;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author fyales
 * @since 2017/6/15
 */

public class DiskCache<T> implements ICache<T> {

    private String mCachePath;

    public DiskCache(Context context) {
        mCachePath = FileUtils.getCacheDir(context);
    }

    @Override
    public Observable<T> get(final String key, final Class<T> cls) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {

                Log.v("cache", "load from disk: " + key);
                String filename = mCachePath + key;
                String result = FileUtils.readTextFromSDcard(filename);

                if (subscriber.isUnsubscribed()) {
                    return;
                }
                if (TextUtils.isEmpty(result)) {
                    subscriber.onNext(null);
                } else{
                    T t = new Gson().fromJson(result, cls);
                    subscriber.onNext(t);
                }

                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void put(final String key, final T t) {
        Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Log.v("cache", "Save to disk: " + key);

                String fileName = mCachePath + key;
                String result = t.toString();
                FileUtils.saveText2Sdcard(fileName, result);

                if (subscriber.isUnsubscribed()) return;
                subscriber.onNext(t);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
