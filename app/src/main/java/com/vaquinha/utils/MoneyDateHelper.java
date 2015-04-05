package com.vaquinha.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MoneyDateHelper {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private MoneyDateHelper() {}

    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static String nowFormatted() {
        return dateFormat.format(now());
    }

    public static Calendar parse(String formattedDate) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(formattedDate));
            return cal;
        } catch (Exception e) {
            RuntimeException runtimeException = new RuntimeException("Error parsing date");
            runtimeException.setStackTrace(e.getStackTrace());
            throw runtimeException;
        }
    }

    public static String format(Calendar calendar) {
        return dateFormat.format(calendar.getTime());
    }
}
