package reid.list.load;

import reid.list.R;

/**
 * Created by reid on 2017/11/4.
 */

public class DefaultLoadMoreView extends LoadMoreView {

    @Override
    protected int getLayoutId() {
        return R.layout.layout_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.plastic_load_more_loading_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.plastic_load_more_load_end_view;
    }

}
