package ir.abring.abringlibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat HOUR_MINUTE = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat HOUR = new SimpleDateFormat("HH");

    public static final long HOUR_OF_DAY = 24;
    public static final long MIN_OF_HOUR = 60;
    public static final long SEC_OF_MIN = 60;
    public static final long MILLIS_OF_SEC = 1000;

    public static long halfAnHourSeconds() {
        return MIN_OF_HOUR * SEC_OF_MIN / 2;
    }

    public static long anHourSeconds() {
        return MIN_OF_HOUR * SEC_OF_MIN;
    }

    public static long twoHourSeconds() {
        return MIN_OF_HOUR * SEC_OF_MIN * 2;
    }

    public static String getHourMinute() {
        return HOUR_MINUTE.format(new Date());
    }

    public static String milliseconds2String(long milliSeconds) {
        return DATE_FORMAT.format(new Date(milliSeconds));
    }

    public static boolean isToday(long timestamp) {
        long millOfDay = MILLIS_OF_SEC * SEC_OF_MIN * MIN_OF_HOUR * HOUR_OF_DAY;
        return System.currentTimeMillis() / millOfDay == timestamp / millOfDay;
    }

    public static boolean isNight() {
        int currentHour = Integer.parseInt(HOUR.format(new Date()));
        return currentHour >= 19 || currentHour <= 6;
    }

    public static String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public static String twoDigitHours(String hours) {
        String[] separated = hours.split(":");
        if (Integer.valueOf(separated[0]) < 10)
            separated[0] = "0".concat(separated[0]);
        if (Integer.valueOf(separated[1]) < 10)
            separated[1] = "0".concat(separated[1]);
        hours = separated[0] + ":" + separated[1];
        return hours;
    }

    public static String twoDigitDate(String date) {
        String[] separated = date.split("-");
        if (Integer.valueOf(separated[1]) < 10)
            separated[1] = "0".concat(separated[1]);
        if (Integer.valueOf(separated[2]) < 10)
            separated[2] = "0".concat(separated[2]);
        date = separated[0] + "-" + separated[1] + "-" + separated[2];
        return date;
    }
}
