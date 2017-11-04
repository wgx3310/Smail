package reid.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reid.list.load.LoadMoreView;

/**
 * Created by reid on 2017/11/1.
 */

class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public static ViewHolder createViewHolder(View itemView){
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    public static ViewHolder createEmptyViewHolder(ViewGroup parent){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_empty_holder, parent, false);
        return new ViewHolder(itemView);
    }

    public static ViewHolder createLoadMoreViewHolder(LoadMoreView view, ViewGroup parent){
        View itemView = view.getView(parent);
        itemView.setTag(view);
        return new ViewHolder(itemView);
    }

    public void bindData(){
        Object tag = itemView.getTag();
        if (tag != null && tag instanceof LoadMoreView){
            ((LoadMoreView) tag).updateStatus();
        }
    }
}
