package reid.list;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reid on 2017/10/25.
 */

public class PlasticView extends FrameLayout {

    private static final int DEFAULT_ITEM_COUNT_TO_LOAD_MORE = 6;

    private static final int LAYOUT_TYPE_LINEAR = 1;
    private static final int LAYOUT_TYPE_GRID = 2;
    private static final int LAYOUT_TYPE_STAGGERED = 3;

    private SwipeRefreshLayout mPtrLayout;
    private RecyclerView mRecyclerView;
    protected ViewStub mProgress;
    protected ViewStub mMoreLoading;
    protected ViewStub mEmpty;

    protected View mProgressView;
    protected View mMoreLoadingView;
    protected View mEmptyView;

    protected boolean mRefreshable;
    protected boolean mClipToPadding;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mScrollbarStyle;
    protected int mEmptyId;
    protected int mProgressId;
    protected int mMoreLoadingId;

    private RecyclerView.OnScrollListener mInternalScrollListener;
    private List<RecyclerView.OnScrollListener> mScrollListeners;

    private OnMoreListener mOnMoreListener;
    private boolean isLoadingMore;
    private int mItemCountToLoadMore = DEFAULT_ITEM_COUNT_TO_LOAD_MORE;

    private int mLayoutManagerType = 0;

    public PlasticView(@NonNull Context context) {
        this(context, null);
    }

