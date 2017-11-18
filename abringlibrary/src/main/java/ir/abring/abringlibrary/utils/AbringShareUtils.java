package ir.abring.abringlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AbringShareUtils {

    public static void shareText(Context context, String extraSubject, String extraText) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, extraSubject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        context.startActivity(
                Intent.createChooser(sharingIntent, extraSubject));
    }

    public static void shareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }
}
