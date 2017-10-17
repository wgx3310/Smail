package com.reid.smail.fragment;


import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    protected Handler mMainHandler = new Handler(Looper.getMainLooper());

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    protected void addSubscription(Subscription subscription){
        if (subscription == null){
            return;
        }

        if (compositeSubscription == null){
            compositeSubscription = new CompositeSubscription();
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
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
        super.onDestroy();
    }
}
