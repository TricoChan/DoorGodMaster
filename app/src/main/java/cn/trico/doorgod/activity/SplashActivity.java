package cn.trico.doorgod.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import cn.trico.doorgod.R;
import cn.trico.doorgod.application.App;
import cn.trico.doorgod.utils.RuntimeCache;
import cn.trico.doorgod.value.CacheKey;

/**
 * 欢迎页Activity
 *
 * @author Trico
 * @since 2018/3/7
 */

public class SplashActivity extends BaseActivity {

    private final static int GO_MAIN = 1;
    private final static int GO_LOGIN = 2;
    private final static int GO_BIND = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_MAIN:
                    removeAllMessage();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
                case GO_LOGIN:
                    removeAllMessage();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                    break;
                case GO_BIND:
                    removeAllMessage();
                    startActivity(new Intent(SplashActivity.this, BindActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void setFullScreenToLollipop() {
        isFullScreen(true);
        super.setFullScreenToLollipop();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_splash_layout;
    }

    /**
     * 当计时结束时，分发跳转界面
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (App.getCache().getString("token" + RuntimeCache.getCurrentCache(this, CacheKey.CURRENT_USER)) != null) {
            if (App.getCache().getSerializableObj("spinnerList" + RuntimeCache.getCurrentCache(this, CacheKey.CURRENT_USER)) == null) {//成功登录未绑定
                handler.sendEmptyMessageDelayed(GO_BIND, 2000);
            } else {//成功登录已绑定
                handler.sendEmptyMessageDelayed(GO_MAIN, 2000);
            }
        } else {//未登录
            handler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
        }
    }

    private void removeAllMessage() {
        handler.removeMessages(GO_MAIN);
        handler.removeMessages(GO_LOGIN);
        handler.removeMessages(GO_BIND);
    }
}
