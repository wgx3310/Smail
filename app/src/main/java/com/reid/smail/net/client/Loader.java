package com.reid.smail.net.client;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by reid on 2017/10/16.
 */

public abstract class Loader<T> {

    protected T impl;

    protected T Impl(){
        if (impl == null){
            impl = ApiClient.getInstance().get(getBaseUrl(), getType());
        }
        return impl;
    }

    protected abstract String getBaseUrl();

    //指定观察者与被观察者线程
    protected <T> ObservableTransformer<T, T> transformer(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.onTerminateDetach().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private Class<T> getType() {
        Class<T> entityClass = null;
        Type t = getClass().getGenericSuperclass();
        Type[] p = ((ParameterizedType) t).getActualTypeArguments();
        entityClass = (Class<T>) p[0];
        return entityClass;
    }
}
