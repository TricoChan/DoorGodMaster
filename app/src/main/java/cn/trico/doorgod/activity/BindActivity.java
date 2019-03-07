package cn.trico.doorgod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import cn.trico.doorgod.R;
import cn.trico.doorgod.application.App;
import cn.trico.doorgod.entity.ImageInfo;
import cn.trico.doorgod.entity.UpdateUserInfo;
import cn.trico.doorgod.utils.HttpUtils;
import cn.trico.doorgod.utils.JsonParseUtils;
import cn.trico.doorgod.utils.RuntimeCache;
import cn.trico.doorgod.utils.ToastUtils;
import cn.trico.doorgod.value.CacheKey;
import cn.trico.doorgod.value.RequestType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 绑定设备Activity
 *
 * @author Trico
 * @get key: spinnerList value:list
 * @from BindActivity
 * @since 2018/07/18
 */
public class BindActivity extends BaseActivity implements View.OnClickListener {

    private final int BIND_SUCCESS = 0;
    private AppCompatSpinner mSpinner;
    private Button mEnterBtn;
    private Button mBindBtn;
    private EditText mNumEt;
    private List<String> spinnerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void setFullScreenToLollipop() {
        isFullScreen(true);
        super.setFullScreenToLollipop();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_bind_layout;
    }

    @Override
    protected void initView() {
        isTranslucent(true);
        mEnterBtn = findViewById(R.id.sure_button);
        mBindBtn = findViewById(R.id.bind_button);
        mSpinner = findViewById(R.id.bind_spinner);
        mNumEt = findViewById(R.id.bind_num);
    }

    @Override
    protected void initData() {
        spinnerList = (List<String>) getIntent().getSerializableExtra(CacheKey.CURRENT_SPINNER);
        if (spinnerList == null) {
            List<String> list = (List<String>) App.getCache().getSerializableObj(CacheKey.CURRENT_SPINNER + RuntimeCache.getCurrentCache(this, CacheKey.CURRENT_USER));
            if (list == null) {//通过登录未绑定的情况
                mSpinner.setEnabled(false);
                //todo 设置文字提示
            } else {
                mAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
                mSpinner.setAdapter(mAdapter);
            }
        } else {
            mAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerList);
            mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpinner.setAdapter(mAdapter);
        }
        mEnterBtn.setOnClickListener(this);
        mBindBtn.setOnClickListener(this);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RuntimeCache.putCurrentCache(BindActivity.this, CacheKey.CURRENT_DEVICE, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(BindActivity.this, R.string.toast_not_selected, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_button:
                //App.getCache().putString("","");
                App.getInstance().finishAll();//清除所有activity
                startActivity(new Intent(BindActivity.this, MainActivity.class));
                break;
            case R.id.bind_button:
                // todo 同时切换设备
                postBindRequest(mNumEt.getText().toString());
                break;
            default:
                return;
        }
    }

    private void postBindRequest(final String deviceid) {
        HttpUtils.sendDeviceRequestPost(deviceid, RequestType.ACTIVATE_DEVICE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.toastinThread(BindActivity.this, R.string.network_error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                final UpdateUserInfo result = JsonParseUtils.parseActiveUpdateInfo(responseData);
                switch (Integer.parseInt(result.getErr_code())) {
                    case 0:
                        RuntimeCache.putCurrentCache(BindActivity.this, CacheKey.CURRENT_DEVICE, deviceid);
                        App.getInstance().finishAll();//清除所有activity
                        startActivity(new Intent(BindActivity.this, MainActivity.class));
                        break;
                    case 2001:
                        ToastUtils.toastinThread(BindActivity.this, R.string.toast_bind_invalid);
                        break;
                    case 3002:
                        ToastUtils.toastinThread(BindActivity.this, R.string.toast_bind_existence);
                        break;
                    default:
                        ToastUtils.toastinThread(BindActivity.this, R.string.global_unknown_error);
                        return;
                }
            }
        });
    }
}
