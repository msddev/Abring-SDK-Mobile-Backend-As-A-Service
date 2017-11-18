package ir.abring.abringlibrary.abringclass.user;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import com.orhanobut.hawk.Hawk;

import ir.abring.abringlibrary.AbringConstant;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.models.abringregister.AbringResult;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.services.AbringUserServices;
import ir.abring.abringlibrary.ui.dialog.AbringLoginDialog;
import ir.abring.abringlibrary.utils.AbringCheck;

public class AbringLogin {
    private String username;    //required
    private String password;    //required

    AbringLogin(LoginBuilder registerBuilder) {
        this.username = registerBuilder.username;
        this.password = registerBuilder.password;
    }

    public static class LoginBuilder {
        private String username;    //required
        private String password;    //required

        public LoginBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public LoginBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public AbringLogin build() {
            return new AbringLogin(this);
        }

    }

    public void login(final Activity mActivity, final AbringCallBack abringCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Run in new thread
                AbringUserServices.login(username, password,
                        new AbringCallBack<Object, Object>() {
                            @Override
                            public void onSuccessful(final Object response) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AbringRegisterModel register = (AbringRegisterModel) response;
                                        AbringServices.setUser(register.getResult());
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

    /**
     * login with ui
     */

    private static AbringLoginDialog mFragment;

    public static void showDialog(FragmentManager fragmentManager,
                           final Activity mActivity,
                           final AbringCallBack abringCallBack) {
        // close existing dialog fragments
        Fragment frag = fragmentManager.findFragmentByTag("LoginDialogFragment");
        if (frag != null)
            fragmentManager.beginTransaction().remove(frag).commit();

        mFragment = AbringLoginDialog.getInstance(new AbringLoginDialog.OnFinishListener() {
            @Override
            public void onFinishDialog(String userName, String password) {

                AbringLogin abringUser = new AbringLogin
                        .LoginBuilder()
                        .setUsername(userName)
                        .setPassword(password)
                        .build();

                abringUser.login(mActivity, new AbringCallBack() {
                    @Override
                    public void onSuccessful(Object response) {
                        AbringRegisterModel register = (AbringRegisterModel) response;
                        AbringServices.setUser(register.getResult());

                        Toast.makeText(mActivity, mActivity.getString(R.string.abring_login_successfull), Toast.LENGTH_SHORT).show();

                        abringCallBack.onSuccessful(response);
                        mFragment.dismiss();
                    }

                    @Override
                    public void onFailure(Object response) {
                        AbringApiError apiError = (AbringApiError) response;

                        Toast.makeText(mActivity,
                                AbringCheck.isEmpty(apiError.getMessage()) ? mActivity.getString(R.string.abring_failure_responce) :
                                        apiError.getMessage(), Toast.LENGTH_SHORT).show();

                        abringCallBack.onFailure(response);
                    }
                });
            }
        });

        mFragment.show(fragmentManager, "LoginDialogFragment");
    }
}
