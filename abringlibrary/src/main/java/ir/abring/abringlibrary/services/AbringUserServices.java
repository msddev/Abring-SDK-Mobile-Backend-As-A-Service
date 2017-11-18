package ir.abring.abringlibrary.services;

import android.content.Context;
import android.util.Log;

import java.io.File;

import ir.abring.abringlibrary.Abring;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.network.AbringAppAPI;
import ir.abring.abringlibrary.network.AbringAppService;
import ir.abring.abringlibrary.network.AbringRetrofitErrorResponce;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringlibrary.utils.AbringNetworkUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbringUserServices {

    public static void register(String username,
                                String password,
                                String name,
                                File avatar,
                                String email,
                                String phone,
                                String reg_idgcm,
                                final AbringCallBack<Object, Object> abringCallBack) {

        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {
            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);

            // Parsing any Media type file
            MultipartBody.Part avatarToUpload = null;
            if (avatar != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), avatar);
                avatarToUpload = MultipartBody.Part.createFormData("avatar", avatar.getName(), requestBody);
            }

            RequestBody usernameRequest = AbringCheck.isEmpty(username) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody passwordRequest = AbringCheck.isEmpty(password) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), password);
            RequestBody nameRequest = AbringCheck.isEmpty(name) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody emailRequest = AbringCheck.isEmpty(email) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody phoneRequest = AbringCheck.isEmpty(phone) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), phone);
            RequestBody gcmRequest = AbringCheck.isEmpty(reg_idgcm) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), reg_idgcm);
            RequestBody appIdRequest = AbringCheck.isEmpty(Abring.getPackageName()) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), Abring.getPackageName());

            Call<AbringRegisterModel> mEntityCall = mApiService.RegisterAPI(
                    usernameRequest,
                    passwordRequest,
                    nameRequest,
                    avatarToUpload,
                    emailRequest,
                    phoneRequest,
                    gcmRequest,
                    appIdRequest);

            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<AbringRegisterModel>() {
                @Override
                public void onResponse(Call<AbringRegisterModel> call, final Response<AbringRegisterModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        abringCallBack.onSuccessful(response.body());
                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<AbringRegisterModel> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else {
            abringCallBack.onFailure(Abring.getContext().getString(R.string.abring_no_connect_to_internet));
        }
    }

    public static void mobileRegister(String mobile,
                                      String username,
                                      String password,
                                      String deviceId,
                                      String name,
                                      File avatar,
                                      final AbringCallBack<Object, Object> abringCallBack) {

        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {
            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);

            // Parsing any Media type file
            MultipartBody.Part avatarToUpload = null;
            if (avatar != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), avatar);
                avatarToUpload = MultipartBody.Part.createFormData("avatar", avatar.getName(), requestBody);
            }

            RequestBody mobileRequest = AbringCheck.isEmpty(mobile) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), mobile);
            RequestBody usernameRequest = AbringCheck.isEmpty(username) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody passwordRequest = AbringCheck.isEmpty(password) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), password);
            RequestBody deviceIdRequest = AbringCheck.isEmpty(password) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), deviceId);
            RequestBody nameRequest = AbringCheck.isEmpty(name) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody appIdRequest = AbringCheck.isEmpty(Abring.getPackageName()) ?
                    null : RequestBody.create(MediaType.parse("text/plain"), Abring.getPackageName());

            Call<Void> mEntityCall = mApiService.MobileRegisterAPI(
                    mobileRequest,
                    usernameRequest,
                    passwordRequest,
                    deviceIdRequest,
                    nameRequest,
                    avatarToUpload,
                    appIdRequest);

            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, final Response<Void> response) {
                    if (response.isSuccessful()) {
                        abringCallBack.onSuccessful("Verification code has been sent to you");
                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else {
            abringCallBack.onFailure(Abring.getContext().getString(R.string.abring_no_connect_to_internet));
        }
    }

    public static void mobileVerify(String code, String mobile, final AbringCallBack<Object, Object> abringCallBack) {

        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {
            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);

            Call<AbringRegisterModel> mEntityCall = mApiService.MobileVerifyAPI(mobile,
                    code,
                    Abring.getPackageName());

            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<AbringRegisterModel>() {
                @Override
                public void onResponse(Call<AbringRegisterModel> call, final Response<AbringRegisterModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        abringCallBack.onSuccessful(response.body());
                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<AbringRegisterModel> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else {
            abringCallBack.onFailure(Abring.getContext().getString(R.string.abring_no_connect_to_internet));
        }
    }

    public static void mobileResendCode(String mobile, final AbringCallBack<Object, Object> abringCallBack) {

        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {
            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);

            Call<Void> mEntityCall = mApiService.MobileResendCodeAPI(mobile,
                    Abring.getPackageName());

            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, final Response<Void> response) {
                    if (response.isSuccessful()) {
                        abringCallBack.onSuccessful(response.body());
                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else {
            abringCallBack.onFailure(Abring.getContext().getString(R.string.abring_no_connect_to_internet));
        }
    }

    public static void login(String username,
                             String password,
                             final AbringCallBack<Object, Object> abringCallBack) {

        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {
            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);

            Call<AbringRegisterModel> mEntityCall = mApiService.LoginAPI(username, password, Abring.getPackageName());

            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<AbringRegisterModel>() {
                @Override
                public void onResponse(Call<AbringRegisterModel> call, final Response<AbringRegisterModel> response) {
                    if (response.isSuccessful()) {
                        abringCallBack.onSuccessful(response.body());
                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<AbringRegisterModel> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else {
            abringCallBack.onFailure(Abring.getContext().getString(R.string.abring_no_connect_to_internet));
        }
    }

    public static void loginAsGuest(String deviceId, final AbringCallBack<Object, Object> abringCallBack) {

        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {
            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);

            Call<AbringRegisterModel> mEntityCall = mApiService.LoginAsGuestAPI(deviceId, Abring.getPackageName());

            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<AbringRegisterModel>() {
                @Override
                public void onResponse(Call<AbringRegisterModel> call, final Response<AbringRegisterModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        abringCallBack.onSuccessful(response.body());
                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<AbringRegisterModel> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else {
            abringCallBack.onFailure(Abring.getContext().getString(R.string.abring_no_connect_to_internet));
        }
    }

    public static void logout(String token, final AbringCallBack<Object, Object> abringCallBack) {

        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {
            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);

            Call<Void> mEntityCall = mApiService.LogoutAPI(token);

            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, final Response<Void> response) {
                    if (response.isSuccessful()) {
                        abringCallBack.onSuccessful(null);
                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else {
            abringCallBack.onFailure(Abring.getContext().getString(R.string.abring_no_connect_to_internet));
        }
    }

    private static void handleError(Object response, Context context, AbringCallBack<Object, Object> abringCallBack) {
        Object msg = new AbringRetrofitErrorResponce().getMessage(response, context);
        abringCallBack.onFailure(msg);
    }

}
