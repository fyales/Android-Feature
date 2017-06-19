package com.fyales.android.demo.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.GlideModule;
import com.fyales.android.demo.utils.FileUtils;

import java.io.File;

/**
 * Created by wpj on 16/6/15下午3:23.
 */
public class CustomModule implements GlideModule {
    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        // Apply options to the builder here.

        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                // Careful: the external cache directory doesn't enforce permissions
                File cacheLocation = new File(FileUtils.getCacheDir(context), "glide");
                cacheLocation.mkdirs();
                //104857600 == 100M
                return DiskLruCacheWrapper.get(cacheLocation, 104857600);
            }
        });
//        builder.setDiskCache(new DiskLruCacheFactory(FileUtils.getCacheDir(), 104857600));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.

    }
}
