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

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void addDisposable(Disposable disposable){
        if (disposable == null){
            return;
        }

        if (compositeDisposable == null){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        if (mMainHandler != null){
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (compositeDisposable != null){
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        super.onDestroy();
    }
}
