package com.reid.smail.net.client;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    public abstract String getBaseUrl();

    //指定观察者与被观察者线程
    protected <T> Observable.Transformer<T, T> transformer() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> source) {
                return source.onTerminateDetach().subscribeOn(Schedulers.io())
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
