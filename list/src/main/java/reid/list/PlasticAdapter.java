package reid.list;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by reid on 2017/11/1.
 */

public class PlasticAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM_TYPE_HEADER = 100000;
    private static final int ITEM_TYPE_FOOTER = 200000;

    private SparseArray<DecorativeView> mHeaderViews = new SparseArray<>();
    private SparseArray<DecorativeView> mFooterViews = new SparseArray<>();

    private RecyclerView.Adapter mAdapter;
    private PlasticAdapterObserver mObserver = new PlasticAdapterObserver();

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
            notifyItemRangeChanged(positionStart+mHeaderViews.size(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart+mHeaderViews.size(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart+mHeaderViews.size(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            notifyItemMoved(fromPosition + mHeaderViews.size(), toPosition + mHeaderViews.size());
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
        } else if (mAdapter != null){
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)){
            mHeaderViews.get(ITEM_TYPE_HEADER + position).bindView(position);
        }else if (isFooterView(position)){
            int index = position - mHeaderViews.size() - getRealItemCount();
            mFooterViews.get(ITEM_TYPE_FOOTER + index).bindView(index);
        }else if (mAdapter != null){
            mAdapter.onBindViewHolder(holder, position - mHeaderViews.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)){
            return ITEM_TYPE_HEADER + position;
        }else if (isFooterView(position)){
            return ITEM_TYPE_FOOTER + position - mHeaderViews.size() - getRealItemCount();
        }else if (mAdapter != null){
            return mAdapter.getItemViewType(position-mHeaderViews.size());
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int realItemCount = mAdapter != null?mAdapter.getItemCount():0;
        int headerCount = mHeaderViews.size();
        int footerCount = mFooterViews.size();
        return realItemCount + headerCount + footerCount;
    }

    private int getRealItemCount(){
        return mAdapter != null?mAdapter.getItemCount():0;
    }

    private boolean isHeaderView(int position){
        return position < mHeaderViews.size();
    }

    private boolean isFooterView(int position){
        return position >= mHeaderViews.size() + getRealItemCount();
    }

    public void addHeaderView(DecorativeView headerView){
        if (headerView == null){
            return;
        }

        mHeaderViews.put(ITEM_TYPE_HEADER + mHeaderViews.size(), headerView);
        notifyDataSetChanged();
    }

    public void addFooterView(DecorativeView footerView){
        if (footerView == null){
            return;
        }

        mFooterViews.put(ITEM_TYPE_FOOTER + mFooterViews.size(), footerView);
        notifyDataSetChanged();
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
                    int viewType = getItemViewType(position);
                    if (mHeaderViews.get(viewType) != null){
                        return manager.getSpanCount();
                    } else if (mFooterViews.get(viewType) != null){
                        return manager.getSpanCount();
                    }
                    if (spanSizeLookup != null){
                        return spanSizeLookup.getSpanSize(position - mHeaderViews.size());
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
        if (isHeaderView(position) || isFooterView(position)){
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
        super.onViewDetachedFromWindow(holder);
        int position = holder.getLayoutPosition();
        if (!isHeaderView(position) && !isFooterView(position) && mAdapter != null){
            mAdapter.onViewDetachedFromWindow(holder);
        }
    }
}
