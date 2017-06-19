package com.fyales.android.demo.model;

import com.google.gson.Gson;

/**
 * @author fyales
 * @since 2017/6/15
 */

public class Girl {
    public String _id;
    public String createdAt;
    public String desc;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public String used;
    public String who;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
