package ir.abring.abringlibrary.abringclass;

import android.app.Activity;
import android.util.Log;

import ir.abring.abringlibrary.Abring;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.services.UserServices;

public class AbringUser {
    private String username;    //required
    private String password;    //required
    private String name;        //optional
    private String avatar;      //optional
    private String email;       //optional
    private String phone;       //optional
    private String reg_idgcm;   //optional

    AbringUser(RegisterBuilder registerBuilder) {
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
        private String avatar;      //optional
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

        public RegisterBuilder setAvatar(String avatar) {
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

        public AbringUser build() {
            return new AbringUser(this);
        }

    }


    public void register(final Activity mActivity, final AbringCallBack abringCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //Run in new thread
                UserServices.register(username,
                        password,
                        name,
                        avatar,
                        email,
                        phone,
                        reg_idgcm,
                        new AbringCallBack<Object, Object>() {
                            @Override
                            public void onSuccessful(final Object response) {
                                Log.d("Tag", "onSuccessful: " + response.toString());

                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        abringCallBack.onSuccessful(response);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(final Object response) {
                                Log.d("Tag", "onFailure: " + response.toString());

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
}
