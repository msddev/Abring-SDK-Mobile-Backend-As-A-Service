package ir.abring.abringlibrary.abringclass.user;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.io.File;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.services.AbringUserServices;
import ir.abring.abringlibrary.ui.dialog.AbringRegisterDialog;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringlibrary.utils.AbringPermissaoUtils;

public class AbringRegister {
    private String username;    //required
    private String password;    //required
    private String name;        //optional
    private File avatar;        //optional
    private String email;       //optional
    private String phone;       //optional
    private String reg_idgcm;   //optional

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
        private File avatar;        //optional
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

    public void register(Activity mActivity, AbringCallBack abringCallBack) {

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
                                AbringServices.setUser(register.getResult());

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
}
