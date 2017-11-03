package ir.abring.abringservices;

import android.app.Application;

import ir.abring.abringlibrary.Abring;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Abring.Builder(this)
                .setPackageName("com.masoud.test1")
                .build();
    }
}
