package cn.trico.doorgod.utils;

import android.app.Application;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.trico.doorgod.application.App;
import cn.trico.doorgod.value.CacheKey;
import cn.trico.doorgod.value.RequestApi;
import cn.trico.doorgod.value.RequestType;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Trico on 2018/3/17.
 */

public class HttpUtils {
    /**
     * @post 更新用户信息
     */
    public static void sendUpdateRequestPost(String type, String value, okhttp3.Callback callback) {
        final FormBody formBody;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new RequestInteceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        switch (type) {
            case RequestType.USER_NAME:
                formBody = new FormBody.Builder()
                        .add(type, value)
                        .build();
                break;
            case RequestType.USER_PASSWORD:
                formBody = new FormBody.Builder()
                        .add(type, value)
                        .build();
                break;
            case RequestType.AVATAR_URL:
                formBody = new FormBody.Builder()
                        .add(type, value)
                        .build();
                break;
            default:
                return;
        }
        Request request = new Request.Builder()
                .url(RequestApi.UPDATE_USER_INFO)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * @post 请求图片
     */
    public static void sendImageRequestPost(String deviceid, String page_size, String page_number, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new RequestInteceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("device_id", deviceid)
                .add("page_size", page_size)
                .add("page_num", page_number)
                .build();
        Request request = new Request.Builder()
                .url(RequestApi.GET_IMAGE)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * @post 发送指令
     */
    public static void sendCommandRequestPost(String deviceid, String type, String content, okhttp3.Callback callback) {
        Request request;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new RequestInteceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                //.retryOnConnectionFailure(true)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("device_id", deviceid)
                .add("type", type)
                .add("content", content)
                .build();
        request = new Request.Builder()
                .url(RequestApi.SEND_CONTROL_COMMAND)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * @post 注册/激活设备
     * @post 获取设备信息
     */
    public static void sendDeviceRequestPost(String deviceid, String type, okhttp3.Callback callback) {
        Request request;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new RequestInteceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("device_id", deviceid)
                .build();
        switch (type) {
            case RequestType.ACTIVATE_DEVICE:
                request = new Request.Builder()
                        .url(RequestApi.DEVICE_ACTIVATION)
                        .post(formBody)
                        .build();
                break;
            case RequestType.DEVICE_INFO:
                request = new Request.Builder()
                        .url(RequestApi.GET_DEVICE_INFO)
                        .post(formBody)
                        .build();
                break;
            default:
                return;
        }
        client.newCall(request).enqueue(callback);
    }

    /**
     * @post 用户登录
     * @post 用户注册
     */
    public static void sendLoginRequestPost(String phone, String password, String type, okhttp3.Callback callback) {
        Request request;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new RequestInteceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("phone", phone)
                .add("password", password)
                .build();
        switch (type) {
            case RequestType.USER_LOGIN:
                request = new Request.Builder()
                        .url(RequestApi.USER_LOGIN)
                        .post(formBody)
                        .build();
                break;
            case RequestType.USER_REGIST:
                request = new Request.Builder()
                        .url(RequestApi.USER_REGISTER)
                        .post(formBody)
                        .build();
                break;
            default:
                return;
        }
        client.newCall(request).enqueue(callback);
    }

    /**
     * @get 获取用户信息
     */
    public static void sendLoginRequestGet(String phone, String password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new RequestInteceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(RequestApi.GET_USER_INFO)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 拦截器
     * <p>
     * 判断token是否空，还没请求到token或者不需要token则直接请求。
     * 请求的header已经有token，也不需要再添加token
     */
    public static final class RequestInteceptor implements Interceptor {
        private static final String USER_TOKEN = "Authorization";
        private final String token = "Bearer " + App.getCache().getString(CacheKey.CURRENT_TOKEN + RuntimeCache.getCurrentCache(App.getInstance().getApplicationContext(), CacheKey.CURRENT_USER));

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request originalRequest = chain.request();
            if (token.equals("Bearer null") || originalRequest.header("Authorization") != null) {
                return chain.proceed(originalRequest);
            }
            Request request = originalRequest.newBuilder()
                    .header(USER_TOKEN, token)
                    .build();
            return chain.proceed(request);
        }
    }
}
