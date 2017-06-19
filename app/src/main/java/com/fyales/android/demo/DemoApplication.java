package com.fyales.android.demo;

import android.app.Application;
import android.content.res.Configuration;

import com.fyales.android.demo.cache.CacheManager;
import com.fyales.android.demo.imageloader.ImageUtils;
import com.fyales.android.demo.imageloader.glide.GlideImageLoader;

/**
 * @author fyales
 * @since 2017/6/16
 */

public class DemoApplication extends Application {

    private static DemoApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        ImageUtils.getInstance().init(new GlideImageLoader());
        CacheManager.getInstance(this);

    }

    private static DemoApplication getInstance(){
        return application;
    }
}
