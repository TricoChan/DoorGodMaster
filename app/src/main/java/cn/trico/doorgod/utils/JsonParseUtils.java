package cn.trico.doorgod.utils;

import android.text.style.UpdateAppearance;

import com.google.gson.Gson;

import cn.trico.doorgod.entity.CommandReturn;
import cn.trico.doorgod.entity.ImageInfo;
import cn.trico.doorgod.entity.DeviceInfo;
import cn.trico.doorgod.entity.UpdateUserInfo;
import cn.trico.doorgod.entity.UserInfo;
import cn.trico.doorgod.entity.UserLogin;

/**
 * Json解析工具类
 *
 * @author Trico
 * @since 2018/3/26
 */

public class JsonParseUtils {
    /**
     * 请求图片结果解析
     *
     * @param jsonData jsonData
     * @return ImageInfo类对象
     */
    public static ImageInfo parseImageInfo(String jsonData) {
        Gson gson = new Gson();
        ImageInfo imageInfo = gson.fromJson(jsonData, ImageInfo.class);
        return imageInfo;
    }

    /**
     * 请求设备信息解析
     *
     * @param jsonData jsonData
     * @return DeviceInfo类对象
     */
    public static DeviceInfo parseDeviceInfo(String jsonData) {
        Gson gson = new Gson();
        DeviceInfo deviceInfo = gson.fromJson(jsonData, DeviceInfo.class);
        return deviceInfo;
    }

    /**
     * 发送指令结果解析
     *
     * @param jsonData jsonData
     * @return CommandReturn类对象
     */
    public static CommandReturn parseCommandReturn(String jsonData) {
        Gson gson = new Gson();
        CommandReturn commandReturn = gson.fromJson(jsonData, CommandReturn.class);
        return commandReturn;
    }
    /**
     * 请求更新用户信息解析
     * 请求激活/注册设备/注册账号结果解析
     *
     * @param jsonData jsonData
     * @return UpdateUserInfo类对象
     */
    public static UpdateUserInfo parseActiveUpdateInfo(String jsonData) {
        Gson gson = new Gson();
        UpdateUserInfo userInfo = gson.fromJson(jsonData, UpdateUserInfo.class);
        return userInfo;
    }
    /**
     * 请求用户登录结果解析
     *
     * @param jsonData jsonData
     * @return UserLogin类对象
     */
    public static UserLogin parseUserLogin(String jsonData) {
        Gson gson = new Gson();
        UserLogin userLogin = gson.fromJson(jsonData, UserLogin.class);
        return userLogin;
    }
    /**
     * 请求获取用户信息结果解析
     *
     * @param jsonData jsonData
     * @return UserInfo类对象
     */
    public static UserInfo parseUserInfo(String jsonData) {
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(jsonData, UserInfo.class);
        return userInfo;
    }
}