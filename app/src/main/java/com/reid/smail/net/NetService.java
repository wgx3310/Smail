package com.reid.smail.net;

import com.reid.smail.net.api.AccountApi;
import com.reid.smail.net.api.ShotApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import smail.util.AppCompat;
import smail.util.NetHelper;

/**
 * Created by reid on 2017/8/26.
 */

public class NetService {

    private static final String BASE_URL = "https://api.dribbble.com/v1/";
    private static final String WEBSITE_URL = "https://dribbble.com/";

    public static NetService get(){
        return Internal.instance;
    }

    public static class Internal{
        private static final NetService instance = new NetService();
    }

    private NetService() {}

    private ShotApi shotApi;
    private AccountApi websiteApi;
    private OkHttpClient httpClient;

    public ShotApi getShotApi(){
        if (shotApi == null){
            createShotApi();
        }
        return shotApi;
    }

    private void createShotApi() {
        shotApi = new Retrofit.Builder()
                .client(getHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ShotApi.class);
    }

    public AccountApi getWebsiteApi(){
        if (websiteApi == null){
            createWebsiteApi();
        }

        return websiteApi;
    }

    private void createWebsiteApi(){
        websiteApi = new Retrofit.Builder()
                .client(getHttpClient())
                .baseUrl(WEBSITE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(AccountApi.class);
    }

    private OkHttpClient getHttpClient(){
        if (httpClient != null){
            return httpClient;
        }

        httpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addNetworkInterceptor(getNetworkInterceptor())
                .addInterceptor(getInterceptor())
                .cache(new Cache(AppCompat.getContext().getExternalCacheDir(), 10*1024*1024))
                .build();

        return httpClient;
    }

    private Interceptor getInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetHelper.isConnected()){
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                return chain.proceed(request);
            }
        };
        return interceptor;
    }

    private Interceptor getNetworkInterceptor() {
        Interceptor interceptor = new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (NetHelper.isConnected()){
                    response.newBuilder().header("Cache-Control", "public, max-age=0")
                            .removeHeader("Pragma").build();
                }else {
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=3600")
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        return interceptor;
    }
}
