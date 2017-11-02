package com.reid.smail.net.client;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import reid.utils.AppCompat;
import reid.utils.NetHelper;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by reid on 2017/10/16.
 */

public class ApiClient {

    private ApiClient(){

    }

    private static class Internal{
        private static final ApiClient sInstance = new ApiClient();
    }

    public static ApiClient getInstance(){
        return Internal.sInstance;
    }

    private Map<String, Retrofit> retrofitMap = new ArrayMap<>();
    private OkHttpClient httpClient;

    public void register(String baseUrl){
        if (TextUtils.isDigitsOnly(baseUrl)){
            return;
        }

        Retrofit retrofit = getRetrofit(baseUrl);
        retrofitMap.put(baseUrl, retrofit);
    }

    private Retrofit getRetrofit(String baseUrl) {
        if (retrofitMap.get(baseUrl) != null){
            return retrofitMap.get(baseUrl);
        }

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.build();
        return retrofit;
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

    public <S> S get(String baseUrl, Class<S> service) {
        return getRetrofit(baseUrl).create(service);
    }

}
