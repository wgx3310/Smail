package reid.list;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by reid on 2017/11/1.
 */

public interface DecorativeView {
    View getView(ViewGroup parent);

    void bindView(int position);
}
