package cn.trico.doorgod.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.roundview.RoundTextView;

import cn.trico.doorgod.R;
import cn.trico.doorgod.activity.BindActivity;
import cn.trico.doorgod.activity.LoginActivity;
import cn.trico.doorgod.activity.VersionActivity;
import cn.trico.doorgod.application.App;
import cn.trico.doorgod.utils.RuntimeCache;
import cn.trico.doorgod.value.CacheKey;

/**
 * 个人中心页Activity
 *
 * @author Trico
 * @since 2018/3/7
 */

public class UserFragment extends BaseFragment {

    private ImageView iv_user_bg;
    private ImageView iv_user_key;
    private ImageView iv_user_version;
    private ImageView iv_user_clear;
    private TextView tv_user_key;
    private TextView tv_user_version;
    private TextView tv_user_clear;
    private TextView tv_user_name;
    private LinearLayout user_key;
    private LinearLayout user_version;
    private LinearLayout user_clear;
    private LinearLayout user_logout;
    private RoundTextView bt_alter_info;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_user_layout;
    }

    @Override
    protected void initView() {
//        iv_user_bg = (ImageView) mContentView.findViewById(R.id.iv_user_bg);
//        iv_user_clear = (ImageView) mContentView.findViewById(R.id.iv_user_clear);
//        iv_user_version = (ImageView) mContentView.findViewById(R.id.iv_user_key);
//        iv_user_clear = (ImageView) mContentView.findViewById(R.id.iv_user_version);
//        tv_user_key = (TextView) mContentView.findViewById(R.id.tv_user_key);
//        tv_user_version = (TextView) mContentView.findViewById(R.id.tv_user_version);
//        tv_user_clear = (TextView) mContentView.findViewById(R.id.tv_user_clear);
        user_clear = (LinearLayout) mContentView.findViewById(R.id.user_clear);
        user_key = (LinearLayout) mContentView.findViewById(R.id.user_key);
        user_version = (LinearLayout) mContentView.findViewById(R.id.user_version);
        user_logout = (LinearLayout) mContentView.findViewById(R.id.user_logout);
        tv_user_name = (TextView) mContentView.findViewById(R.id.tv_user_name);
        bt_alter_info = (RoundTextView) mContentView.findViewById(R.id.bt_alter_info);
    }

    @Override
    protected void initData() {
        tv_user_name.setText(RuntimeCache.getCurrentCache(getContext(), CacheKey.CURRENT_USER));
        bt_alter_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "尚未开放，请等待新版本APP", Toast.LENGTH_SHORT).show();
            }
        });
        user_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    App.getCache().clearCache();
                    Toast.makeText(getContext(),R.string.toast_clear_success,Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "清理失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });

        user_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BindActivity.class));//todo 清除activity麻烦
            }
        });


        user_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VersionActivity.class));
            }
        });

        user_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getCache().remove(CacheKey.CURRENT_TOKEN + RuntimeCache.getCurrentCache(getContext(), CacheKey.CURRENT_USER));//删除token
                App.getInstance().finishAll();//清除所有activity
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
//
//        bt_user_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                App.getInstance().finishAll();
//            }
//        });
    }

    @Override
    public void fetchData() {

    }

}
