package com.tayloryan.securecontacts.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtil {

    public static String getStringFromNull(String sourceStr) {
        if (null == sourceStr) {
            return "";
        }
        return sourceStr;

    }

    public static String getFormatTime(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        Calendar result = Calendar.getInstance();
        result.setTime(date);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        if (result.after(today)) {
            return new SimpleDateFormat("HH:mm").format(date);
        } else {
            return new SimpleDateFormat("MM-dd HH:mm").format(date);
        }
    }
}
