package ir.abring.abringlibrary.network;

import ir.abring.abringlibrary.models.Ping;
import ir.abring.abringlibrary.models.Register;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AppAPI {

    @FormUrlEncoded
    @POST("index.php?r=player/register")
    Call<Register> RegisterAPI(
            @Field("username") String username,
            @Field("password") String password,
            @Field("app_id") String app_id
    );

    @FormUrlEncoded
    @POST("index.php?r=site/ping")
    Call<Ping> PingAPI(
            @Field("app_id") String app_id
    );

}
