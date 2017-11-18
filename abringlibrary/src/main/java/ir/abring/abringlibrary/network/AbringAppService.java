package ir.abring.abringlibrary.network;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import ir.abring.abringlibrary.Abring;
import ir.abring.abringlibrary.AbringConstant;
import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringlibrary.utils.AbringFileUtil;
import ir.abring.abringlibrary.utils.AbringNetworkUtil;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AbringAppService {

    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private static final int MAX_AGE = 60 * 10; //with network 10min
    private static final int MAX_STALE = 60 * 60 * 24; //1 day ,no network

    private static final String WEB_SERVICE_BASE_URL = "http://ws.v3.abring.ir/";

    private volatile static AbringAppService sAppClient;
    private Map<String, Object> serviceByType = new HashMap<>();
    private Retrofit mRetrofit;

    private AbringAppService() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        ExecutorService exec = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setDaemon(false);
                thread.setPriority(Thread.MAX_PRIORITY);
                return thread;
            }
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(cacheInterceptor())
                .cache(cache())
                .build();

        mRetrofit = new Retrofit
                .Builder()
                .baseUrl(WEB_SERVICE_BASE_URL)
                .callbackExecutor(exec)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Cache cache() {
        final File baseDir = AbringFileUtil.getExternalCacheDir(Abring.getContext());
        final File cacheDir = new File(baseDir, "HttpResponseCache");
        return (new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));

    }

    private Interceptor cacheInterceptor() {

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                if (!AbringNetworkUtil.isAvailable(Abring.getContext())) {
                    original = original.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                }
                Request.Builder builder = original.newBuilder();

                String token = Hawk.get(AbringConstant.ABRING_TOKEN, null);
                if (token != null)
                    builder.header("Authorization", token);

                Request request = builder.method(original.method(), original.body())
                        .build();
                Log.i("request", request.url().toString());
                if (request.header("Authorization") != null)
                    Log.i("header", request.header("Authorization"));

                return chain.proceed(request);
            }
        };
    }

    public static AbringAppService getInstance() {
        synchronized (AbringAppService.class) {
            if (sAppClient == null) {
                sAppClient = new AbringAppService();
            }
        }
        return sAppClient;
    }

    public synchronized <T> T getService(Class<T> apiInterface) {
        String serviceName = apiInterface.getName();
        if (!AbringCheck.isNull(serviceByType.get(serviceName))) {
            return (T) serviceByType.get(serviceName);
        }
        T service = mRetrofit.create(apiInterface);
        serviceByType.put(serviceName, service);
        return service;
    }

}
