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
import ir.abring.abringlibrary.services.AbringAppServices;
import ir.abring.abringlibrary.ui.dialog.AbringCheckUpdateDialog;
import ir.abring.abringlibrary.utils.AbringPermissaoUtils;
import ir.abring.abringlibrary.utils.AbringVersion;

public class AbringCheckUpdate {

    private String url;    //required
    private String dirPath;    //required
    private String fileName;    //required

    AbringCheckUpdate(LoginBuilder registerBuilder) {
        this.url = registerBuilder.url;
        this.dirPath = registerBuilder.dirPath;
        this.fileName = registerBuilder.fileName;
    }

    public static class LoginBuilder {

        private String url;    //required
        private String dirPath;    //required
        private String fileName;    //required

        public LoginBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public LoginBuilder setDirPath(String dirPath) {
            this.dirPath = dirPath;
            return this;
        }

        public LoginBuilder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public AbringCheckUpdate build() {
            return new AbringCheckUpdate(this);
        }

    }

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
            abringCallBack.onFailure(mActivity.getString(R.string.abring_not_access_to_storage));
        }
    }

    public void showDialog(final FragmentManager fragmentManager,
                           final Activity mActivity,
                           final AbringCallBack abringCallBack) {

        checkUpdate(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {

                AbringCheckUpdateModel mUpdateApp = (AbringCheckUpdateModel) response;
                int current = AbringVersion.getVersionCode(mActivity);
                if (Integer.valueOf(mUpdateApp.getResult().getAndroid().getForce()) > current)
                    forceUpdate(fragmentManager, mActivity, abringCallBack);
                else if (Integer.valueOf(mUpdateApp.getResult().getAndroid().getCurrent()) > current)
                    normalUpdate(fragmentManager, mActivity, abringCallBack);
                else
                    abringCallBack.onSuccessful(null);

            }

            @Override
            public void onFailure(Object response) {
                abringCallBack.onFailure(response);
            }
        });
    }

    private void forceUpdate(final FragmentManager fragmentManager,
                             final Activity mActivity,
                             final AbringCallBack abringCallBack) {

        new MaterialDialog.Builder(new ContextThemeWrapper(mActivity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_new_version)
                .content(R.string.abring_app_new_version)
                .positiveText(R.string.abring_download_app)
                .negativeText(R.string.abring_exit)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        checkPermission(fragmentManager, mActivity, abringCallBack);
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

    private void normalUpdate(final FragmentManager fragmentManager,
                              final Activity mActivity,
                              final AbringCallBack abringCallBack) {

        new MaterialDialog.Builder(new ContextThemeWrapper(mActivity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_new_version)
                .content(R.string.abring_app_new_version)
                .positiveText(R.string.abring_download_app)
                .negativeText(R.string.abring_exit)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        checkPermission(fragmentManager, mActivity, abringCallBack);
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


    private void checkPermission(FragmentManager fragmentManager,
                                 Activity mActivity,
                                 AbringCallBack abringCallBack) {

        if (AbringPermissaoUtils.hasPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                AbringPermissaoUtils.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            showDownloadDialog(abringCallBack, fragmentManager);

        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            AbringPermissaoUtils.showDialog(mActivity,
                    permissions,
                    AbringPermissaoUtils.READ_EXTERNAL_STORAGE,
                    mActivity.getString(R.string.abring_READ_EXTERNAL_STORAGE_STRING));
        }
    }

    private void showDownloadDialog(final AbringCallBack abringCallBack,
                                    FragmentManager fragmentManager) {

        Fragment frag = fragmentManager.findFragmentByTag("AbringCheckUpdateDialog");
        if (frag != null)
            fragmentManager.beginTransaction().remove(frag).commit();

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
        bundle.putString("url", url);
        bundle.putString("dirPath", dirPath);
        bundle.putString("fileName", fileName);
        mFragment.setArguments(bundle);
        mFragment.show(fragmentManager, "AbringCheckUpdateDialog");
    }
}
