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

import cn.trico.doorgod.R;
import cn.trico.doorgod.entity.UpdateUserInfo;
import cn.trico.doorgod.utils.HttpUtils;
import cn.trico.doorgod.utils.JsonParseUtils;
import cn.trico.doorgod.utils.RegexUtil;
import cn.trico.doorgod.utils.ToastUtils;
import cn.trico.doorgod.value.IntentKey;
import cn.trico.doorgod.value.RequestType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static cn.trico.doorgod.application.App.finishAll;

public class RegisterActivity extends BaseActivity {

    private Button mRegisterButton;
    private EditText mPhoneNum;
    private EditText mPassword;
    private EditText mEnsureps;
    private ProgressBar mProgressView;
    private String mobile;
    private String password;
    private String ensure;
    private String[] info = new String[2];

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_register_layout;
    }

    @Override
    protected void initView() {
        isFullScreen(true);
        mRegisterButton = (Button) findViewById(R.id.ensure_button);
        mPhoneNum = (EditText) findViewById(R.id.register_phone);
        mPassword = (EditText) findViewById(R.id.register_password);
        mEnsureps = (EditText) findViewById(R.id.ensure_password);
        mProgressView = (ProgressBar) findViewById(R.id.register_progress);
    }


    @Override
    protected void initData() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = mPhoneNum.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                ensure = mEnsureps.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(RegisterActivity.this, R.string.tips_phone_null, Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, R.string.tips_password_null, Toast.LENGTH_SHORT).show();
                } else if (!RegexUtil.isMobileNum(mobile)) {
                    Toast.makeText(RegisterActivity.this, R.string.tips_phone_illegal, Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6 || password.length() > 12) {
                    Toast.makeText(RegisterActivity.this, R.string.tips_password_length, Toast.LENGTH_SHORT).show();
                } else if (!password.equals(ensure)) {
                    Toast.makeText(RegisterActivity.this, R.string.tips_password_inconsistent, Toast.LENGTH_SHORT).show();
                } else {
                    mProgressView.setVisibility(View.VISIBLE);
                    registerRequest();
                }
            }
        });
    }

    private void registerRequest() {
        HttpUtils.sendLoginRequestPost(mobile, password, RequestType.USER_REGIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //todo 宕机
                mProgressView.setVisibility(View.INVISIBLE);
                ToastUtils.toastinThread(RegisterActivity.this, R.string.network_error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mProgressView.setVisibility(View.INVISIBLE);
                String responseData = response.body().string();
                UpdateUserInfo userInfo = JsonParseUtils.parseActiveUpdateInfo(responseData);
                switch (Integer.parseInt(userInfo.getErr_code())) {
                    case 0://登录成功
                        info[0] = mobile;
                        info[1] = password;
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra(IntentKey.REGISTER_SUCCESS, info);
                        finishAll();
                        startActivity(intent);
                        break;
                    case 1004://用户已存在
                        ToastUtils.toastinThread(RegisterActivity.this, R.string.tips_user_exist);
                        break;
                    default:
                        ToastUtils.toastinThread(RegisterActivity.this, R.string.tips_unknown_error);
                        break;
                }
            }
        });
    }
}
