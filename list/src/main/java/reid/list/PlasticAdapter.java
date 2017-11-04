package reid.list;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import reid.list.load.DefaultLoadMoreView;
import reid.list.load.LoadMoreView;

/**
 * Created by reid on 2017/11/1.
 */

public class PlasticAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM_TYPE_HEADER = 100000;
    private static final int ITEM_TYPE_FOOTER = 200000;
    private static final int ITEM_TYPE_LOADING = 300000;

    private SparseArray<DecorativeView> mHeaderViews = new SparseArray<>();
    private SparseArray<DecorativeView> mFooterViews = new SparseArray<>();

    private RecyclerView.Adapter mAdapter;
    private PlasticAdapterObserver mObserver = new PlasticAdapterObserver();
    private PlasticView mPlasticView;

    private LoadMoreView mLoadMoreView = new DefaultLoadMoreView();

    public PlasticAdapter(RecyclerView.Adapter adapter){
        if (mAdapter != null){
            mAdapter.unregisterAdapterDataObserver(mObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerAdapterDataObserver(mObserver);
        }
    }

    private class PlasticAdapterObserver extends RecyclerView.AdapterDataObserver{
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart+getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart+getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart+getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            notifyItemMoved(fromPosition + getHeaderViewCount(), toPosition + getHeaderViewCount());
        }

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null){
            View view = mHeaderViews.get(viewType).getView(parent);
            if (view != null){
                return ViewHolder.createViewHolder(view);
            }
        } else if (mFooterViews.get(viewType) != null){
            View view = mFooterViews.get(viewType).getView(parent);
            if (view != null){
                return ViewHolder.createViewHolder(view);
            }
        } else if (viewType  == ITEM_TYPE_LOADING){
            return ViewHolder.createLoadMoreViewHolder(mLoadMoreView, parent);
        } else if (mAdapter != null){
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
        return ViewHolder.createEmptyViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)){
            mHeaderViews.get(ITEM_TYPE_HEADER + position).bindView(position);
        }else if (isFooterView(position)){
            int index = position - getHeaderViewCount() - getRealItemCount();
            mFooterViews.get(ITEM_TYPE_FOOTER + index).bindView(index);
        }else if (isLoadMoreViewPosition(position)){
            ((ViewHolder)holder).bindData();
        } else if (mAdapter != null){
            mAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreViewPosition(position)){
            return ITEM_TYPE_LOADING;
        }else if (isHeaderView(position)){
            return ITEM_TYPE_HEADER + position;
        }else if (isFooterView(position)){
            return ITEM_TYPE_FOOTER + position - getHeaderViewCount() - getRealItemCount();
        }else if (mAdapter != null){
            return mAdapter.getItemViewType(position-getHeaderViewCount());
        }
        return super.getItemViewType(position);
    }

    private boolean isLoadMoreViewPosition(int position){
        if (getLoadMoreViewCount() == 0){
            return false;
        }

        if (position == getHeaderViewCount() + getRealItemCount()
                + getFooterViewCount()){
            return true;
        }

        return false;
    }

    @Override
    public int getItemCount() {
        return getRealItemCount() + getHeaderViewCount()
                + getFooterViewCount() + getLoadMoreViewCount();
    }

    private int getRealItemCount(){
        return mAdapter != null?mAdapter.getItemCount():0;
    }

    private boolean isHeaderView(int position){
        return mHeaderViews != null?mHeaderViews.get(ITEM_TYPE_HEADER + position) != null:false;
    }

    private boolean isFooterView(int position){
        int index = position - getHeaderViewCount() - getRealItemCount();
        return mFooterViews != null?mFooterViews.get(ITEM_TYPE_FOOTER + index) != null : false;
    }

    public int getHeaderViewCount(){
        if (mHeaderViews != null){
            return mHeaderViews.size();
        }
        return 0;
    }

    public int getFooterViewCount(){
        if (mFooterViews != null){
            return mFooterViews.size();
        }
        return 0;
    }

    public int getLoadMoreViewPosition() {
        return getHeaderViewCount() + getRealItemCount() + getFooterViewCount();
    }

    public int getLoadMoreViewCount(){
        if (mPlasticView == null || !mPlasticView.isLoadMoreEnable()
                || mPlasticView.getOnMoreListener() == null){
            return 0;
        }

        if (getRealItemCount() == 0 || mLoadMoreView.isLoadMoreEndGone()){
            return 0;
        }

        return 1;
    }

    boolean canLoadMore(){
        return mLoadMoreView.canLoadMore();
    }

    boolean isLoadingMore(){
        return mLoadMoreView.isLoading();
    }

    void setLoadMoreComplete(boolean notify){
        if (getLoadMoreViewCount() == 0) return;
        mLoadMoreView.setLoadComplete();
        if (notify) notifyItemChanged(getLoadMoreViewPosition());
    }

    void setLoadMoreEnd(boolean gone){
        if (getLoadMoreViewCount() == 0) return;
        if (gone){
            mLoadMoreView.setLoadEnd();
            mLoadMoreView.setLoadMoreEndGone(true);
            notifyItemRemoved(getLoadMoreViewPosition());
        } else {
            mLoadMoreView.setLoadEnd();
            notifyItemChanged(getLoadMoreViewPosition());
        }
    }

    void setLoadingMore(){
        if (getLoadMoreViewCount() == 0) return;
        mLoadMoreView.setLoading();
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public void addHeaderView(DecorativeView headerView){
        if (headerView == null){
            return;
        }

        mHeaderViews.put(ITEM_TYPE_HEADER + getHeaderViewCount(), headerView);
        notifyDataSetChanged();
    }

    public void addFooterView(DecorativeView footerView){
        if (footerView == null){
            return;
        }

        mFooterViews.put(ITEM_TYPE_FOOTER + getFooterViewCount(), footerView);
        notifyDataSetChanged();
    }

    void onAttachedToPlasticView(PlasticView plasticView){
        mPlasticView = plasticView;
    }

    void onDetachedFromPlasticView(PlasticView plasticView){
        mPlasticView = null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mAdapter != null){
            mAdapter.onAttachedToRecyclerView(recyclerView);
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = manager.getSpanSizeLookup();
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHeaderView(position) || isFooterView(position) || isLoadMoreViewPosition(position)){
                        return manager.getSpanCount();
                    }else if (spanSizeLookup != null){
                        return spanSizeLookup.getSpanSize(position - getHeaderViewCount());
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mAdapter != null){
            mAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderView(position) || isFooterView(position) || isLoadMoreViewPosition(position)){
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }else if (mAdapter != null){
            mAdapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (!(holder instanceof ViewHolder)){
            mAdapter.onViewDetachedFromWindow(holder);
        }
    }
}
