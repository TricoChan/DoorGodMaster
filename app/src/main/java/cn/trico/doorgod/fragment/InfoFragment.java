package cn.trico.doorgod.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.trico.doorgod.CustomRecyclerView.EmptyRecyclerView;
import cn.trico.doorgod.R;
import cn.trico.doorgod.activity.HistoryActivity;
import cn.trico.doorgod.activity.LoginActivity;
import cn.trico.doorgod.adapter.DeviceInfoAdapter;
import cn.trico.doorgod.application.App;
import cn.trico.doorgod.dialog.AlterDialogFragment;
import cn.trico.doorgod.entity.DeviceInfo;
import cn.trico.doorgod.entity.DeviceInfoItem;
import cn.trico.doorgod.utils.HttpUtils;
import cn.trico.doorgod.utils.ITabClickListener;
import cn.trico.doorgod.utils.JsonParseUtils;
import cn.trico.doorgod.utils.RuntimeCache;
import cn.trico.doorgod.utils.TimeUtils;
import cn.trico.doorgod.utils.ToastUtils;
import cn.trico.doorgod.value.CacheKey;
import cn.trico.doorgod.value.RequestType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static cn.trico.doorgod.application.App.finishAll;

/**
 * 信息展示页
 * <p>
 * 有信息则读缓存，用户更改或数据更新则更新缓存
 * <p>
 * Created by Trico on 2018/3/26.
 */

/*
 * 设备信息cache存储清单
 * ---------------------------------------
 * 信息list cache
 * @key "list+手机号"
 * @value (List<DeviceInfoItem>) 手机号字符串
 * ---------------------------------------
 * 电量 cache
 * @key "ELECTRICITY+手机号"
 * @value (String) 电量值
 * ---------------------------------------
 */
public class InfoFragment extends SwipeImmerseFragment implements ITabClickListener, DeviceInfoAdapter.onItemClickListener {

    private static String ELECTRICITY;
    private static String TIME;
    private String CURRENT_USER;
    //private ImageView iv_battery;
    private TextView tv_battery;
    private TextView tv_device_id;
    private TextView tv_activate_time;
    private EmptyRecyclerView emptyRecyclerView;
    private List<DeviceInfoItem> list = new ArrayList<>();
    private String body;
    private String DEVICE;
    private DeviceInfoAdapter adapter;
    private View bt_history_image;
    private View mEmptyView;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_info_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        final RefreshLayout mRefreshLayout = refreshLayout;
        tv_device_id = mContentView.findViewById(R.id.tv_device_id);
        tv_activate_time = mContentView.findViewById(R.id.tv_activate_time);
        bt_history_image = mContentView.findViewById(R.id.bt_history_image);
        bt_history_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HistoryActivity.class));
            }
        });
        mRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().post(new Runnable() {
                    @Override
                    public void run() {
                        initList();
                        mRefreshLayout.finishRefresh();
                    }
                });
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
            }

            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
        });
        mEmptyView = (LinearLayout) mContentView.findViewById(R.id.ll_empty);
