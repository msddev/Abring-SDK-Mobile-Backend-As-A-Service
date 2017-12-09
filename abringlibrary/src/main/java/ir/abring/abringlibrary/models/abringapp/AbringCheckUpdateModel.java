package ir.abring.abringlibrary.models.abringapp;

import android.os.Parcel;
import android.os.Parcelable;

public class AbringCheckUpdateModel implements Parcelable {

    private String code;
    private ResultBean result;
    private String message;
    private String timestamp;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class ResultBean implements Parcelable {
        private AndroidBean android;
        private AndroidBean ios;

        public AndroidBean getAndroid() {
            return android;
        }

        public void setAndroid(AndroidBean android) {
            this.android = android;
        }

        public AndroidBean getIos() {
            return ios;
        }

        public void setIos(AndroidBean ios) {
            this.ios = ios;
        }

        public static class AndroidBean implements Parcelable {
            private String current;
            private String force;
            private String link;

            public String getCurrent() {
                return current;
            }

            public void setCurrent(String current) {
                this.current = current;
            }

            public String getForce() {
                return force;
            }

            public void setForce(String force) {
                this.force = force;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.current);
                dest.writeString(this.force);
                dest.writeString(this.link);
            }

            public AndroidBean() {
            }

            protected AndroidBean(Parcel in) {
                this.current = in.readString();
                this.force = in.readString();
                this.link = in.readString();
            }

            public static final Creator<AndroidBean> CREATOR = new Creator<AndroidBean>() {
                @Override
                public AndroidBean createFromParcel(Parcel source) {
                    return new AndroidBean(source);
                }

                @Override
                public AndroidBean[] newArray(int size) {
                    return new AndroidBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.android, flags);
            dest.writeParcelable(this.ios, flags);
        }

        public ResultBean() {
        }

        protected ResultBean(Parcel in) {
            this.android = in.readParcelable(AndroidBean.class.getClassLoader());
            this.ios = in.readParcelable(AndroidBean.class.getClassLoader());
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel source) {
                return new ResultBean(source);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeParcelable(this.result, flags);
        dest.writeString(this.message);
        dest.writeString(this.timestamp);
    }

    public AbringCheckUpdateModel() {
    }

    protected AbringCheckUpdateModel(Parcel in) {
        this.code = in.readString();
        this.result = in.readParcelable(ResultBean.class.getClassLoader());
        this.message = in.readString();
        this.timestamp = in.readString();
    }

    public static final Creator<AbringCheckUpdateModel> CREATOR = new Creator<AbringCheckUpdateModel>() {
        @Override
        public AbringCheckUpdateModel createFromParcel(Parcel source) {
            return new AbringCheckUpdateModel(source);
        }

        @Override
        public AbringCheckUpdateModel[] newArray(int size) {
            return new AbringCheckUpdateModel[size];
        }
    };
}
