package ir.abring.abringlibrary.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateConvertUtils {

    public static final String DATA_FORMAT_PATTEN_YYYY_MMMM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_FORMAT_PATTEN_YYYY_MMMM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATA_FORMAT_PATTEN_YYYY_MMMM_DD = "yyyy-MM-dd";
    public static final String DATA_FORMAT_PATTEN_HH_MM = "HH:mm";


    public static long dateToTimeStamp(String data, String dataFormatPatten) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataFormatPatten);
        Date date = null;
        try {
            date = simpleDateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        return date.getTime();
    }

    //convert unix epoch timestamp (seconds) to milliseconds
    public static String timeStampToDateString(long time, String dataFormatPatten) {
        @SuppressLint("SimpleDateFormat")

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        time = time * 1000L;
        cal.setTimeInMillis(time);
        return DateFormat.format(dataFormatPatten, cal).toString();
    }

    //convert milliseconds
    public static String timeStampSecondsToDateString(long time, String dataFormatPatten) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format(dataFormatPatten, cal).toString();
    }

    public static Date dateStringToDate(String dateString, String dataFormatPatten) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataFormatPatten);
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        return date;
    }

    public static Calendar dateStringToCalendar(String dateString, String dataFormatPatten) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormatPatten, Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String currentJalaliDateTime() {
        Calendar c = Calendar.getInstance();
        return JalaliCalendar.getStrJalaliDate(c.getTime(), true);
    }

    public static Date currentMiladiDateTime() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }
}
