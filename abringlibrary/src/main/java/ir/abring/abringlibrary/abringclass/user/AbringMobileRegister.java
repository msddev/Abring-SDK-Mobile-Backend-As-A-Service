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

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.models.abringregister.AbringResult;
import ir.abring.abringlibrary.services.AbringUserServices;
import ir.abring.abringlibrary.ui.dialog.AbringMobileRegisterDialog;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class AbringMobileRegister {

    private static Activity myActivity;
    private int REQUEST_EXTERNAL_STORAGE = 110;

    private static String mobile;    //required
    private String username;    //optional
    private String password;    //optional
    private String deviceId;       //optional
    private String name;        //optional
    private File avatar;      //optional

    private final static String ABRING_USER_INFO = "ABRING_USER_INFO";

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

    public void register(Activity mActivity, AbringCallBack abringCallBack) {
        myActivity = mActivity;

        if (avatar != null) {
            if (checkSelfPermission(mActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                runRegister(mActivity, abringCallBack);

            } else {
                getPermission(mActivity);
            }
        } else {
            runRegister(mActivity, abringCallBack);
        }
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
                                            /*AbringRegisterModel register = (AbringRegisterModel) response;
                                            setUser(register.getResult());*/
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

    private void getPermission(final Activity mActivity) {
        new MaterialDialog.Builder(new ContextThemeWrapper(mActivity, R.style.Theme_MatrialDialog))
                .title(R.string.permission)
                .content(R.string.read_external_storage_permission_content)
                .positiveText(R.string.accept_permission)
                .negativeText(R.string.cancle)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_EXTERNAL_STORAGE);
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

    /**
     * register with ui
     */
    private boolean isUsername;
    private boolean isPassword;
    private boolean isDeviceId;
    private boolean isName;
    private boolean isAvatar;

    AbringMobileRegisterDialog mFragment;

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

                        abringUser.register(mActivity, new AbringCallBack() {
                            @Override
                            public void onSuccessful(Object response) {
                                Toast.makeText(mActivity, "کد فعالسازی ارسال شد...", Toast.LENGTH_LONG).show();

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
                                                        abringCallBack.onSuccessful(response);
                                                        mFragment.dismiss();
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
                        setUser(register.getResult());
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

    private static void setUser(AbringResult result) {
        Hawk.put(ABRING_USER_INFO, result);
    }


    public static Object getUser() {
        Object user = null;
        if (Hawk.contains(ABRING_USER_INFO))
            user = Hawk.get(ABRING_USER_INFO, null);
        return user;
    }
}
