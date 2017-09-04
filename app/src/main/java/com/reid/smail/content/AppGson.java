package com.reid.smail.content;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by reid on 2017/9/3.
 */

public class AppGson {
    private static Gson gson;

    public static Gson get(){
        if (gson == null){
            create();
        }
        return gson;
    }

    private static void create() {
        if (gson != null) return;

        gson = new GsonBuilder().create();
    }
}
