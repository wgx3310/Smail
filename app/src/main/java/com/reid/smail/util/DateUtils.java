package com.reid.smail.util;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

/**
 * Created by reid on 2017/9/1.
 */

public class DateUtils {

    public static String  formatDateUseCh(long ms) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(ms);
    }

    public static long parseISO8601(String date) {
        try {
            return ISO8601Utils.parse(date, new ParsePosition(0)).getTime();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }
}
