package recycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.liangren.lrrecycleview.R;
/**
 * @author yuanzheng
 */
public class AutoLoadMoreRecyclerView extends RecyclerView {
    private static final String TAG = "AutoLoadMoreRecyclerView";
    private static final int WRAP_CONTENT = -1;
    private int mVisibleItemCount, mTotalItemCount, mFirstVisibleItemPosition;
    private LinearLayoutManager mLayoutManager;
    private RecycleListAdapter mAdapter;
    private int mTotalDataCount;
    private boolean mLoadingLock;
    public AutoLoadMoreRecyclerView(Context context) {
        super(context);
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(RecycleListAdapter adapter) {
        this.mAdapter = adapter;
        super.setAdapter(this.mAdapter);
    }

    public void setTotalDataCount(int totalDataCount){
        this.mTotalDataCount=totalDataCount;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        this.mLayoutManager = (LinearLayoutManager) layout;
        super.setLayoutManager(this.mLayoutManager);
    }

    /**
     * set list divider
     *
     * @param dividerRes divider resource
     */
    public void setDivider(int dividerRes) {
        setDivider(dividerRes, WRAP_CONTENT);
    }

    /**
     * set list divider
     *
     * @param dividerRes    divider resource
     * @param dividerHeight divider height
     */
    public void setDivider(int dividerRes, int dividerHeight) {
        Drawable drawable = getResources().getDrawable(dividerRes);
        setDivider(drawable, dividerHeight);
    }

    /**
     * set list divider
     *
     * @param drawable      drawable
     * @param dividerHeight divider height
     */
    public void setDivider(final Drawable drawable, final int dividerHeight) {
        if (null == drawable) {
            throw new NullPointerException("drawable resource is null");
        }
        addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();

                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);

                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                    int top = child.getBottom() + params.bottomMargin;
                    int bottom;
                    if (dividerHeight == WRAP_CONTENT) {
                        bottom = top + drawable.getIntrinsicHeight();
                    } else {
                        if (dividerHeight < 0) {
                            bottom = top;
                        } else {
                            bottom = top + dividerHeight;
                        }

                    }

                    drawable.setBounds(left, top, right, bottom);
                    drawable.draw(c);
                }
            }
        });
    }

    /**
     * set list divider
     *
     * @param drawable drawable
     */
    public void setDivider(Drawable drawable) {
        setDivider(drawable, WRAP_CONTENT);
    }

    /**
     * enable list view auto load more
     *
     * @param loadMoreListener load more listener
     */
    public void enableAutoLoadMore(final RecycleLoadMoreListener loadMoreListener) {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null == mLayoutManager || null == mAdapter || mLoadingLock) {
                    return;
                }
                mVisibleItemCount = mLayoutManager.getChildCount();
                mTotalItemCount = mLayoutManager.getItemCount();
                mFirstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                if ((mVisibleItemCount + mFirstVisibleItemPosition) >= mTotalItemCount) {
                    if (mAdapter.getData().size() < mTotalDataCount && mAdapter.getData().size() > 0) {
                        // has more
                        mLoadingLock = true;
                        if (!mAdapter.getFooters().contains(getResources().getString(R.string.loading))) {
                            mAdapter.addFooter(getResources().getString(R.string.loading));
                        }
                        if (null != loadMoreListener) {
                            loadMoreListener.loadMore();
                        }
                    } else {
                        // no more
                        if (mAdapter.getFooters().contains(getResources().getString(R.string.loading))) {
                            mAdapter.removeFooter(getResources().getString(R.string.loading));
                        }
                    }

                }
            }
        });
    }

    public void loadMoreComplete(){
        mLoadingLock=false;
    }

    public interface RecycleLoadMoreListener {
        void loadMore();
    }
}
