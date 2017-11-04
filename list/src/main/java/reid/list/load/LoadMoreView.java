package reid.list.load;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reid.utils.ViewHelper;

/**
 * Created by reid on 2017/11/4.
 */

public abstract class LoadMoreView {

    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_END = 3;

    private View mView;
    private View mLoadingView;
    private View mLoadEndView;
    private View mLoadFailView;

    private int mCurStatus = STATUS_DEFAULT;
    private boolean mLoadMoreEndGone = false;

    public View getView(ViewGroup parent){

        mView = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        if (mView != null){
            mLoadingView = mView.findViewById(getLoadingViewId());
            mLoadEndView = mView.findViewById(getLoadEndViewId());
        }
        return mView;
    }

    public boolean isLoading(){
        return mCurStatus == STATUS_LOADING;
    }

    public boolean canLoadMore(){
        return mCurStatus == STATUS_DEFAULT;
    }

    public void setLoading(){
        if (mView == null) return;
        mCurStatus = STATUS_LOADING;
    }

    public void setLoadComplete(){
        if (mView == null) return;
        mCurStatus = STATUS_DEFAULT;
    }

    public void setLoadEnd(){
        if (mView == null) return;
        mCurStatus = STATUS_END;
    }

    public void updateStatus(){
        switch (mCurStatus){
            case STATUS_DEFAULT:
                updateViewStatue(View.GONE, View.GONE, View.GONE);
                break;
            case STATUS_LOADING:
                updateViewStatue(View.VISIBLE, View.GONE, View.VISIBLE);
                break;
            case STATUS_END:
                updateViewStatue(View.GONE, View.VISIBLE, View.VISIBLE);
                break;
            default:
                updateViewStatue(View.GONE, View.GONE, View.GONE);
                break;
        }
    }

    private void updateViewStatue(int loadingVisible, int loadEndVisible, int viewVisible){
        ViewHelper.setVisible(mLoadingView, loadingVisible);
        ViewHelper.setVisible(mLoadEndView, loadEndVisible);
        ViewHelper.setVisible(mView,viewVisible);
    }

    public final void setLoadMoreEndGone(boolean loadMoreEndGone) {
        this.mLoadMoreEndGone = loadMoreEndGone;
    }

    public final boolean isLoadMoreEndGone() {
        if (getLoadEndViewId() == 0) return true;
        return mLoadMoreEndGone;
    }

    protected abstract int getLayoutId();

    protected abstract int getLoadingViewId();

    protected abstract int getLoadEndViewId();

}
