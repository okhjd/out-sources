package com.dw.imximeng.activitys.signIn;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.WebActivity;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserSiteInfo;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.MyCountDownTimer;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\25 0025
 */
public class FastRegistrationActivity extends BaseActivity {
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.et_account_number)
    EditText etAccountNumber;
    @BindView(R.id.et_verification_code)
    EditText etVerificationCode;
    @BindView(R.id.cb_user_protocol)
    CheckBox cbUserProtocol;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;

    private int messageCode;
    MyCountDownTimer myCountDownTimer;

    private String userProtocol = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_fast_registration;
    }

    @Override
    public void initView() {
        setTitle("快速注册");
    }

    @Override
    public void initData() {
        myCountDownTimer = new MyCountDownTimer(60000, 1000, tvVerificationCode);
        getUserSiteInfo(sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnClick({R.id.tv_next, R.id.tv_verification_code, R.id.tv_user_protocol})
    public void onClick(View view) {
        String phone = etAccountNumber.getText().toString().trim();
        String code = etVerificationCode.getText().toString().trim();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_next:
                if (code.isEmpty()) {
                    showToast("请输入验证码");
                    return;
                }
                if (!code.equals(messageCode + "")) {
                    showToast("验证码不正确");
                    return;
                }
                if (!StringUtils.isMobile(phone)) {
                    showToast("手机格式不正确");
                    return;
                }
                if (!cbUserProtocol.isChecked()) {
                    showToast("请同意用户协议");
                    return;
                }
                bundle.clear();
                bundle.putString(ActivityExtras.EXTRAS_SET_PASSWORD_PHONE, phone);
                bundle.putBoolean(ActivityExtras.EXTRAS_IS_REGISTER, true);
                ActivityUtils.forward(this, ConfirmPasswordActivity.class, bundle);
                break;
            case R.id.tv_verification_code:
                if (phone.isEmpty()) {
                    showToast("请输入手机号");
                    return;
                }
                if (!StringUtils.isMobile(phone)) {
                    showToast("手机格式不正确");
                    return;
                }

                myCountDownTimer.start();

                verificationCode(phone, sharedPreferencesHelper.isSwitchLanguage());
                break;
            case R.id.tv_user_protocol://查看用户协议
                bundle.clear();
                bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, userProtocol);
                bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, "用户注册协议");
                ActivityUtils.overlay(this, WebActivity.class, bundle);
                break;
        }
    }

    @OnTextChanged({R.id.et_account_number, R.id.et_verification_code})
    public void onTextChanged(CharSequence c) {
        if (!etAccountNumber.getText().toString().isEmpty() && !etVerificationCode.getText().toString().isEmpty()) {
            tvNext.setBackgroundResource(R.drawable.shape_sign_in_btn_enable);
            tvNext.setEnabled(true);
        } else {
            tvNext.setBackgroundResource(R.drawable.shape_sign_in_btn);
            tvNext.setEnabled(false);
        }
    }

    private void verificationCode(final String phone, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.USER_ID_CODE)
                .addParams("phone", phone)
                .addParams("cate", MethodHelper.TYPE_REGISTER)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
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
                showToast(response.getMessage());
                if (response.getStatus() == 1) {
                    messageCode = (int)((double)response.getData());
                }
            }
        });
    }

    private void getUserSiteInfo(boolean language) {
        OkHttpUtils.post().url(MethodHelper.GET_USER_SITE_INFO)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .build().execute(new Callback<UserSiteInfo>() {
            @Override
            public UserSiteInfo parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, UserSiteInfo.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(UserSiteInfo response, int id) {
                if (response.getStatus() == 1) {
                    userProtocol = response.getData().getAgreement_url();
                }
            }
        });
    }
}
