package com.fyales.android.demo.net;

import com.fyales.android.demo.model.GirlModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author fyales
 * @since 2017/6/15
 */

public interface GankService {

    @GET("data/%E7%A6%8F%E5%88%A9/{pageSize}/{pageNum}")
    Observable<GirlModel> getFuli(@Path("pageSize") int pageSize, @Path("pageNum") int pageNum);
}
