package ir.abring.abringlibrary.services;

import android.util.Log;
import ir.abring.abringlibrary.Abring;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.register.Register;
import ir.abring.abringlibrary.network.AppAPI;
import ir.abring.abringlibrary.network.AppService;
import ir.abring.abringlibrary.network.RetrofitErrorHelper;
import ir.abring.abringlibrary.utils.NetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserServices {

    public static void register(String username,
                                String password,
                                String name,
                                String avatar,
                                String email,
                                String phone,
                                String reg_idgcm,
                                final AbringCallBack<Object, Object> abringCallBack) {

        if (NetworkUtil.isNetworkConnected(Abring.getContext())) {
            AppAPI mApiService = AppService.getInstance().getService(AppAPI.class);

            Call<Register> mEntityCall = mApiService.RegisterAPI(
                    username,
                    password,
                    name,
                    avatar,
                    email,
                    phone,
                    reg_idgcm,
                    Abring.getPackageName());

            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<Register>() {
                @Override
                public void onResponse(Call<Register> call, final Response<Register> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        abringCallBack.onSuccessful(response.body());
                    } else {
                        String msg = new RetrofitErrorHelper().getMessage(response, Abring.getContext());
                        abringCallBack.onFailure(msg);
                    }
                }

                @Override
                public void onFailure(Call<Register> call, Throwable t) {
                    String msg = new RetrofitErrorHelper().getMessage(t, Abring.getContext());
                    abringCallBack.onFailure(msg);
                }
            });

        } else {
            abringCallBack.onFailure(Abring.getContext().getString(R.string.no_connect_to_internet));
        }
    }

}
