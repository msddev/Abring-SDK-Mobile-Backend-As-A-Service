package ir.abring.abringlibrary.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TypefaceSpan extends MetricAffectingSpan {

    private static final float ACTIONBAR_TITLE_TEXT_SIZE = 15.0f;

    private Typeface mTypeface;

    private Context context;

    public TypefaceSpan(Context context, String typefaceName) {

        this.context = context;
        LruCache<String, Typeface> sTypeFaceCache = new LruCache<>(12);
        Typeface typeFace = sTypeFaceCache.get(typefaceName);

        if (typeFace == null) {
            typeFace = Typeface.createFromAsset(context.getAssets(), typefaceName);
            sTypeFaceCache.put(typefaceName, typeFace);
        }

        mTypeface = typeFace;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        final float scale = context.getResources().getDisplayMetrics().density;

        int textSize = (int) (ACTIONBAR_TITLE_TEXT_SIZE * scale + 0.5f);
        p.setTypeface(mTypeface);
        p.setTextSize(textSize);
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint p) {
        final float scale = context.getResources().getDisplayMetrics().density;

        int textSize = (int) (ACTIONBAR_TITLE_TEXT_SIZE * scale + 0.5f);
        p.setTypeface(mTypeface);
        p.setTextSize(textSize);
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

}

