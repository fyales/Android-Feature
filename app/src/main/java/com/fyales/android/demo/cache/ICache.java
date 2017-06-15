package com.fyales.android.demo.cache;

import rx.Observable;

/**
 * @author fyales
 * @since 2017/6/15
 */

public interface ICache<T> {
    //获取缓存
    Observable<T> get(String key,Class<T> cls);
    //存入缓存
    void put(String key,T t);
}
