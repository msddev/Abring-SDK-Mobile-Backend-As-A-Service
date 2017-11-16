package ir.abring.abringlibrary.abringclass.user;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import com.orhanobut.hawk.Hawk;
import java.io.File;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringResult;
import ir.abring.abringlibrary.services.AbringUserServices;
import ir.abring.abringlibrary.ui.dialog.AbringRegisterDialog;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class AbringMobileRegister {
    private String mobile;    //required
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

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAvatar(File avatar) {
            this.avatar = avatar;
        }

        public AbringMobileRegister build() {
            return new AbringMobileRegister(this);
        }

    }

    public void register(final Activity mActivity, final AbringCallBack abringCallBack) {
        if (avatar != null &&
                checkSelfPermission(mActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

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
                                            abringregister.AbringRegister register = (ir.abring.abringlibrary.models.abringregister.AbringRegister) response;
                                            setUser(register.getResult());
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
        } else
            Toast.makeText(mActivity, mActivity.getString(R.string.read_external_storage_permission), Toast.LENGTH_LONG).show();

    }

    /**
     * register with ui
     */
    private boolean isName;
    private boolean isAvatar;
    private boolean isEmail;
    private boolean isPhone;

    AbringRegisterDialog mFragment;

    AbringMobileRegister(DialogBuilder dialogBuilder) {
        this.isName = dialogBuilder.isName;
        this.isAvatar = dialogBuilder.isAvatar;
        this.isEmail = dialogBuilder.isEmail;
        this.isPhone = dialogBuilder.isPhone;
    }

    public static class DialogBuilder {
        private boolean isName;
        private boolean isAvatar;
        private boolean isEmail;
        private boolean isPhone;

        public DialogBuilder setName(boolean name) {
            isName = name;
            return this;
        }

        public DialogBuilder setAvatar(boolean avatar) {
            isAvatar = avatar;
            return this;
        }

        public DialogBuilder setEmail(boolean email) {
            isEmail = email;
            return this;
        }

        public DialogBuilder setPhone(boolean phone) {
            isPhone = phone;
            return this;
        }

        public AbringMobileRegister build() {
            return new AbringMobileRegister(this);
        }

    }

    public void showDialog(FragmentManager fragmentManager,
                           final Activity activity,
                           final AbringCallBack abringCallBack) {
        // close existing dialog fragments
        Fragment frag = fragmentManager.findFragmentByTag("RegisterDialogFragment");
        if (frag != null)
            fragmentManager.beginTransaction().remove(frag).commit();

        mFragment = AbringRegisterDialog.getInstance(isName, isAvatar, isEmail, isPhone,
                new AbringRegisterDialog.OnFinishListener() {
                    @Override
                    public void onFinishDialog(String userName,
                                               String password,
                                               String name,
                                               String phone,
                                               String email,
                                               File avatar) {

                        AbringMobileRegister abringUser = new AbringMobileRegister
                                .RegisterBuilder()
                                .setUsername(userName)
                                .setPassword(password)
                                .setName(name)
                                .setPhone(phone)
                                .setEmail(email)
                                .setAvatar(avatar)
                                .build();

                        abringUser.register(activity, new AbringCallBack() {
                            @Override
                            public void onSuccessful(Object response) {
                                ir.abring.abringlibrary.models.abringregister.AbringRegister register = (ir.abring.abringlibrary.models.abringregister.AbringRegister) response;
                                setUser(register.getResult());
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

    private void setUser(AbringResult result) {
        Hawk.put(ABRING_USER_INFO, result);
    }


    public static Object getUser() {
        Object user = null;
        if (Hawk.contains(ABRING_USER_INFO))
            user = Hawk.get(ABRING_USER_INFO, null);
        return user;
    }
}
