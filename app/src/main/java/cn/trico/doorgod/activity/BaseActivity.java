package cn.trico.doorgod.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;

import cn.trico.doorgod.R;

/**
 * Created by Trico on 2018/3/7.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private boolean isTranslucent = false;
    private boolean isFullScreen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTranslucentStatusToLollipop();
        setFullScreenToLollipop();
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResourceId());
        initView();
        initData();
        Log.d("BaseActivity", getClass().getSimpleName());
    }

    protected abstract int setLayoutResourceId();

    protected void isTranslucent(boolean isTranslucent) {
        this.isTranslucent = isTranslucent;
    }

    protected void isFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
    }

    protected void initView() {
    }

    protected void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置沉浸式状态栏
     */
    public void setTranslucentStatusToLollipop() {//5.0版本
        if (isTranslucent) {
            //this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

           // this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            Window window = this.getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 设置全屏显示
     */
    protected void setFullScreenToLollipop() {
        if (isFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
