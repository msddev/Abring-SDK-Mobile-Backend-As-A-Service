package ir.abring.abringlibrary.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import ir.abring.abringlibrary.R;

public class AbringPermissaoUtils {

    public final static int READ_EXTERNAL_STORAGE = 101;
    public final static int READ_PHONE_STATE = 102;
    public final static int READ_SMS_STATE = 103;

    public static boolean useRunTimePermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasPermission(Activity activity, String permission) {
        if (useRunTimePermissions()) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestPermissionsActivity(Activity activity, String[] permission, int requestCode) {
        if (useRunTimePermissions()) {
            activity.requestPermissions(permission, requestCode);
        }
    }

    public static void requestPermissionsFragment(Fragment fragment, String[] permission, int requestCode) {
        if (useRunTimePermissions()) {
            fragment.requestPermissions(permission, requestCode);
        }
    }

    public static void showDialog(final Activity activity,
                                  final String[] permission,
                                  final int requestCode,
                                  final String permissionTextFA) {

        new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_permission)
                .content(String.format(activity.getString(R.string.abring_permission_content), permissionTextFA))
                .positiveText(R.string.abring_accept_permission)
                .negativeText(R.string.abring_cancel2)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestPermissionsActivity(activity, permission, requestCode);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void showDialog(final Activity activity,
                                  final Fragment fragment,
                                  final String[] permission,
                                  final int requestCode,
                                  final String permissionTextFA) {

        new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_permission)
                .content(String.format(activity.getString(R.string.abring_permission_content), permissionTextFA))
                .positiveText(R.string.abring_accept_permission)
                .negativeText(R.string.abring_cancel2)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestPermissionsFragment(fragment, permission, requestCode);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
