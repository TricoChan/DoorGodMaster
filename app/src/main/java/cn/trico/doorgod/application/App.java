package cn.trico.doorgod.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.trico.doorgod.utils.CacheUtils;
import cn.trico.doorgod.utils.RuntimeCache;
import cn.trico.doorgod.value.CacheKey;

import static cn.trico.doorgod.utils.PushUtils.setAlias;

public class App extends Application {

    public static List<Activity> activities = new ArrayList<>();
    private static CacheUtils cache;
    private static App instance;
    private boolean isAliasAction = true;
    private int action = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        String registrationId = JPushInterface.getRegistrationID(this);
        String alias = RuntimeCache.getCurrentCache(this, CacheKey.CURRENT_USER);
        Log.e("1099", "run:--------->registrationId： " + registrationId);
        setAlias(getApplicationContext(), alias, isAliasAction, action);
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    /**
     * 获取 Application 实例
     *
     * @return application 实例
     */
    public static App getInstance() {
        return instance;
    }

    /**
     * 获取缓存实例
     *
     * @return 缓存类实例
     */
    public static CacheUtils getCache() {
        if (cache == null) {
            cache = CacheUtils.get(App.getInstance().getCacheDir());
        }
        return cache;
    }

    /**
     * 注册活动生命周期管理器
     */
    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            activities.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activities.remove(activity);
        }
    };

    /**
     * 清除所有Activity
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
