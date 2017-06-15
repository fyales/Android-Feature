package com.fyales.android.demo.cache;

import android.content.Context;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author fyales
 * @since 2017/6/15
 */

public class CacheManager<T> {

    private ICache<T> mMemoryCache;
    private ICache<T> mDiskCache;
    private static CacheManager mInstance;

    private CacheManager(Context context){
        mMemoryCache = new MemoryCache<>();
        mDiskCache = new DiskCache<>(context);
    }

    public static CacheManager getInstance(Context context){
        if (mInstance == null){
            synchronized (CacheManager.class){
                mInstance = new CacheManager(context);
            }
        }
        return mInstance;
    }

    /**
     * 加载相关数据
     * @param key   key值
     * @param cls   class类
     * @param networkCache  网络获取类
     * @return  Observable
     */
    public Observable<T> load(String key,Class<T> cls,NetworkCache<T> networkCache){
        return Observable.concat(
                loadFromMemory(key,cls),
                loadFromDisk(key,cls),
                loadFromNetwork(key,cls,networkCache))
                .first(new Func1<T, Boolean>() {
                    @Override
                    public Boolean call(T t) {
                        //可以加入缓存是否过期的判断
                        return t != null;
                    }
                });
    }

    /**
     * 从内存中取出缓存
     * @param key   key值
     * @param cls   class类
     * @return  Observable
     */
    private Observable<T> loadFromMemory(String key, Class<T> cls) {
        return mMemoryCache.get(key, cls);
    }

    /**
     * 从文件中取出缓存
     * @param key   key值
     * @param cls   class类
     * @return  Observable
     */
    private  Observable<T> loadFromDisk(final String key, Class<T> cls) {
        return mDiskCache.get(key, cls);
    }

    /**
     * 从网络中获取数据，并将数据放入缓存
     * @param key   key值
     * @param cls   class类
     * @param networkCache  网络获取类
     * @return  Observable
     */
    private Observable<T> loadFromNetwork(final String key, Class<T> cls
            , NetworkCache<T> networkCache) {
        //将从网络获取的数据加入缓存
        return networkCache.get(key, cls)
                .doOnNext(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        if (null != t && mDiskCache != null) {
                            mDiskCache.put(key, t);
                            mMemoryCache.put(key, t);
                        }
                    }
                });
    }

}
