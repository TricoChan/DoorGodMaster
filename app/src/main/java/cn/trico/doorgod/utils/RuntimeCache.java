package cn.trico.doorgod.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * 运行时SharedPrefrences缓存
 *
 * @author Trico
 * @since 2018/07/18
 */
public class RuntimeCache {
    /**
     * 存储APP运行时显示的相关信息
     *
     * @param context
     * @param key
     */
    public static void putCurrentCache(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("RuntimeCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 存储APP运行时缓存的布尔类型值
     *
     * @param context
     * @param key
     */
    public static void putBooleanCache(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("RuntimeCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取APP运行时缓存值
     *
     * @param context
     * @param key
     * @return String
     */
    public static String getCurrentCache(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("RuntimeCache", Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    /**
     * 获取APP运行时缓存布尔值
     *
     * @param context
     * @param key
     * @return boolean
     */
    public static boolean getBooleanCache(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("RuntimeCache", Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
}
