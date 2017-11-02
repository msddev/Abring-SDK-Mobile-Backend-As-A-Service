package ir.abring.abringlibrary;

import android.content.Context;

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
        }

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Abring build() {
            return new Abring(this);
        }
    }
}
