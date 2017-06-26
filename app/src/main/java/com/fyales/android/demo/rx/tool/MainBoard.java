package com.fyales.android.demo.rx.tool;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * @author fyales
 * @since 2017/6/13
 */

public class MainBoard {

    private static MainBoard sMainBoard;
    private Subject<Object,Object> subject;
    private String mKey;


    private void setKey(String key){
        mKey = key;
    }

    private MainBoard(){
        subject = PublishSubject.create();
    }

    public static MainBoard getInstance(){
        if (sMainBoard == null){
            synchronized (RxBus.class){
                if (sMainBoard == null){
                    sMainBoard = new MainBoard();
                }
            }
        }
        return sMainBoard;
    }

    public Observable getObsevable(){
//        return subject;
        return subject.startWith(mKey);
    }

    public void notifyDataChanged(String key){
        subject.onNext(key);
    }


}
