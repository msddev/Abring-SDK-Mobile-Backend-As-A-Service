package ir.abring.abringlibrary.abringclass.user;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.hawk.Hawk;

import ir.abring.abringlibrary.AbringConstant;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.services.AbringUserServices;
import ir.abring.abringlibrary.utils.AbringCheck;

public class AbringLogout {
    /**
     * logout - with token
     */
    public static void logout(final Activity mActivity, final AbringCallBack abringCallBack) {

        final String token = Hawk.get(AbringConstant.ABRING_TOKEN, null);
        if (!AbringCheck.isEmpty(token)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Run in new thread
                    AbringUserServices.logout(token, new AbringCallBack<Object, Object>() {
                        @Override
                        public void onSuccessful(final Object response) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AbringServices.setUser(null);
                                    abringCallBack.onSuccessful(null);
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
        } else {
            AbringApiError errorResponse = new AbringApiError();
            errorResponse.setMessage(mActivity.getString(R.string.abring_not_login));
            abringCallBack.onFailure(errorResponse);
        }
    }

    /**
     * abring logout UI
     */
    public static void showDialog(final Activity mActivity, final AbringCallBack abringCallBack) {

        new MaterialDialog.Builder(new ContextThemeWrapper(mActivity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_logout)
                .content(R.string.abring_logout_question)
                .positiveText(R.string.abring_accept)
                .negativeText(R.string.abring_cancel)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        runlogoutAction(mActivity, abringCallBack);
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

    private static void runlogoutAction(final Activity mActivity, final AbringCallBack abringCallBack) {
        AbringLogout.logout(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                Toast.makeText(mActivity, R.string.logout_successful, Toast.LENGTH_LONG).show();
                abringCallBack.onSuccessful(response);
            }

            @Override
            public void onFailure(Object response) {
                AbringApiError apiError = (AbringApiError) response;
                Toast.makeText(mActivity,
                        AbringCheck.isEmpty(apiError.getMessage()) ? mActivity.getString(R.string.abring_failure_responce) : apiError.getMessage(),
                        Toast.LENGTH_SHORT).show();
                abringCallBack.onFailure(response);
            }
        });
    }

    /**
     * logout all
     */
    public static void logoutAll(final Activity mActivity, final AbringCallBack abringCallBack) {

        final String token = Hawk.get(AbringConstant.ABRING_TOKEN, null);
        if (!AbringCheck.isEmpty(token)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Run in new thread
                    AbringUserServices.logoutAll(token, new AbringCallBack<Object, Object>() {
                        @Override
                        public void onSuccessful(final Object response) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AbringServices.setUser(null);
                                    abringCallBack.onSuccessful(null);
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
        } else {
            AbringApiError errorResponse = new AbringApiError();
            errorResponse.setMessage(mActivity.getString(R.string.abring_not_login));
            abringCallBack.onFailure(errorResponse);
        }
    }

    /**
     * abring logout UI
     */
    public static void showDialogLogoutAll(final Activity mActivity, final AbringCallBack abringCallBack) {

        new MaterialDialog.Builder(new ContextThemeWrapper(mActivity, R.style.Theme_MatrialDialog))
                .title(R.string.abring_logout)
                .content(R.string.abring_logout_all_question)
                .positiveText(R.string.abring_accept)
                .negativeText(R.string.abring_cancel)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        runlogoutAllAction(mActivity, abringCallBack);
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

    private static void runlogoutAllAction(final Activity mActivity, final AbringCallBack abringCallBack) {
        AbringLogout.logoutAll(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                Toast.makeText(mActivity, R.string.logout_successful, Toast.LENGTH_LONG).show();
                abringCallBack.onSuccessful(response);
            }

            @Override
            public void onFailure(Object response) {
                AbringApiError apiError = (AbringApiError) response;
                Toast.makeText(mActivity,
                        AbringCheck.isEmpty(apiError.getMessage()) ? mActivity.getString(R.string.abring_failure_responce) : apiError.getMessage(),
                        Toast.LENGTH_SHORT).show();
                abringCallBack.onFailure(response);
            }
        });
    }
}
