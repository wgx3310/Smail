package com.reid.smail.fragment;


import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    protected Handler mMainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onDestroy() {
        if (mMainHandler != null){
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        super.onDestroy();
    }
}
