package reid.utils;

import android.view.View;

/**
 * Created by reid on 2017/11/4.
 */

public class ViewHelper {

    public static void setVisible(View view, int visible){
        if (view != null){
            view.setVisibility(visible);
        }
    }
}
