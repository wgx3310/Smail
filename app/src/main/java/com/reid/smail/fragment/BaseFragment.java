package com.reid.smail.fragment;


import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    protected Handler mMainHandler = new Handler(Looper.getMainLooper());

    private CompositeDisposable compositeSubscription = new CompositeDisposable();

    protected void addSubscription(Disposable subscription){
        if (subscription == null){
            return;
        }

        if (compositeSubscription == null){
            compositeSubscription = new CompositeDisposable();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        if (mMainHandler != null){
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (compositeSubscription != null){
            compositeSubscription.dispose();
            compositeSubscription = null;
        }
        super.onDestroy();
    }
}
