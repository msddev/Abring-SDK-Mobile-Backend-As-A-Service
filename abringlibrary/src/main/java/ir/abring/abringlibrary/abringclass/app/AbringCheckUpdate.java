package ir.abring.abringlibrary.abringclass.app;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.annotation.NonNull;
import android.view.ContextThemeWrapper;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringapp.AbringCheckUpdateModel;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.services.AbringAppServices;
import ir.abring.abringlibrary.ui.dialog.AbringCheckUpdateDialog;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringlibrary.utils.AbringPermissaoUtils;
import ir.abring.abringlibrary.utils.AbringVersion;

public class AbringCheckUpdate {

    private static Activity mActivity;
    private static FragmentManager mFragmentManager;
    private static AbringCheckUpdateModel mUpdateApp;

    public static void check(Activity activity, final AbringCallBack abringCallBack) {

        mActivity = activity;
        new Thread(new Runnable() {
            @Override
            public void run() {

                AbringAppServices.checkUpdate(new AbringCallBack<Object, Object>() {
                    @Override
                    public void onSuccessful(final Object response) {

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                abringCallBack.onSuccessful(response);

                            }
                        });
                    }

                    @Override
                    public void onFailure(final Object response) {

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                abringCallBack.onFailure(response);

                            }
                        });
                    }
                });

            }
        }).start();

    }

    public static void showDialog(final FragmentManager fragmentManager,
                                  final Activity activity,
                                  final AbringCallBack abringCallBack) {

        mActivity = activity;
        mFragmentManager = fragmentManager;

        check(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                mUpdateApp = (AbringCheckUpdateModel) response;

                if (mUpdateApp.getResult().getAndroid() != null) {

                    int current = AbringVersion.getVersionCode(mActivity);

                    if (Integer.valueOf(mUpdateApp.getResult().getAndroid().getForce()) > current)
                        forceUpdate(abringCallBack);

                    else if (Integer.valueOf(mUpdateApp.getResult().getAndroid().getCurrent()) > current)
                        normalUpdate(abringCallBack);

                    else
                        abringCallBack.onSuccessful(mUpdateApp);

                } else
                    abringCallBack.onSuccessful(mUpdateApp);
            }

            @Override
            public void onFailure(Object response) {
                abringCallBack.onFailure(response);
            }
        });
    }

    private static void forceUpdate(final AbringCallBack abringCallBack) {

        new MaterialDialog.Builder(new ContextThemeWrapper(mActivity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_new_version)
                .content(R.string.abring_app_new_version)
                .positiveText(R.string.abring_download_app)
                .negativeText(R.string.abring_exit)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        checkPermission(abringCallBack);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mActivity.finish();
                    }
                })
                .show();
    }

    private static void normalUpdate(final AbringCallBack abringCallBack) {

        new MaterialDialog.Builder(new ContextThemeWrapper(mActivity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_new_version)
                .content(R.string.abring_app_new_version)
                .positiveText(R.string.abring_download_app)
                .negativeText(R.string.abring_exit)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        checkPermission(abringCallBack);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        abringCallBack.onSuccessful(null);
                    }
                })
                .show();
    }


    private static void checkPermission(AbringCallBack abringCallBack) {

        if (AbringPermissaoUtils.hasPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                AbringPermissaoUtils.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            showDownloadDialog(abringCallBack);

        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            AbringPermissaoUtils.showDialog(mActivity,
                    permissions,
                    AbringPermissaoUtils.READ_EXTERNAL_STORAGE,
                    mActivity.getString(R.string.abring_READ_EXTERNAL_STORAGE_STRING));
        }
    }

    private static void showDownloadDialog(final AbringCallBack abringCallBack) {

        Fragment frag = mFragmentManager.findFragmentByTag("AbringCheckUpdateDialog");
        if (frag != null)
            mFragmentManager.beginTransaction().remove(frag).commit();

        AbringCheckUpdateDialog mFragment =
                AbringCheckUpdateDialog.getInstance(new AbringCallBack<Object, Object>() {
                    @Override
                    public void onSuccessful(Object response) {
                        abringCallBack.onSuccessful(null);
                    }

                    @Override
                    public void onFailure(Object response) {
                        abringCallBack.onFailure(null);
                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString("url", mUpdateApp.getResult().getAndroid().getLink());
        mFragment.setArguments(bundle);
        mFragment.show(mFragmentManager, "AbringCheckUpdateDialog");
    }
}
