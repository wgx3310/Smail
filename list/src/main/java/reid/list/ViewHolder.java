package reid.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by reid on 2017/11/1.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public static ViewHolder createViewHolder(View itemView){
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }


}
