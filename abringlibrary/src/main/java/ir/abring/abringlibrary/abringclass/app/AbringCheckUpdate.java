package ir.abring.abringlibrary.abringclass.app;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringapp.AbringCheckUpdateModel;
import ir.abring.abringlibrary.services.AbringAppServices;
import ir.abring.abringlibrary.utils.AbringPermissaoUtils;
import ir.abring.abringlibrary.utils.AbringVersion;

public class AbringCheckUpdate {

    public void checkUpdate(Activity mActivity, final AbringCallBack abringCallBack) {

        if (AbringPermissaoUtils.hasPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                AbringPermissaoUtils.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            AbringAppServices.checkUpdate(new AbringCallBack<Object, Object>() {
                @Override
                public void onSuccessful(Object response) {
                    abringCallBack.onSuccessful(response);
                }

                @Override
                public void onFailure(Object response) {
                    abringCallBack.onFailure(response);
                }
            });

        } else {
            abringCallBack.onFailure("مجوز دسترسی به حافظه وجود ندارد!");
        }
    }

    public void showDialog(FragmentManager fragmentManager,
                           final Activity mActivity,
                           final AbringCallBack abringCallBack) {

        checkUpdate(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                AbringCheckUpdateModel mUpdateApp = (AbringCheckUpdateModel) response;
                int current = AbringVersion.getVersionCode(mActivity);
                if (Integer.valueOf(mUpdateApp.getResult().getAndroid().getForce()) > current)
                    forceUpdate(mActivity, abringCallBack);
                else if (Integer.valueOf(mUpdateApp.getResult().getAndroid().getCurrent()) > current)
                    normalUpdate(mActivity, abringCallBack);
                else
                    abringCallBack.onSuccessful(null);
            }

            @Override
            public void onFailure(Object response) {

            }
        });

        /*// close existing dialog fragments
        Fragment frag = fragmentManager.findFragmentByTag("CheckUpdateDialogFragment");
        if (frag != null)
            fragmentManager.beginTransaction().remove(frag).commit();*/

    }

    private void forceUpdate(final Activity mActivity, AbringCallBack abringCallBack) {
        new MaterialDialog.Builder(new ContextThemeWrapper(mActivity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_new_version)
                .content(R.string.abring_app_new_version)
                .positiveText(R.string.abring_download_app)
                .negativeText(R.string.abring_exit)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        checkPermission(mActivity);
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

    private void normalUpdate(final Activity mActivity, AbringCallBack abringCallBack) {
        new MaterialDialog.Builder(new ContextThemeWrapper(mActivity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_new_version)
                .content(R.string.abring_app_new_version)
                .positiveText(R.string.abring_download_app)
                .negativeText(R.string.abring_exit)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        checkPermission(mActivity);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //showNextIntent();
                    }
                })
                .show();
    }


    private void checkPermission(Activity mActivity) {
        if (AbringPermissaoUtils.hasPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                AbringPermissaoUtils.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            AbringPermissaoUtils.showDialog(mActivity,
                    permissions,
                    AbringPermissaoUtils.READ_EXTERNAL_STORAGE,
                    mActivity.getString(R.string.abring_READ_EXTERNAL_STORAGE_STRING));
        }
    }
}
