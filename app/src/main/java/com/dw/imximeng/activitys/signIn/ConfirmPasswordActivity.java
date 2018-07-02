package com.dw.imximeng.activitys.signIn;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.MainActivity;
import com.dw.imximeng.R;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.app.AppManager;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserInfo;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.SharedPreferencesHelper;
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
public class ConfirmPasswordActivity extends BaseActivity {
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_sure_password)
    EditText etSurePassword;
    @BindView(R.id.cb_visibility)
    CheckBox cbVisibility;
    @BindView(R.id.cb_sure_visibility)
    CheckBox cbSureVisibility;

    private String phone;
    private boolean isRegister = true;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        phone = bundle.getString(ActivityExtras.EXTRAS_SET_PASSWORD_PHONE);
        isRegister = bundle.getBoolean(ActivityExtras.EXTRAS_IS_REGISTER, true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_password;
    }

    @Override
    public void initView() {
        setTitle(isRegister ? "快速注册" : "找回密码");
    }

    @Override
    public void initData() {

    }

    @OnTextChanged({R.id.et_password, R.id.et_sure_password})
    public void onTextChanged(CharSequence c) {
        if (!etPassword.getText().toString().isEmpty() && !etSurePassword.getText().toString().isEmpty()) {
            tvComplete.setBackgroundResource(R.drawable.shape_sign_in_btn_enable);
            tvComplete.setEnabled(true);
        } else {
            tvComplete.setBackgroundResource(R.drawable.shape_sign_in_btn);
            tvComplete.setEnabled(false);
        }
    }

    @OnClick({R.id.tv_complete, R.id.cb_visibility, R.id.cb_sure_visibility})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_complete:
                String password = etPassword.getText().toString().trim();
                String surePassword = etSurePassword.getText().toString().trim();
                if (password.isEmpty()) {
                    showToast("请输入密码");
                    return;
                }
                if (surePassword.isEmpty()) {
                    showToast("请再次输入密码");
                    return;
                }
                if (!password.equals(surePassword)) {
                    showToast("密码不一致");
                    return;
                }
                if (isRegister) {
                    registerUser(phone, password, surePassword, sharedPreferencesHelper.isSwitchLanguage());
                } else {
                    forgetPassword(phone, password, surePassword, sharedPreferencesHelper.isSwitchLanguage());
                }

                break;
            case R.id.cb_visibility:
                if (cbVisibility.isChecked()) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPassword.setSelection(etPassword.getText().toString().length());
                break;
            case R.id.cb_sure_visibility:
                if (cbSureVisibility.isChecked()) {
                    etSurePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etSurePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etSurePassword.setSelection(etSurePassword.getText().toString().length());
                break;
        }
    }

    private void registerUser(final String phone, final String password, String surePassword, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.USER_REGISTER)
                .addParams("phone", phone)
                .addParams("password", password)
                .addParams("suerpass", surePassword)
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
                    userLogin(phone, password, sharedPreferencesHelper.isSwitchLanguage());
                }
            }
        });
    }

    private void forgetPassword(final String phone, final String password, String surePassword, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.FORGET_PASSWORD)
                .addParams("phone", phone)
                .addParams("password", password)
                .addParams("suerpass", surePassword)
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
                    userLogin(phone, password, sharedPreferencesHelper.isSwitchLanguage());
                }
            }
        });
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
                    SharedPreferencesHelper sph = new SharedPreferencesHelper(getApplicationContext());
                    sph.setUserPhone(phone);
                    sph.setUserPassword(password);

                    String data = new Gson().toJson(response.getData());
                    BaseApplication.userInfo = new Gson().fromJson(data, UserInfo.class);

                    ActivityUtils.forward(getApplicationContext(), MainActivity.class);
                    AppManager.getAppManager().finishActivity(SignInActivity.class);
                    finish();
                }
            }
        });
    }
}
