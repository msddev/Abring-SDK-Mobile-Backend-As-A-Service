package ir.abring.abringlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;

import ir.abring.abringlibrary.R;

public class AbringThemeUtil {

    public static int getThemeColor(Context context, int attrRes) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{attrRes});
        int color = typedArray.getColor(0, 0xffffff);
        typedArray.recycle();
        return color;
    }

    public static int getCurrentColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.resourceId;
    }

    public static int getCurrentColorPrimaryDark(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.resourceId;
    }

    public static int getCurrentColorAccent(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.resourceId;
    }

    public static Drawable setTintDrawable(Drawable drawable, Context context) {
        Drawable drawable1 = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable1, context.getResources().getColorStateList(getCurrentColorPrimary(context)));
        return drawable1;
    }

    public static Drawable setTintDrawable(@DrawableRes int drawable, Context context, @ColorRes int color) {
        Drawable drawable1 = DrawableCompat.wrap(context.getResources().getDrawable(drawable));
        DrawableCompat.setTintList(drawable1, context.getResources().getColorStateList(color));
        return drawable1;
    }

    public static void restartActivityToApplyTheme(Activity activity){
        // Restart activity to apply theme
        activity.overridePendingTransition(0, 0);
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(activity.getIntent());
    }
}
