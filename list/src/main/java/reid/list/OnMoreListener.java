package reid.list;

/**
 * Created by reid on 2017/10/24.
 */

public interface OnMoreListener {
    void onMore(int totalItemCount, int itemCountToLoadMore, int lastVisibleItemPosition);
}
