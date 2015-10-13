package com.liangren.lrrecycleview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;

import recycleview.AutoLoadMoreRecyclerView;
import recycleview.RecycleListenerInterface;
import recycleview.RecycleMode;

public class SampleActivity extends AppCompatActivity implements AutoLoadMoreRecyclerView.RecycleLoadMoreListener {

    private AutoLoadMoreRecyclerView mList;
    private SimpleAdapter mAdapter;
    private ArrayList<String> mData = new ArrayList<String>();

    private int headerPosition = 0;
    private int footerPosition = 0;
    private int mode = RecycleMode.MODE_DATA;

    private int mCurrentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        for (int i = 0; i < 20; i++) {
            mData.add("item  " + i);
        }

        initToolbar();
        initSwipeLayout();
        initList();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initSwipeLayout() {
        final SwipeRefreshLayout mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
    }

    private void initList() {
        mList = (AutoLoadMoreRecyclerView) findViewById(R.id.recycler);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initAdapter();
        mList.setAdapter(mAdapter);
        mList.setTotalDataCount(50);
        mList.enableAutoLoadMore(this);
        mList.setDivider(R.drawable.divider);
    }

    @Override
    public void loadMore() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCurrentPage++;
                    ArrayList<String> moreData = new ArrayList<String>();
                    for (int i = 0; i < 20; i++) {
                        moreData.add("item  " + (20 * (mCurrentPage - 1) + i));
                    }
                    mAdapter.addData(moreData);
                    mList.loadMoreComplete();
                }
            }, 2000);

    }

    private void initAdapter() {
        mAdapter = new SimpleAdapter(mData);
        mAdapter.setOnItemClickListener(new RecycleListenerInterface.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object o) {
                showToast((String) o);
            }
        });
        mAdapter.setOnItemLongClickListener(new RecycleListenerInterface.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, Object o) {
                showToast(o + " long click");
            }
        });
        mAdapter.setOnHeaderViewClickListener(new RecycleListenerInterface.OnHeaderViewClickListener() {
            @Override
            public void onHeaderViewClick(View view, Object o) {
                showToast((String) o);
            }
        });
        mAdapter.setOnFooterViewClickListener(new RecycleListenerInterface.OnFooterViewClickListener() {
            @Override
            public void onFooterViewClick(View view, Object o) {
                showToast((String) o);
            }
        });
        mAdapter.setOnEmptyViewClickListener(new RecycleListenerInterface.OnEmptyViewClickListener() {
            @Override
            public void onEmptyViewClick(View view) {
                showToast("click empty view");
            }
        });

        mAdapter.setOnErrorViewClickListener(new RecycleListenerInterface.OnErrorViewClickListener() {
            @Override
            public void onErrorViewClick(View view) {
                showToast("click error view");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * add test method
     *
     * @param item menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_header:
                if (headerPosition < 0) {
                    headerPosition = 0;
                }
                headerPosition++;
                mAdapter.addHeader("header " + headerPosition);
                mList.scrollToPosition(0);
                return true;
            case R.id.action_remove_header:
                if (headerPosition <= 0) {
                    return true;
                }
                headerPosition--;
                mAdapter.removeHeader(headerPosition);
                mList.scrollToPosition(0);
                return true;
            case R.id.action_add_footer:
                if (footerPosition < 0) {
                    footerPosition = 0;
                }
                footerPosition++;
                mAdapter.addFooter("footer " + footerPosition);
                mList.scrollToPosition(mAdapter.getItemCount() - 1);
                return true;
            case R.id.action_remove_footer:
                if (footerPosition <= 0) {
                    return true;
                }
                footerPosition--;
                mAdapter.removeFooter(footerPosition);
                mList.scrollToPosition(mAdapter.getItemCount() - 1);
                return true;
            case R.id.action_change_mode:
                mAdapter.changeMode(++mode);
                if (mode > RecycleMode.MODE_EMPTY) {
                    mode = RecycleMode.MODE_DATA;
                }
                return true;
            case R.id.action_clear_data:
                mCurrentPage = 1;
                headerPosition = 0;
                footerPosition = 0;
                mAdapter.removeAllHeader();
                mAdapter.removeAllFooters();
                mAdapter.setData(null);
                return true;
            case R.id.action_set_data:
                mCurrentPage = 1;
                mData.clear();
                for (int i = 0; i < 20; i++) {
                    mData.add("item  " + i);
                }
                headerPosition = 0;
                footerPosition = 0;
                mAdapter.removeAllHeader();
                mAdapter.removeAllFooters();
                mAdapter.setData(mData);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