    public PlasticView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlasticView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.PlasticView);
        try {
            mRefreshable = array.getBoolean(R.styleable.PlasticView_refreshable, false);
            mEmptyId = array.getResourceId(R.styleable.PlasticView_layout_empty, 0);
            mProgressId = array.getResourceId(R.styleable.PlasticView_layout_progress, R.layout.layout_progress);
            mMoreLoadingId = array.getResourceId(R.styleable.PlasticView_layout_more_loading, R.layout.layout_more_loading);
            mClipToPadding = array.getBoolean(R.styleable.PlasticView_recyclerClipToPadding, false);
            mPadding = (int) array.getDimension(R.styleable.PlasticView_recyclerPadding, -1.0f);
            mPaddingLeft = (int) array.getDimension(R.styleable.PlasticView_recyclerPaddingLeft, 0.0f);
            mPaddingTop = (int) array.getDimension(R.styleable.PlasticView_recyclerPaddingTop, 0.0f);
            mPaddingRight = (int) array.getDimension(R.styleable.PlasticView_recyclerPaddingRight, 0.0f);
            mPaddingBottom = (int) array.getDimension(R.styleable.PlasticView_recyclerPaddingBottom, 0.0f);
            mScrollbarStyle = array.getInt(R.styleable.PlasticView_recyclerScrollbarStyle, -1);
        } finally {
            array.recycle();
        }
    }

    private void init() {
        View inflate = View.inflate(getContext(), R.layout.layout_plastic, this);
        mPtrLayout = inflate.findViewById(R.id.ptr_layout);
        mPtrLayout.setEnabled(mRefreshable);

        mProgress = inflate.findViewById(android.R.id.progress);
        mProgress.setLayoutResource(mProgressId);
        mProgressView = mProgress.inflate();

        mMoreLoading = inflate.findViewById(R.id.more_loading);
        mMoreLoading.setLayoutResource(mMoreLoadingId);
        if (mMoreLoadingId != 0){
            mMoreLoadingView = mMoreLoading.inflate();
        }
        mMoreLoading.setVisibility(View.GONE);

        mEmpty = inflate.findViewById(R.id.empty);
        mEmpty.setLayoutResource(mEmptyId);
        if (mEmptyId != 0){
            mEmptyView = mEmpty.inflate();
        }
        mEmpty.setVisibility(View.GONE);

        initRecycler(inflate);
    }

    private void initRecycler(View inflate) {
        mRecyclerView = inflate.findViewById(R.id.recycler);
        mRecyclerView.setClipToPadding(mClipToPadding);

        if (mPadding != -1.0f){
            mRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
        }else {
            mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        }

        if (mScrollbarStyle != -1){
            mRecyclerView.setScrollBarStyle(mScrollbarStyle);
        }

        mInternalScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mScrollListeners != null && mScrollListeners.size() > 0){
                    for (RecyclerView.OnScrollListener listener : mScrollListeners){
                        listener.onScrollStateChanged(recyclerView, newState);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                processOnLoadMore();

                if (mScrollListeners != null && mScrollListeners.size() > 0){
                    for (RecyclerView.OnScrollListener listener : mScrollListeners){
                        listener.onScrolled(recyclerView, dx, dy);
                    }
                }
            }
        };
        mRecyclerView.addOnScrollListener(mInternalScrollListener);
    }

    private void processOnLoadMore() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager);
        int totalItemCount = layoutManager.getItemCount();

        if (!isLoadingMore() && getOnMoreListener() != null
                && (totalItemCount - lastVisibleItemPosition) <= mItemCountToLoadMore){
            isLoadingMore = true;
            if (mMoreLoading != null){
                mMoreLoading.setVisibility(View.VISIBLE);
            }
            mOnMoreListener.onMore(totalItemCount, mItemCountToLoadMore, lastVisibleItemPosition);
        }
    }

    private int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition = -1;
        if (layoutManager == null) return lastVisibleItemPosition;

        if (mLayoutManagerType == 0){
            if (layoutManager instanceof GridLayoutManager){
                mLayoutManagerType = LAYOUT_TYPE_GRID;
            }else if (layoutManager instanceof LinearLayoutManager){
                mLayoutManagerType = LAYOUT_TYPE_LINEAR;
            }else if (layoutManager instanceof StaggeredGridLayoutManager){
                mLayoutManagerType = LAYOUT_TYPE_STAGGERED;
            }
        }

        switch (mLayoutManagerType){
            case LAYOUT_TYPE_LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case LAYOUT_TYPE_GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case LAYOUT_TYPE_STAGGERED:
                lastVisibleItemPosition = calculateStaggeredGrid(layoutManager);
                break;
        }

        return lastVisibleItemPosition;
    }

    private int calculateStaggeredGrid(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager == null) return -1;

        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        int[] lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];
        staggeredGridLayoutManager.findLastVisibleItemPositions(lastScrollPositions);
        return findMax(lastScrollPositions);
    }

    private int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max)
                max = value;
        }
        return max;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapterInternal(adapter, false, true);
    }

    public void swapAdapter(RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews) {
        setAdapterInternal(adapter, true, removeAndRecycleExistingViews);
    }

    private final void setAdapterInternal(RecyclerView.Adapter adapter, boolean compatibleWithPrevious,
                                    boolean removeAndRecycleExistingViews) {
        if (compatibleWithPrevious)
            mRecyclerView.swapAdapter(adapter, removeAndRecycleExistingViews);
        else
            mRecyclerView.setAdapter(adapter);

        mProgress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mPtrLayout.setRefreshing(false);
        if (null != adapter)
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    super.onItemRangeChanged(positionStart, itemCount);
                    update();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    update();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    update();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                    update();
                }

                @Override
                public void onChanged() {
                    super.onChanged();
                    update();
                }

                private void update() {
                    mProgress.setVisibility(View.GONE);
                    mMoreLoading.setVisibility(View.GONE);
                    isLoadingMore = false;
                    mPtrLayout.setRefreshing(false);
                    if (mRecyclerView.getAdapter().getItemCount() == 0 && mEmptyId != 0) {
                        mEmpty.setVisibility(View.VISIBLE);
                    } else if (mEmptyId != 0) {
                        mEmpty.setVisibility(View.GONE);
                    }
                }
            });

        if (mEmptyId != 0) {
            mEmpty.setVisibility(null != adapter && adapter.getItemCount() > 0
                    ? View.GONE
                    : View.VISIBLE);
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecyclerView.setLayoutManager(manager);
    }

    public void clear() {
        mRecyclerView.setAdapter(null);
    }

    public void showProgress() {
        hideRecycler();
        if (mEmptyId != 0) mEmpty.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    public void showRecycler() {
        hideProgress();
        if (mRecyclerView.getAdapter().getItemCount() == 0 && mEmptyId != 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else if (mEmptyId != 0) {
            mEmpty.setVisibility(View.GONE);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void hideRecycler() {
        mRecyclerView.setVisibility(View.GONE);
    }

    public void showMoreLoading() {
        mMoreLoading.setVisibility(View.VISIBLE);
    }

    public void hideMoreLoading() {
        mMoreLoading.setVisibility(View.GONE);
    }

    public void setRefreshing(boolean refreshing) {
        mPtrLayout.setRefreshing(refreshing);
    }

    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mPtrLayout.setEnabled(listener != null?true:false);
        mPtrLayout.setOnRefreshListener(listener);
    }

    public void setRefreshingColorResources(@ColorRes int colRes1, @ColorRes int colRes2, @ColorRes int colRes3, @ColorRes int colRes4) {
        mPtrLayout.setColorSchemeResources(colRes1, colRes2, colRes3, colRes4);
    }

    public void setRefreshingColor(int col1, int col2, int col3, int col4) {
        mPtrLayout.setColorSchemeColors(col1, col2, col3, col4);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener){
        if (mScrollListeners == null){
            mScrollListeners = new ArrayList<>();
        }
        mScrollListeners.add(listener);
    }

    public void removeOnSrcollListener(RecyclerView.OnScrollListener listener){
        if (mScrollListeners != null){
            mScrollListeners.remove(listener);
        }
    }

    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

    /**
     * Remove the onItemTouchListener for the recycler
     */
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.removeOnItemTouchListener(listener);
    }

    public OnMoreListener getOnMoreListener() {
        return mOnMoreListener;
    }

    public void setOnMoreListener(OnMoreListener mOnMoreListener) {
        this.mOnMoreListener = mOnMoreListener;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void setLoadingMore(boolean isLoadingMore) {
        this.isLoadingMore = isLoadingMore;
    }

    public int getItemCountToLoadMore() {
        return mItemCountToLoadMore;
    }

    public void setItemCountToLoadMore(int itemCountToLoadMore) {
        this.mItemCountToLoadMore = itemCountToLoadMore;
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mRecyclerView.setOnTouchListener(listener);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecyclerView.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.removeItemDecoration(itemDecoration);
    }

    public void smoothScrollBy(int dx, int dy) {
        mRecyclerView.smoothScrollBy(dx, dy);
    }
}
