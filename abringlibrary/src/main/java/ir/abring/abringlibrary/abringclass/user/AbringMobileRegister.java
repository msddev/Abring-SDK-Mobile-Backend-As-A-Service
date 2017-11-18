package ir.abring.abringlibrary.abringclass.user;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.hawk.Hawk;
import java.io.File;

import ir.abring.abringlibrary.Abring;
import ir.abring.abringlibrary.AbringConstant;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.models.abringregister.AbringResult;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.services.AbringUserServices;
import ir.abring.abringlibrary.ui.dialog.AbringMobileRegisterDialog;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringlibrary.utils.AbringPermissaoUtils;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class AbringMobileRegister {

    private static Activity myActivity;

    private static String mobile;    //required
    private String username;    //optional
    private String password;    //optional
    private String deviceId;       //optional
    private String name;        //optional
    private File avatar;      //optional

    AbringMobileRegister(MobileRegisterBuilder registerBuilder) {
        this.mobile = registerBuilder.mobile;
        this.username = registerBuilder.username;
        this.password = registerBuilder.password;
        this.deviceId = registerBuilder.deviceId;
        this.name = registerBuilder.name;
        this.avatar = registerBuilder.avatar;
    }

    public static class MobileRegisterBuilder {
        private String mobile;    //required
        private String username;    //optional
        private String password;    //optional
        private String deviceId;       //optional
        private String name;        //optional
        private File avatar;      //optional

        public MobileRegisterBuilder setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public MobileRegisterBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public MobileRegisterBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public MobileRegisterBuilder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public MobileRegisterBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public MobileRegisterBuilder setAvatar(File avatar) {
            this.avatar = avatar;
            return this;
        }

        public AbringMobileRegister build() {
            return new AbringMobileRegister(this);
        }

    }

    public void mobileRegister(Activity mActivity, AbringCallBack abringCallBack) {
        myActivity = mActivity;

        if (avatar != null) {
            if (AbringPermissaoUtils.hasPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                runRegister(mActivity, abringCallBack);
            } else {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                AbringPermissaoUtils.showDialog(mActivity,
                        permissions,
                        AbringPermissaoUtils.READ_EXTERNAL_STORAGE,
                        mActivity.getString(R.string.abring_READ_EXTERNAL_STORAGE_STRING));
            }
        } else
            runRegister(mActivity, abringCallBack);

    }

    private void runRegister(final Activity mActivity, final AbringCallBack abringCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Run in new thread
                AbringUserServices.mobileRegister(mobile,
                        username,
                        password,
                        deviceId,
                        name,
                        avatar,
                        new AbringCallBack<Object, Object>() {
                            @Override
                            public void onSuccessful(final Object response) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(mActivity, R.string.send_accept_code, Toast.LENGTH_LONG).show();
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
     * register with ui
     */
    private boolean isUsername;
    private boolean isPassword;
    private boolean isDeviceId;
    private boolean isName;
    private boolean isAvatar;

    private AbringMobileRegisterDialog mFragment;

    AbringMobileRegister(DialogBuilder dialogBuilder) {
        this.isUsername = dialogBuilder.isUsername;
        this.isPassword = dialogBuilder.isPassword;
        this.isDeviceId = dialogBuilder.isDeviceId;
        this.isName = dialogBuilder.isName;
        this.isAvatar = dialogBuilder.isAvatar;
    }

    public static class DialogBuilder {
        private boolean isUsername;
        private boolean isPassword;
        private boolean isDeviceId;
        private boolean isName;
        private boolean isAvatar;

        public DialogBuilder setUsername(boolean username) {
            isUsername = username;
            return this;
        }

        public DialogBuilder setPassword(boolean password) {
            isPassword = password;
            return this;
        }

        public DialogBuilder setDeviceId(boolean deviceId) {
            isDeviceId = deviceId;
            return this;
        }

        public DialogBuilder setName(boolean name) {
            isName = name;
            return this;
        }

        public DialogBuilder setAvatar(boolean avatar) {
            isAvatar = avatar;
            return this;
        }

        public AbringMobileRegister build() {
            return new AbringMobileRegister(this);
        }

    }

    public void showDialog(final FragmentManager fragmentManager,
                           final Activity mActivity,
                           final AbringCallBack abringCallBack) {

        myActivity = mActivity;

        // close existing dialog fragments
        Fragment frag = fragmentManager.findFragmentByTag("RegisterDialogFragment");
        if (frag != null)
            fragmentManager.beginTransaction().remove(frag).commit();

        mFragment = AbringMobileRegisterDialog.getInstance(isUsername,
                isPassword,
                isDeviceId,
                isName,
                isAvatar,
                new AbringMobileRegisterDialog.OnRegisterFinishListener() {
                    @Override
                    public void onFinishDialog(final String mobile,
                                               String userName,
                                               String password,
                                               String deviceId,
                                               String name,
                                               File avatar) {

                        AbringMobileRegister abringUser = new AbringMobileRegister
                                .MobileRegisterBuilder()
                                .setMobile(mobile)
                                .setUsername(userName)
                                .setPassword(password)
                                .setDeviceId(deviceId)
                                .setName(name)
                                .setAvatar(avatar)
                                .build();

                        abringUser.mobileRegister(mActivity, new AbringCallBack() {
                            @Override
                            public void onSuccessful(Object response) {
                                Toast.makeText(mActivity, R.string.abring_send_accept_code, Toast.LENGTH_LONG).show();

                                // close existing dialog fragments
                                Fragment frag = fragmentManager.findFragmentByTag("RegisterDialogFragment");
                                if (frag != null)
                                    fragmentManager.beginTransaction().remove(frag).commit();

                                mFragment = AbringMobileRegisterDialog.getInstance(
                                        new AbringMobileRegisterDialog.OnActiveFinishListener() {
                                            @Override
                                            public void onFinishDialog(String code) {
                                                mobileVerify(code, new AbringCallBack<Object, Object>() {
                                                    @Override
                                                    public void onSuccessful(Object response) {

                                                        Toast.makeText(mActivity, mActivity.getString(R.string.abring_successful_responce), Toast.LENGTH_SHORT).show();

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

                                mFragment.show(fragmentManager, "RegisterDialogFragment");
                            }

                            @Override
                            public void onFailure(Object response) {
                                abringCallBack.onFailure(response);
                            }
                        });

                    }
                });

        mFragment.show(fragmentManager, "RegisterDialogFragment");
    }

    /**
     * verify mobile number
     */
    public static void mobileVerify(String code,
                                    final AbringCallBack<Object, Object> abringCallBack) {

        AbringUserServices.mobileVerify(code, mobile, new AbringCallBack<Object, Object>() {
            @Override
            public void onSuccessful(final Object response) {
                myActivity.runOnUiThread(new Runnable() {
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
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        abringCallBack.onFailure(response);
                    }
                });
            }
        });

    }

    /**
     * resend actice code
     */
    public static void mobileResendCode(final AbringCallBack<Object, Object> abringCallBack) {
        AbringUserServices.mobileResendCode(mobile, new AbringCallBack<Object, Object>() {
            @Override
            public void onSuccessful(final Object response) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        abringCallBack.onSuccessful(response);
                    }
                });
            }

            @Override
            public void onFailure(final Object response) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        abringCallBack.onFailure(response);
                    }
                });
            }
        });
    }
}
