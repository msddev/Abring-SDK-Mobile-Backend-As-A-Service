package ir.abring.abringlibrary.abringclass.app;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringapp.AbringCheckUpdateModel;
import ir.abring.abringlibrary.services.AbringAppServices;
import okhttp3.RequestBody;

public class AbringDynamicRequest {

    private String method;                              //required
    private String url;                                 //required
    private Map<String, RequestBody> parametersPost;    //required
    private Map<String, String> parametersGet;          //required

    AbringDynamicRequest(DynamicRequestGetBuilder mBuilder) {
        this.method = mBuilder.method;
        this.url = mBuilder.url;
        this.parametersGet = mBuilder.parameters;
    }

    AbringDynamicRequest(DynamicRequestPostBuilder mBuilder) {
        this.method = mBuilder.method;
        this.url = mBuilder.url;
        this.parametersPost = mBuilder.parameters;
    }

    public static class DynamicRequestPostBuilder {
        private String method = "POST";
        private String url;
        private Map<String, RequestBody> parameters;

        public DynamicRequestPostBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public DynamicRequestPostBuilder setParameters(Map<String, RequestBody> parameters) {
            this.parameters = parameters;
            return this;
        }

        public AbringDynamicRequest build() {
            return new AbringDynamicRequest(this);
        }
    }

    public static class DynamicRequestGetBuilder {
        private String method = "GET";
        private String url;
        private Map<String, String> parameters;

        public DynamicRequestGetBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        public DynamicRequestGetBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public DynamicRequestGetBuilder setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
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
                            parametersPost,
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
                            parametersGet,
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
