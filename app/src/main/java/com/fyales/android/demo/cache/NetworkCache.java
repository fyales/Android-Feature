package com.fyales.android.demo.cache;

import rx.Observable;

/**
 * @author fyales
 * @since 2017/6/15
 */
public abstract class NetworkCache<T> {
    public abstract Observable<T> get(String key, final Class<T> cls);

}
