package com.fyales.android.demo.rx.tool;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RxBus
 * Created by fyales on 2017/6/13.
 */

public class RxBus {

    private static RxBus sRxBus;
    private final Subject<Object,Object> bus;


    private RxBus() {
        this.bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance(){
        if (sRxBus == null){
            synchronized (RxBus.class){
                if (sRxBus == null){
                    sRxBus = new RxBus();
                }
            }
        }
        return sRxBus;
    }

    /**
     * 发送事件
     * @param o 传递的数据
     */
    public void post(Object o){
        bus.onNext(o);
    }

    /**
     * 根据事件类型决定事件是否向下传递
     * @param eventType 事件类型
     * @param <T>   事件类型
     * @return  过滤后的Observable
     */
    public <T> Observable<T> tObservable(Class<T> eventType){
        return bus.ofType(eventType);
    }

}
