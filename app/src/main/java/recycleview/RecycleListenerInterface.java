package recycleview;

import android.view.View;

public class RecycleListenerInterface {

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T t);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View view, T t);
    }

    public interface OnEmptyViewClickListener {
        void onEmptyViewClick(View view);
    }

    public interface OnErrorViewClickListener {
        void onErrorViewClick(View view);
    }

    public interface OnHeaderViewClickListener<T> {
        void onHeaderViewClick(View view, T t);
    }

    public interface OnFooterViewClickListener<T> {
        void onFooterViewClick(View view, T t);
    }
}
