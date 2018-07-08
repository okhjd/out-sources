package com.dw.imximeng.activitys.signIn;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.ErrorInfo;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserInfo;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.LanguageHelper;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.SharedPreferencesHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\25 0025
 */
public class SignInActivity extends BaseActivity {
    @BindView(R.id.et_account_number)
    EditText etAccountNumber;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_sign_in)
    TextView tvSignIn;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.tv_other_sign_in)
    TextView tvOtherSignIn;
    @BindView(R.id.iv_tencent)
    ImageView ivTencent;
    @BindView(R.id.iv_weChat)
    ImageView ivWeChat;
    @BindView(R.id.cb_visibility)
    CheckBox cbVisibility;

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            userLogin(platform.getName().toLowerCase(), data.get("uid"));
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast(t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            showToast("");
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.tv_sign_in, R.id.tv_register, R.id.tv_forget, R.id.iv_tencent, R.id.iv_weChat, R.id.cb_visibility})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_in:
                String phone = etAccountNumber.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (phone.isEmpty()) {
                    showToast("请输入手机号");
                    return;
                }
                if (password.isEmpty()) {
                    showToast("请输入密码");
                    return;
                }
                if (!StringUtils.isMobile(phone)) {
                    showToast("手机格式不正确");
                    return;
                }
                userLogin(phone, password, sharedPreferencesHelper.isSwitchLanguage());

                break;
            case R.id.tv_register:
                ActivityUtils.overlay(this, FastRegistrationActivity.class);
                break;
            case R.id.tv_forget:
                ActivityUtils.overlay(this, ForgetPasswordActivity.class);
                break;
            case R.id.iv_tencent:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.iv_weChat:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.cb_visibility:
                if (cbVisibility.isChecked()) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPassword.setSelection(etPassword.getText().toString().length());
                break;
        }
    }

    @Override
    protected void switchLanguage(boolean language) {
        LanguageHelper languageHelper = new LanguageHelper(this, language);
        etAccountNumber.setHint(languageHelper.getAccountNumber());
        etPassword.setHint(languageHelper.getPassword());
        tvSignIn.setText(languageHelper.getSignIn());
        tvRegister.setText(languageHelper.getRegister());
        tvForget.setText(languageHelper.getForget());
        tvOtherSignIn.setText(languageHelper.getOtherSignIn());

        etAccountNumber.setText(new SharedPreferencesHelper(this).getUserPhone());
        etAccountNumber.setSelection(etAccountNumber.getText().toString().length());
    }

    @OnTextChanged({R.id.et_account_number, R.id.et_password})
    public void onTextChanged(CharSequence c) {
        if (!etAccountNumber.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
            tvSignIn.setBackgroundResource(R.drawable.shape_sign_in_btn_enable);
            tvSignIn.setEnabled(true);
        } else {
            tvSignIn.setBackgroundResource(R.drawable.shape_sign_in_btn);
            tvSignIn.setEnabled(false);
        }
    }

    private void userLogin(final String phone, final String password, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.USER_LOGIN)
                .addParams("phone", phone)
                .addParams("password", password)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string().replace("\"data\":[]", "\"data\":{}");
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                closeProgressBar();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                closeProgressBar();
                showToast(response.getMessage());
                if (response.getStatus() == 1) {
                    SharedPreferencesHelper sph = new SharedPreferencesHelper(getApplicationContext());
                    sph.setUserPhone(phone);
                    sph.setUserPassword(password);

                    String data = new Gson().toJson(response.getData());
                    BaseApplication.userInfo = new Gson().fromJson(data, UserInfo.class);
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setMsgCode(MessageEvent.MessageType.REFRESH_MAIN);
                    EventBus.getDefault().post(messageEvent);

                    finish();
                }
            }
        });
    }

    private void userLogin(final String type, final String key) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.KEY_LOGIN)
                .addParams("type", type)
                .addParams("key", key)
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                closeProgressBar();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                closeProgressBar();
                if (response.getStatus() == 1) {
                    SharedPreferencesHelper sph = new SharedPreferencesHelper(getApplicationContext());
                    sph.setThirdInfo(type, key);

                    String data = new Gson().toJson(response.getData());
                    BaseApplication.userInfo = new Gson().fromJson(data, UserInfo.class);
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setMsgCode(MessageEvent.MessageType.REFRESH_MAIN);
                    EventBus.getDefault().post(messageEvent);

                    finish();
                }else if (response.getStatus() == 0){
                    String data = new Gson().toJson(response.getData());
                    ErrorInfo error = new Gson().fromJson(data, ErrorInfo.class);
                    if (error.getError_code().equals("00002")){
                        Bundle bundle = new Bundle();
                        bundle.putString(ActivityExtras.EXTRAS_THIRD_PARTY_TYPE, type);
                        bundle.putString(ActivityExtras.EXTRAS_THIRD_PARTY_KEY, key);
                        ActivityUtils.overlay(SignInActivity.this, FastRegistrationActivity.class, bundle);
                    }
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_up, R.anim.out_to_down);
    }
}
