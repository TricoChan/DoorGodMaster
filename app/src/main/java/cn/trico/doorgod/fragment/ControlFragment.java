package cn.trico.doorgod.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.xw.repo.BubbleSeekBar;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import cn.trico.doorgod.R;
import cn.trico.doorgod.application.App;
import cn.trico.doorgod.entity.CommandReturn;
import cn.trico.doorgod.utils.HttpUtils;
import cn.trico.doorgod.utils.JsonParseUtils;
import cn.trico.doorgod.utils.NoRepeatOnCheckedChangeListener;
import cn.trico.doorgod.utils.RuntimeCache;
import cn.trico.doorgod.utils.ToastUtils;
import cn.trico.doorgod.value.CacheKey;
import cn.trico.doorgod.value.RequestType;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 控制页Fragment
 *
 * @author Trico
 * @since 2018/3/7
 */

public class ControlFragment extends SwipeImmerseFragment implements View.OnClickListener, BubbleSeekBar.CustomSectionTextArray, BubbleSeekBar.OnProgressChangedListener {

    private BubbleSeekBar mBubbleSeekBar;
    private View imageButton;
    private Switch controlSwitch;
    private Switch pushSwitch;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_control_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        controlSwitch = (Switch) mContentView.findViewById(R.id.control_switch);
        pushSwitch = (Switch) mContentView.findViewById(R.id.push_switch);
        mBubbleSeekBar = (BubbleSeekBar) mContentView.findViewById(R.id.control_machine);
        imageButton = (View) mContentView.findViewById(R.id.ib_control_photo);
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
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
    }

    @Override
    protected void initData() {
        imageButton.setOnClickListener(this);
        mBubbleSeekBar.setCustomSectionTextArray(this);
        mBubbleSeekBar.setOnProgressChangedListener(this);
        controlSwitch.setChecked(false);
        pushSwitch.setChecked(RuntimeCache.getBooleanCache(getContext(), CacheKey.SWITCH_STATE));
        controlSwitch.setSwitchTextAppearance(getContext(), R.style.s_false);
        pushSwitch.setSwitchTextAppearance(getContext(), R.style.s_false);
        controlSwitch.setOnCheckedChangeListener(new NoRepeatOnCheckedChangeListener() {
            @Override
            public void onNoRepeatClick(CompoundButton buttonView, boolean isChecked) {
                processNoRepeat(buttonView, isChecked);
            }
        });
        pushSwitch.setOnCheckedChangeListener(new NoRepeatOnCheckedChangeListener() {
            @Override
            public void onNoRepeatClick(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JPushInterface.stopPush(getContext());
                } else {
                    JPushInterface.resumePush(getContext());
                }
                RuntimeCache.putBooleanCache(getContext(), CacheKey.SWITCH_STATE, isChecked);
            }
        });
    }

    /**
     * 拍照按钮点击事件
     */
    @Override
    public void onClick(View v) {
        HttpUtils.sendCommandRequestPost(RuntimeCache.getCurrentCache(getContext(), CacheKey.CURRENT_DEVICE), RequestType.CONTROL_PHOTO, RequestType.REQUEST_NULL_CONTENT, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.toastinThread(getContext(), R.string.network_error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                final CommandReturn commandReturn = JsonParseUtils.parseCommandReturn(responseData);
                if (commandReturn.getErr_code() != 404) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getContext())
                                    .load(commandReturn.getResult())
                                    .into(parallax);
                            Toast.makeText(getContext(), R.string.toast_get_photo, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ToastUtils.toastinThread(getContext(), R.string.toast_device_turnoff);
                }
            }
        });
    }


    /**
     * 开关按钮状态改变事件
     */
    public void processNoRepeat(CompoundButton buttonView, boolean isChecked) {
        //监听到改变阻止点击开关，直到返回结果恢复点击事件
        //控制开关字体颜色
        //区别人为和自动检测，防止重复调用
        if (!buttonView.isPressed()) return;
        if (isChecked) {
            HttpUtils.sendCommandRequestPost(RuntimeCache.getCurrentCache(getContext(), CacheKey.CURRENT_DEVICE), RequestType.CONTROL_SWITCH, "1", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            controlSwitch.setSwitchTextAppearance(getContext(), R.style.s_false);
                            Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    final CommandReturn commandReturn = JsonParseUtils.parseCommandReturn(responseData);
                    if (commandReturn.getErr_code() == 0) {
                        controlSwitch.setSwitchTextAppearance(getContext(), R.style.s_true);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "成功打开开关", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "打开开关失败", Toast.LENGTH_SHORT).show();
                                controlSwitch.setChecked(false);
                                controlSwitch.setSwitchTextAppearance(getContext(), R.style.s_false);
                            }
                        });
                    }
                }
            });
        } else {
            HttpUtils.sendCommandRequestPost(RuntimeCache.getCurrentCache(getContext(), CacheKey.CURRENT_DEVICE), "1", "0", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                            controlSwitch.setSwitchTextAppearance(getContext(), R.style.s_true);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    final CommandReturn commandReturn = JsonParseUtils.parseCommandReturn(responseData);
                    if (commandReturn.getErr_code() == 0) {
                        controlSwitch.setSwitchTextAppearance(getContext(), R.style.s_false);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "成功关闭开关", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        controlSwitch.setSwitchTextAppearance(getContext(), R.style.s_true);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "关闭开关失败", Toast.LENGTH_SHORT).show();
                                controlSwitch.setChecked(true);
                            }
                        });
                    }
                }
            });
        }
    }


    /**
     * 电机进度文字初始化
     */
    @NonNull
    @Override
    public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
        array.clear();
        array.put(0, "一档");
        array.put(1, "二档");
        array.put(2, "三档");
        array.put(3, "四档");
        return array;
    }

    /**
     * 电机设置进度条改变事件
     */
    @Override
    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

    }

    @Override
    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

    }

    @Override
    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
        if (fromUser) {
            HttpUtils.sendCommandRequestPost(RuntimeCache.getCurrentCache(getContext(), CacheKey.CURRENT_DEVICE), RequestType.CONTROL_MACHINE, Integer.toString(progress), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtils.toastinThread(getContext(), R.string.network_error);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    final CommandReturn commandReturn = JsonParseUtils.parseCommandReturn(responseData);
                    if (commandReturn.getErr_code() != 404) {
                        ToastUtils.toastinThread(getContext(), R.string.toast_set_success);
                    } else {
                        ToastUtils.toastinThread(getContext(), R.string.toast_device_turnoff);
                    }
                }
            });
        }
    }

    @Override
    public void fetchData() {
    }


}