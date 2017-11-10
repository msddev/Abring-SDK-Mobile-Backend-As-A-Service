package ir.abring.abringlibrary.services;

import android.content.Context;
import android.util.Log;
import ir.abring.abringlibrary.Abring;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegister;
import ir.abring.abringlibrary.network.AbringAppAPI;
import ir.abring.abringlibrary.network.AbringAppService;
import ir.abring.abringlibrary.network.AbringRetrofitErrorResponce;
import ir.abring.abringlibrary.utils.NetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbringUserServices {

    public static void register(String username,
                                String password,
                                String name,
                                String avatar,
                                String email,
                                String phone,
                                String reg_idgcm,
                                final AbringCallBack<Object, Object> abringCallBack) {

        if (NetworkUtil.isNetworkConnected(Abring.getContext())) {
            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);

            Call<AbringRegister> mEntityCall = mApiService.RegisterAPI(
                    username,
                    password,
                    name,
                    avatar,
                    email,
                    phone,
                    reg_idgcm,
                    Abring.getPackageName());

            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<AbringRegister>() {
                @Override
                public void onResponse(Call<AbringRegister> call, final Response<AbringRegister> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        abringCallBack.onSuccessful(response.body());
                    } else {
                       handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<AbringRegister> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else {
            abringCallBack.onFailure(Abring.getContext().getString(R.string.no_connect_to_internet));
        }
    }

    private static void handleError(Object response, Context context, AbringCallBack<Object, Object> abringCallBack) {
        Object msg = new AbringRetrofitErrorResponce().getMessage(response, context);
        abringCallBack.onFailure(msg);
    }

}
