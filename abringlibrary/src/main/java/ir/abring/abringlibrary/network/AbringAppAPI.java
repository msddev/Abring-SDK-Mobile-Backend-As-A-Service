package ir.abring.abringlibrary.network;

import ir.abring.abringlibrary.models.AbringPing;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AbringAppAPI {

    @Multipart
    @POST("index.php?r=player/register")
    Call<AbringRegisterModel> RegisterAPI(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part avatar,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("reg_idgcm") RequestBody reg_idgcm,
            @Part("app") RequestBody app_id
    );

    @Multipart
    @POST("index.php?r=player/mobile-register")
    Call<Void> MobileRegisterAPI(
            @Part("mobile") RequestBody mobile,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("device_id") RequestBody deviceId,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part avatar,
            @Part("app") RequestBody app_id
    );

    @FormUrlEncoded
    @POST("index.php?r=player/mobile-verify")
    Call<AbringRegisterModel> MobileVerifyAPI(
            @Field("mobile") String mobile,
            @Field("code") String code,
            @Field("app") String app_id
    );

    @FormUrlEncoded
    @POST("index.php?r=player/mobile-resend-code")
    Call<Void> MobileResendCodeAPI(
            @Field("mobile") String mobile,
            @Field("app") String app_id
    );

    @FormUrlEncoded
    @POST("index.php?r=player/login")
    Call<AbringRegisterModel> LoginAPI(
            @Field("username") String username,
            @Field("password") String password,
            @Field("app") String app_id
    );

    @FormUrlEncoded
    @POST("index.php?r=player/login-as-guest")
    Call<AbringRegisterModel> LoginAsGuestAPI(
            @Field("device_id") String deviceId,
            @Field("app") String app_id
    );

    @FormUrlEncoded
    @POST("index.php?r=site/ping")
    Call<AbringPing> PingAPI(
            @Field("app") String app_id
    );

}
