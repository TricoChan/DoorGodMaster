package cn.trico.doorgod.utils;

import android.content.Context;
import android.content.Intent;

import cn.trico.doorgod.activity.LoginActivity;
import cn.trico.doorgod.application.App;
import cn.trico.doorgod.value.CacheKey;

import static cn.trico.doorgod.application.App.finishAll;

/**
 * @author Trico
 * @since  2018/08/11
 */
public class TokenUtils {
    /**
     * 判断token过期并弹框退出
     */
    public static boolean isTokenInvalid(Context context, int responseCode){
        if (responseCode == 401) {//Token过期
            App.getCache().remove(CacheKey.CURRENT_TOKEN + RuntimeCache.getCurrentCache(App.getInstance(), CacheKey.CURRENT_USER));
            //todo dialog
            finishAll();
            context.startActivity(new Intent(context, LoginActivity.class));
            return true;
        }
        return false;
    }
}
