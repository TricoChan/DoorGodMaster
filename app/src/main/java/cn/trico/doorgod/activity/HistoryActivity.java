package cn.trico.doorgod.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.trico.doorgod.CustomRecyclerView.EmptyRecyclerView;
import cn.trico.doorgod.CustomRecyclerView.OnLoadMoreListener;
import cn.trico.doorgod.R;
import cn.trico.doorgod.adapter.HomeImageAdapter;
import cn.trico.doorgod.application.App;
import cn.trico.doorgod.entity.ImageInfo;
import cn.trico.doorgod.fragment.BaseFragment;
import cn.trico.doorgod.utils.HttpUtils;
import cn.trico.doorgod.utils.JsonParseUtils;
import cn.trico.doorgod.utils.RuntimeCache;
import cn.trico.doorgod.value.CacheKey;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static cn.trico.doorgod.application.App.finishAll;

public class HistoryActivity extends BaseActivity {

    private final int initialPageNum = 1;
    private RefreshLayout refreshLayout;
    private EmptyRecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private HomeImageAdapter adapter;
    private List<ImageInfo.ImageBean> list = new ArrayList<>();
    private Toolbar toolbar;
    private View emptyView;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_home_layout;
    }

    @Override
    protected void initView() {
        emptyView = (LinearLayout) findViewById(R.id.ll_empty);
        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.rv_fragment_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setmEmptyRecyclerView(emptyView);
        refreshLayout = (RefreshLayout) findViewById(R.id.home_refresh_layout);
    }

    @Override
    protected void initData() {
        list = (List<ImageInfo.ImageBean>) App.getCache().getSerializableObj(CacheKey.CURRENT_HOME + RuntimeCache.getCurrentCache(getApplicationContext(), CacheKey.CURRENT_USER));//默认加载缓存数据
        if (list == null) {
            list = new ArrayList<>();
        }
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initNetworkData(initialPageNum);
            }
        });
        adapter = new HomeImageAdapter(getApplicationContext(), list, true);
        adapter.setEmptyView(R.layout.rv_load_empty);
        adapter.setLoadingView(R.layout.rv_load_ing);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                loadMore();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadMore() {
        String pageNum = Integer.toString(adapter.getItemCount() / 10 + 1);
        HttpUtils.sendImageRequestPost(RuntimeCache.getCurrentCache(HistoryActivity.this,CacheKey.CURRENT_DEVICE),
                "10",
                pageNum,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        //isFailed = false;
                        //加载失败，更新footer view提示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setLoadFailedView(R.layout.rv_load_fail);
                                adapter.loadFailed();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 401) {//Token过期
                            App.getCache().remove(CacheKey.CURRENT_TOKEN + RuntimeCache.getCurrentCache(getApplicationContext(), CacheKey.CURRENT_USER));
                            //todo dialog
                            finishAll();
                            startActivity(new Intent(HistoryActivity.this, LoginActivity.class));
                            return;
                        }
                        String responseData = response.body().string();
                        final ImageInfo imageInfo = JsonParseUtils.parseImageInfo(responseData);
                        if (imageInfo.getRes().isEmpty() || imageInfo.getRes().size() == 0 || imageInfo.getRes() == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setLoadEndView(R.layout.rv_load_end);
                                    adapter.loadEnd();
                                }
                            });
                        } else {
                            list.addAll(imageInfo.getRes());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setLoadMoreData(list);
                                }
                            });
                        }
                    }
                });
    }

    private void initNetworkData(final int initialPageNum) {
        HttpUtils.sendImageRequestPost(RuntimeCache.getCurrentCache(HistoryActivity.this,CacheKey.CURRENT_DEVICE), "10", Integer.toString(initialPageNum), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                refreshLayout.finishRefresh(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 401) {//Token过期
                    App.getCache().remove(CacheKey.CURRENT_TOKEN + RuntimeCache.getCurrentCache(getApplicationContext(), CacheKey.CURRENT_USER));
                    //todo dialog
                    finishAll();
                    startActivity(new Intent(HistoryActivity.this, LoginActivity.class));
                    return;
                }
                String responseData = response.body().string();
                final ImageInfo imageInfo = JsonParseUtils.parseImageInfo(responseData);
                list.clear();
                adapter.reset();
                //list = imageInfo.getRes();//指向了别的内存空间
                list.addAll(imageInfo.getRes());
                refreshLayout.finishRefresh(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onStop() {
        App.getCache().putSerializableObj(CacheKey.CURRENT_HOME + RuntimeCache.getCurrentCache(getApplicationContext(), CacheKey.CURRENT_USER), (Serializable) list);//存缓存
        super.onStop();
    }


}
