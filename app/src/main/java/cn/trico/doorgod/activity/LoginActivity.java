package cn.trico.doorgod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import cn.trico.doorgod.R;
import cn.trico.doorgod.application.App;
import cn.trico.doorgod.entity.UserLogin;
import cn.trico.doorgod.utils.HttpUtils;
import cn.trico.doorgod.utils.JsonParseUtils;
import cn.trico.doorgod.utils.RegexUtil;
import cn.trico.doorgod.utils.RuntimeCache;
import cn.trico.doorgod.utils.ToastUtils;
import cn.trico.doorgod.value.CacheKey;
import cn.trico.doorgod.value.IntentKey;
import cn.trico.doorgod.value.RequestType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
 * 登录cache存储清单
 * ---------------------------------------
 * 运行缓存
 * ---------------------------------------
 * 用户信息cache
 * @key "user"
 * @value (String) 手机号字符串
 * ---------------------------------------
 * 文件缓存
 * ---------------------------------------
 * token cache
 * @key "token+手机号"
 * @value (String) token
 * ---------------------------------------
 * 设备id分栏cache
 * @key "spinnerList + mobile"
 * @value (List<DeviceId>) spinnerList
 * ---------------------------------------
 */

/**
 * 登录Activity
 * <p>
 * 存储cache
 *
 * @author Trico
 * @transmit key: spinnerList value:list
 * @to BindActivity
 * @since 2018/7/16
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static String MOBILE_NUM;
    private static String PASSWORD;
    private static String[] FILTER;
    private Button mLoginButton;
    private Button mRegisterButton;
    private EditText mPhoneNum;
    private EditText mPassword;
    private ProgressBar mProgressView;
    private List list;

    @Override
    protected void setFullScreenToLollipop() {
        isFullScreen(true);
        super.setFullScreenToLollipop();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_login_layout;
    }

    @Override
    protected void initView() {
        mLoginButton = (Button) findViewById(R.id.login_button);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mPhoneNum = (EditText) findViewById(R.id.phone);
        mPassword = (EditText) findViewById(R.id.password);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
    }

    @Override
    protected void initData() {
        if ((FILTER = getIntent().getStringArrayExtra(IntentKey.REGISTER_SUCCESS)) != null) {
            mPhoneNum.setText(FILTER[0]);
            mPassword.setText(FILTER[1]);//从注册界面获取数据自动填充
        } else if ((MOBILE_NUM = RuntimeCache.getCurrentCache(LoginActivity.this, CacheKey.CURRENT_USER)) != null) {
            mPhoneNum.setText(MOBILE_NUM);//从
        }
        mLoginButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                try {
                    loginRequest();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.register_button:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            default:
                return;
        }
    }

    private void loginRequest() throws InterruptedException {
        MOBILE_NUM = mPhoneNum.getText().toString().trim();
        PASSWORD = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(MOBILE_NUM)) {
            Toast.makeText(this, R.string.tips_phone_null, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(PASSWORD)) {
            Toast.makeText(this, R.string.tips_password_null, Toast.LENGTH_SHORT).show();
        } else if (!RegexUtil.isMobileNum(MOBILE_NUM)) {
            Toast.makeText(this, R.string.tips_phone_illegal, Toast.LENGTH_SHORT).show();
        } else if (PASSWORD.length() < 6 || PASSWORD.length() > 12) {
            Toast.makeText(this, R.string.tips_password_length, Toast.LENGTH_SHORT).show();
        } else {
            mProgressView.setVisibility(View.VISIBLE);
            HttpUtils.sendLoginRequestPost(MOBILE_NUM, PASSWORD, RequestType.USER_LOGIN, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressView.setVisibility(View.INVISIBLE);
                            ToastUtils.toastinThread(LoginActivity.this, R.string.network_error);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressView.setVisibility(View.INVISIBLE);
                        }
                    });
                    String responseData = response.body().string();
                    UserLogin loginInfo = JsonParseUtils.parseUserLogin(responseData);
                    if (Integer.parseInt(loginInfo.getErr_code()) == 0) {//登录成功
                        RuntimeCache.putCurrentCache(LoginActivity.this, CacheKey.CURRENT_USER, MOBILE_NUM);//运行缓存 存储当前用户
                        App.getCache().putString(CacheKey.CURRENT_TOKEN + MOBILE_NUM, loginInfo.getRes().getToken());//文件缓存 存储token
                        list = loginInfo.getRes().getDevice();
                        for (int i = 0; i < list.size(); i++) {
                            UserLogin.DeviceId listItem = (UserLogin.DeviceId) list.get(i);
                            list.set(i, listItem.getDevice_id());
                        }
                        //list = Arrays.asList(list.toArray(new String[list.size()]));
                        App.getCache().putSerializableObj(CacheKey.CURRENT_SPINNER + MOBILE_NUM, (Serializable) list);//文件缓存
                        Intent intent = new Intent(LoginActivity.this, BindActivity.class);
                        intent.putExtra(CacheKey.CURRENT_SPINNER, (Serializable) list);
                        startActivity(intent);
                    } else {
                        ToastUtils.toastinThread(LoginActivity.this, R.string.invalid_account);
                    }
                }
            });
        }
    }
}
