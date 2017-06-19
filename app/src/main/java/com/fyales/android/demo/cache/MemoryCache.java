package com.fyales.android.demo.cache;

import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import rx.Observable;
import rx.Subscriber;

/**
 * @author fyales
 * @since 2017/6/15
 */

public class MemoryCache<T> implements ICache<T>{

    private LruCache<String,String> mCache;

    public MemoryCache(){
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        mCache = new LruCache<String,String>(cacheSize){
            @Override
            protected int sizeOf(String key, String value) {
                try{
                    return value.getBytes("UTF-8").length;
                }catch (UnsupportedEncodingException e){
                    return value.getBytes().length;
                }
            }
        };

    }


    @Override
    public Observable get(final String key, final Class<T> cls) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Log.v("cache","Load from memory: " + key);
                String result = mCache.get(key);

                if (subscriber.isUnsubscribed()){
                    return;
                }

                if (TextUtils.isEmpty(result)){
                    subscriber.onNext(null);
                } else{
                    T t = new Gson().fromJson(result,cls);
                    subscriber.onNext(t);
                }
                subscriber.onCompleted();

            }
        });
    }

    @Override
    public void put(String key, T t) {
        if (null != t){
            Log.v("cache","The save key is " + key);
            mCache.put(key,t.toString());
        }
    }
}