//        iv_battery = (ImageView) mContentView.findViewById(R.id.iv_battery_level);
        tv_battery = (TextView) mContentView.findViewById(R.id.tv_battery_level);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) mContentView.findViewById(R.id.info_collapsing_toolbar);
        emptyRecyclerView = (EmptyRecyclerView) mContentView.findViewById(R.id.rv_device_info);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        emptyRecyclerView.setLayoutManager(layoutManager);
        emptyRecyclerView.setNestedScrollingEnabled(false);//防止滑动粘滞
        emptyRecyclerView.setmEmptyRecyclerView(mEmptyView);//设置空布局
    }

    @Override
    protected void initData() {
        DEVICE = RuntimeCache.getCurrentCache(getContext(), CacheKey.CURRENT_DEVICE);
        CURRENT_USER = RuntimeCache.getCurrentCache(getContext(), CacheKey.CURRENT_USER);
        list = (List<DeviceInfoItem>) App.getCache().getSerializableObj(CacheKey.CURRENT_LIST + CURRENT_USER + DEVICE);//取文件缓存 信息list
        ELECTRICITY = App.getCache().getString(CacheKey.CURRENT_ELECTRICITY + CURRENT_USER + DEVICE);//取文件缓存 电量
        TIME = App.getCache().getString(CacheKey.CREATE_TIME + CURRENT_USER + DEVICE);
        if (list == null) {
            //list默认从cache中取出
            list = new ArrayList<>();
        }
        if (ELECTRICITY != null) {
            tv_battery.setText(ELECTRICITY);
        }
        if (DEVICE != null) {
            tv_device_id.setText(DEVICE);
        }
        if (TIME != null) {
            tv_activate_time.setText(TIME);
        }
        adapter = new DeviceInfoAdapter(getContext(), list);
        adapter.setItemClickListener(this);
        emptyRecyclerView.setAdapter(adapter);
    }


    private void initList() {
        try {
            HttpUtils.sendDeviceRequestPost(RuntimeCache.getCurrentCache(getContext(), CacheKey.CURRENT_DEVICE), RequestType.DEVICE_INFO, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    refreshLayout.finishRefresh(false);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 401) {//Token过期
                        App.getCache().remove(CacheKey.CURRENT_TOKEN + CURRENT_USER);
                        //todo dialog
                        finishAll();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    } else {
                        String responseData = response.body().string();
                        final DeviceInfo deviceInfo = JsonParseUtils.parseDeviceInfo(responseData);
                        if (Integer.parseInt(deviceInfo.getErr_code()) == 0) {
                            refreshLayout.finishRefresh(true);
                            final DeviceInfo.InfoDetails infoDetails = deviceInfo.getRes();
                            if (list != null) list.clear();
                            if (infoDetails.getKnock_state().equals("1")) {
                                body = "有人敲门";
                            } else {
                                body = "静悄悄的，无人来访";
                            }
                            list.add(new DeviceInfoItem("震动检测", body));
                            if (infoDetails.getLock_state().equals("1")) {
                                body = "门锁开";
                            } else {
                                body = "已锁上";
                            }
                            list.add(new DeviceInfoItem("门锁状态", body));
                            if (infoDetails.getPoke_state().equals("1")) {
                                body = "锁孔正常";
                            } else {
                                body = "锁孔异常";
                            }
                            list.add(new DeviceInfoItem("锁孔状态", body));
                            list.add(new DeviceInfoItem(getNick(CacheKey.EXTEND_MODULE_1 + CURRENT_USER), infoDetails.getExtend_module1()));
                            list.add(new DeviceInfoItem(getNick(CacheKey.EXTEND_MODULE_2 + CURRENT_USER), infoDetails.getExtend_module2()));
                            list.add(new DeviceInfoItem(getNick(CacheKey.EXTEND_MODULE_3 + CURRENT_USER), infoDetails.getExtend_module3()));
                            list.add(new DeviceInfoItem(getNick(CacheKey.EXTEND_MODULE_4 + CURRENT_USER), infoDetails.getExtend_module4()));
                            list.add(new DeviceInfoItem(getNick(CacheKey.EXTEND_MODULE_5 + CURRENT_USER), infoDetails.getExtend_module5()));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_battery.setText(ELECTRICITY = infoDetails.getElectricity());
                                    TIME = getResources().getString(R.string.tv_activate_part1) + TimeUtils.utc2Local(deviceInfo.getRes().getCreate_time(), TimeUtils.CHINESE_TIME) + getResources().getString(R.string.tv_activate_part2);
                                    tv_activate_time.setText(TIME);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            ToastUtils.toastinThread(getActivity(), R.string.toast_other_code);
                        }
                    }

                }
            });
        } catch (Exception e) {
            ToastUtils.toastinThread(getActivity(), R.string.toast_request_exception);
        }
    }

    private String getNick(String key) {
        String nick = App.getCache().getString(key);
        if (nick == null) {
            return "扩展模块";
        } else {
            return nick;
        }
    }

    @Override
    public void onStop() {
        //onStop阶段存储缓存
        if (list.size() > 0 && ELECTRICITY != null && TIME != null) {
            App.getCache().putString(CacheKey.CREATE_TIME + CURRENT_USER + DEVICE, TIME);
            App.getCache().putString(CacheKey.CURRENT_ELECTRICITY + CURRENT_USER + DEVICE, ELECTRICITY);
            App.getCache().putSerializableObj(CacheKey.CURRENT_LIST + CURRENT_USER + DEVICE, (Serializable) list);//list存缓存
        }
        super.onStop();
    }

    @Override
    public void fetchData() {
    }

    @Override
    public void onMenuItemClick() {

    }

    @Override
    public BaseFragment getFragment() {
        return null;
    }

    @Override
    public void onItemClick(final int position, final View view) {
        if (position >= 3) {
            //前三项不允许修改
            //三项之后允许弹出dialog增删查改属性值
            AlterDialogFragment viewDialogFragment = new AlterDialogFragment();
            viewDialogFragment.show(getFragmentManager());
            viewDialogFragment.setCallback(new AlterDialogFragment.CallBack() {
                @Override
                public void catchData(String data) {
                    list.get(position).setHead(data);
                    App.getCache().putString(CacheKey.EXTEND_MODULE + Integer.toString(position - 2) + CURRENT_USER, data);//别名本地保存
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }
    }
}