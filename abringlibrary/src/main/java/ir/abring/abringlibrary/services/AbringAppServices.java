package ir.abring.abringlibrary.services;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import ir.abring.abringlibrary.Abring;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringapp.AbringCheckUpdateModel;
import ir.abring.abringlibrary.network.AbringAppAPI;
import ir.abring.abringlibrary.network.AbringAppService;
import ir.abring.abringlibrary.network.AbringRetrofitErrorResponce;
import ir.abring.abringlibrary.utils.AbringNetworkUtil;
import ir.abring.abringlibrary.utils.AbringUtils;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbringAppServices {

    public static void checkUpdate(final AbringCallBack<Object, Object> abringCallBack) {
        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {

            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);
            Call<AbringCheckUpdateModel> mEntityCall = mApiService.CheckUpdate("update",
                    Abring.getPackageName());
            //Call<AbringCheckUpdateModel> mEntityCall = mApiService.CheckUpdate("update", "ir.asemanltd.glx");
            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<AbringCheckUpdateModel>() {
                @Override
                public void onResponse(Call<AbringCheckUpdateModel> call, Response<AbringCheckUpdateModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        abringCallBack.onSuccessful(response.body());
                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<AbringCheckUpdateModel> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else
            handleError(Abring.getContext().getString(R.string.abring_no_connect_to_internet),
                    Abring.getContext(),
                    abringCallBack);
    }

    public static void dynamicRequestPost(String url,
                                          Map<String, RequestBody> params,
                                          final AbringCallBack<Object, Object> abringCallBack) {

        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {

            String URL = "http://ws.v3.abring.ir/index.php?r=".concat(url);
            params.put("app", AbringUtils.toRequestBody(Abring.getPackageName()));

            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);
            Call<ResponseBody> mEntityCall = mApiService.DynamicRequestPost(URL, params);
            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        if (response.raw().body() != null) {
                            try {
                                abringCallBack.onSuccessful(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //returns null response
                            abringCallBack.onSuccessful(null);
                        }

                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else
            handleError(Abring.getContext().getString(R.string.abring_no_connect_to_internet),
                    Abring.getContext(),
                    abringCallBack);
    }

    public static void dynamicRequestGet(String url,
                                         Map<String, String> params,
                                         final AbringCallBack<Object, Object> abringCallBack) {

        if (AbringNetworkUtil.isNetworkConnected(Abring.getContext())) {

            params.put("app", Abring.getPackageName()); //set current package name : "ir.iranplays.tootak"
            params.put("r", url); //set url

            AbringAppAPI mApiService = AbringAppService.getInstance().getService(AbringAppAPI.class);
            Call<ResponseBody> mEntityCall = mApiService.DynamicRequestGet(params);
            Log.i("RetrofitURL", "Request URL: " + mEntityCall.request().url().toString());

            mEntityCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        if (response.raw().body() != null) {
                            try {
                                abringCallBack.onSuccessful(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //returns null response
                            abringCallBack.onSuccessful(null);
                        }

                    } else {
                        handleError(response, Abring.getContext(), abringCallBack);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    handleError(t, Abring.getContext(), abringCallBack);
                }
            });

        } else
            handleError(Abring.getContext().getString(R.string.abring_no_connect_to_internet),
                    Abring.getContext(),
                    abringCallBack);
    }

    private static void handleError(Object response, Context context, AbringCallBack<Object, Object> abringCallBack) {
        Object msg = new AbringRetrofitErrorResponce().getMessage(response, context);
        abringCallBack.onFailure(msg);
    }

}
