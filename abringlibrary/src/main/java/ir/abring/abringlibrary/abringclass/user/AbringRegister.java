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
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.models.abringregister.AbringResult;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.services.AbringUserServices;
import ir.abring.abringlibrary.ui.dialog.AbringRegisterDialog;
import ir.abring.abringlibrary.utils.Check;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class AbringRegister {
    private String username;    //required
    private String password;    //required
    private String name;        //optional
    private File avatar;      //optional
    private String email;       //optional
    private String phone;       //optional
    private String reg_idgcm;   //optional

    private final static String ABRING_USER_INFO = "ABRING_USER_INFO";

    AbringRegister(RegisterBuilder registerBuilder) {
        this.username = registerBuilder.username;
        this.password = registerBuilder.password;
        this.name = registerBuilder.name;
        this.avatar = registerBuilder.avatar;
        this.email = registerBuilder.email;
        this.phone = registerBuilder.phone;
        this.reg_idgcm = registerBuilder.reg_idgcm;
    }

    public static class RegisterBuilder {
        private String username;    //required
        private String password;    //required
        private String name;        //optional
        private File avatar;      //optional
        private String email;       //optional
        private String phone;       //optional
        private String reg_idgcm;   //optional

        public RegisterBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public RegisterBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public RegisterBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RegisterBuilder setAvatar(File avatar) {
            this.avatar = avatar;
            return this;
        }

        public RegisterBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public RegisterBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public RegisterBuilder setReg_idgcm(String reg_idgcm) {
            this.reg_idgcm = reg_idgcm;
            return this;
        }

        public AbringRegister build() {
            return new AbringRegister(this);
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
                    AbringUserServices.register(username,
                            password,
                            name,
                            avatar,
                            email,
                            phone,
                            reg_idgcm,
                            new AbringCallBack<Object, Object>() {
                                @Override
                                public void onSuccessful(final Object response) {
                                    mActivity.runOnUiThread(new Runnable() {
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

    AbringRegister(DialogBuilder dialogBuilder) {
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

        public AbringRegister build() {
            return new AbringRegister(this);
        }

    }

    public void showDialog(FragmentManager fragmentManager,
                           final Activity mActivity,
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

                        AbringRegister abringUser = new AbringRegister
                                .RegisterBuilder()
                                .setUsername(userName)
                                .setPassword(password)
                                .setName(name)
                                .setPhone(phone)
                                .setEmail(email)
                                .setAvatar(avatar)
                                .build();

                        abringUser.register(mActivity, new AbringCallBack() {
                            @Override
                            public void onSuccessful(Object response) {
                                AbringRegisterModel register = (AbringRegisterModel) response;
                                setUser(register.getResult());

                                Toast.makeText(mActivity, mActivity.getString(R.string.successful_responce), Toast.LENGTH_SHORT).show();

                                abringCallBack.onSuccessful(response);
                                mFragment.dismiss();
                            }

                            @Override
                            public void onFailure(Object response) {
                                AbringApiError apiError = (AbringApiError) response;

                                Toast.makeText(mActivity,
                                        Check.isEmpty(apiError.getMessage()) ? mActivity.getString(R.string.failure_responce) :
                                                apiError.getMessage(), Toast.LENGTH_SHORT).show();

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
