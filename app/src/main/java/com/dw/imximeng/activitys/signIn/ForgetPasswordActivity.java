package com.dw.imximeng.activitys.signIn;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Result;
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
public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.et_account_number)
    EditText etAccountNumber;
    @BindView(R.id.et_verification_code)
    EditText etVerificationCode;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;

    private int messageCode;
    MyCountDownTimer myCountDownTimer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initView() {
        setTitle("忘记密码");
    }

    @Override
    public void initData() {
        myCountDownTimer = new MyCountDownTimer(60000, 1000, tvVerificationCode);
    }

    @OnClick({R.id.tv_next, R.id.tv_verification_code})
    public void onClick(View view) {
        String phone = etAccountNumber.getText().toString().trim();
        String code = etVerificationCode.getText().toString().trim();
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
                Bundle bundle = new Bundle();
                bundle.putString(ActivityExtras.EXTRAS_SET_PASSWORD_PHONE, phone);
                bundle.putBoolean(ActivityExtras.EXTRAS_IS_REGISTER, false);
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
                .addParams("cate", MethodHelper.TYPE_FORGET)
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
                    messageCode = (int) ((double) response.getData());
                }
            }
        });
    }
}
