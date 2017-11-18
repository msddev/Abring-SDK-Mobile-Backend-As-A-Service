package ir.abring.abringlibrary.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.abring.abringlibrary.R;

public class AbringCustomToast {

    public static final int SUCCESS = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    public static final int INFO = 4;
    public static final int DEFAULT = 5;
    private static int textColor;

    public static void makeText(Context context, String msg, int type) {

        LinearLayout layout = new LinearLayout(context);
        TextView tv = new TextView(context);

        switch (type) {
            case SUCCESS:
                setBackgroundAndTextcolor(context, layout, R.color.toast_success, R.color.White);
                break;

            case ERROR:
                setBackgroundAndTextcolor(context, layout, R.color.toast_error, R.color.White);
                break;

            case INFO:
                setBackgroundAndTextcolor(context, layout, R.color.toast_info, R.color.White);
                break;

            case DEFAULT:
                setBackgroundAndTextcolor(context, layout, R.color.toast_default, R.color.Black);
                break;

            case WARNING:
                setBackgroundAndTextcolor(context, layout, R.color.toast_warning, R.color.Black);
                break;
        }

        layout.setPadding(dpToPixel(context, 10), dpToPixel(context, 5), dpToPixel(context, 10), dpToPixel(context, 5));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dpToPixel(context, 10), dpToPixel(context, 10), dpToPixel(context, 10), dpToPixel(context, 10));
        layout.setLayoutParams(params);

        // set the TextView properties like color, size etc
        tv.setTextColor(textColor);
        tv.setTextSize(14);

        tv.setGravity(Gravity.CENTER);
        //tv.setTypeface(CommonUtils.getTypeFace(context));

        // set the text you want to show in  Toast
        tv.setText(msg);

        layout.addView(tv);

        Toast toast = new Toast(context); //context is object of Context write "this" if you are an Activity
        // Set The layout as Toast View
        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_LONG);

        // Position you toast here toast position is 50 dp from bottom you can give any integral value
        toast.setGravity(Gravity.BOTTOM, 0, 70);
        toast.show();
    }

    private static void setBackgroundAndTextcolor(Context context, LinearLayout layout, int toast_color, int text_color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(drawRoundRect(AbringUIUtil.getColor(context, toast_color)));
        } else {
            layout.setBackground(drawRoundRect(AbringUIUtil.getColor(context, toast_color)));
        }
        textColor = AbringUIUtil.getColor(context, text_color);
    }

    private static GradientDrawable drawRoundRect(int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(5);
        shape.setColor(backgroundColor);
        return shape;
    }

    private static int dpToPixel(Context context, int dp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());

    }
}
