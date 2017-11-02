package ir.abring.abringlibrary.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.LruCache;

import ir.abring.abringlibrary.R;

public class CommonUtils {

    /**
     * @return IRANSansMobile.ttf
     */
    public static String getFontName(Context context) {
        int[] attrs = {R.attr.fontPath};
        TypedArray a = context.obtainStyledAttributes(R.style.StandardText, attrs);
        return a.getString(0).replace("fonts/", "");
    }

    /**
     * @return fonts/IRANSansMobile.ttf
     */
    public static String getFontPath(Context context) {
        int[] attrs = {R.attr.fontPath};
        TypedArray a = context.obtainStyledAttributes(R.style.StandardText, attrs);
        return a.getString(0);
    }

    /**
     * retuen Typeface file
     * @param context
     * @return
     */
    public static Typeface getTypeFace(Context context) {

        LruCache<String, Typeface> sTypeFaceCache = new LruCache<>(12);
        Typeface mTypeFace = sTypeFaceCache.get(CommonUtils.getFontName(context));

        if (mTypeFace == null) {
            mTypeFace = Typeface.createFromAsset(context.getAssets(), CommonUtils.getFontPath(context));
            sTypeFaceCache.put(CommonUtils.getFontName(context), mTypeFace);
        }
        return mTypeFace;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
