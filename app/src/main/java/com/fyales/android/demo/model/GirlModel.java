package com.fyales.android.demo.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author fyales
 * @since 2017/6/15
 */

public class GirlModel {

    public boolean error;
    public List<Girl> results;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
