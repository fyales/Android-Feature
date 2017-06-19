package com.fyales.android.demo.model;

/**
 * @author fyales
 * @since 2017/6/16
 */

public class Image {
    public String url;
    public String desc;

    @Override
    public String toString() {
        return "Image{" +
                "url='" + url + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
