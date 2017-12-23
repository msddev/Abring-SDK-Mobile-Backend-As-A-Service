package ir.abring.abringlibrary.abringclass.app;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringapp.AbringCheckUpdateModel;
import ir.abring.abringlibrary.services.AbringAppServices;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AbringDynamicRequest {

    private String method;                                     //required
    private String url;                                        //required
    private List<MultipartBody.Part> imageParamsPost;          //required
    private Map<String, String> stringParamsGet;               //required
    private Map<String, RequestBody> requestBodyParamsPost;    //required

    AbringDynamicRequest(DynamicRequestGetBuilder mBuilder) {
        this.method = mBuilder.method;
        this.url = mBuilder.url;
        this.stringParamsGet = mBuilder.stringParamsGet;
    }

    AbringDynamicRequest(DynamicRequestPostBuilder mBuilder) {
        this.method = mBuilder.method;
        this.url = mBuilder.url;
        this.requestBodyParamsPost = mBuilder.requestBodyParamsPost;
        this.imageParamsPost = mBuilder.imageParamsPost;
    }

    public static class DynamicRequestGetBuilder {
        private String method = "GET";
        private String url;
        private Map<String, String> stringParamsGet;

        public DynamicRequestGetBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public DynamicRequestGetBuilder setStringParamsGet(Map<String, String> stringParamsGet) {
            this.stringParamsGet = stringParamsGet;
            return this;
        }

        public AbringDynamicRequest build() {
            return new AbringDynamicRequest(this);
        }
    }

    public static class DynamicRequestPostBuilder {
        private String method = "POST";
        private String url;
        private Map<String, RequestBody> requestBodyParamsPost;
        private List<MultipartBody.Part> imageParamsPost;

        public DynamicRequestPostBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public DynamicRequestPostBuilder setRequestBodyParamsPost(Map<String, RequestBody> requestBodyParamsPost) {
            this.requestBodyParamsPost = requestBodyParamsPost;
            return this;
        }

        public DynamicRequestPostBuilder setImageParamsPost(List<MultipartBody.Part> imageParamsPost) {
            this.imageParamsPost = imageParamsPost;
            return this;
        }

        public AbringDynamicRequest build() {
            return new AbringDynamicRequest(this);
        }
    }

    public void request(final Activity mActivity, final AbringCallBack abringCallBack) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (method.equals("POST")) {

                    AbringAppServices.dynamicRequestPost(url,
                            requestBodyParamsPost,
                            imageParamsPost,
                            new AbringCallBack<Object, Object>() {
                                @Override
                                public void onSuccessful(final Object response) {

                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
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

                } else if (method.equals("GET")) {

                    AbringAppServices.dynamicRequestGet(url,
                            stringParamsGet,
                            new AbringCallBack<Object, Object>() {
                                @Override
                                public void onSuccessful(final Object response) {

                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
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

            }
        }).start();
    }

}
