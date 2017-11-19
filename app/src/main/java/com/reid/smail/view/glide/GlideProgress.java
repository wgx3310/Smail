package com.reid.smail.view.glide;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by reid on 2017/11/19.
 */

public class GlideProgress {

    static final Map<String, GlideProgressListener> PROGRESS_LISTENER_MAP = new HashMap<>();

    public static void addListener(String url, GlideProgressListener listener) {
        PROGRESS_LISTENER_MAP.put(url, listener);
    }

    public static void removeListener(String url) {
        PROGRESS_LISTENER_MAP.remove(url);
    }
}
