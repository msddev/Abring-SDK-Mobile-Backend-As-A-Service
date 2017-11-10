package ir.abring.abringlibrary.network;

import ir.abring.abringlibrary.models.AbringPing;
import ir.abring.abringlibrary.models.abringregister.AbringRegister;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AbringAppAPI {

    @FormUrlEncoded
    @POST("index.php?r=player/register")
    Call<AbringRegister> RegisterAPI(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name,
            @Field("avatar") String avatar,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("reg_idgcm") String reg_idgcm,
            @Field("app_id") String app_id
    );

    @FormUrlEncoded
    @POST("index.php?r=site/ping")
    Call<AbringPing> PingAPI(
            @Field("app_id") String app_id
    );

}
