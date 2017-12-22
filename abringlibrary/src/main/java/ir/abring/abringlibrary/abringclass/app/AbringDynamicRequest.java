package ir.abring.abringlibrary.abringclass.app;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringapp.AbringCheckUpdateModel;
import ir.abring.abringlibrary.services.AbringAppServices;

public class AbringDynamicRequest {

    private String url;    //required
    private Map<String, String> parameters;    //required

    AbringDynamicRequest(DynamicRequestBuilder mBuilder) {
        this.url = mBuilder.url;
        this.parameters = mBuilder.parameters;
    }

    public static class DynamicRequestBuilder {
        private String url;    //required
        private Map<String, String> parameters;    //required

        public DynamicRequestBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public DynamicRequestBuilder setParameters(Map<String, String> parameters) {
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

                AbringAppServices.dynamicRequest(url,
                        parameters,
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
        }).start();
    }

}
