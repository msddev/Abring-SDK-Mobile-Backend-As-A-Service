package ir.abring.abringlibrary;

import android.content.Context;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.LogInterceptor;

public class Abring {

    private static Context context;
    private static String packageName;

    Abring(Builder builder) {
        packageName = builder.packageName;
        context = builder.mContext;
    }

    public static Context getContext() {
        return context;
    }

    public static String getPackageName() {
        return packageName;
    }

    public static class Builder {
        private String packageName;
        private Context mContext;

        public Builder(Context context) {
            mContext = context;

            //setup SharePreferences
            setupHawk();
        }

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Abring build() {
            return new Abring(this);
        }

        private void setupHawk() {
            long startTime = System.currentTimeMillis();

            Hawk.init(mContext).setLogInterceptor(new LogInterceptor() {
                @Override public void onLog(String message) {
                    Log.i("HAWK", message);
                }
            }).build();

            long endTime = System.currentTimeMillis();
            System.out.println("Hawk.init: " + (endTime - startTime) + "ms");
        }
    }
}
