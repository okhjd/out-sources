package com.dw.imximeng.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.dw.imximeng.MainActivity;
import com.dw.imximeng.R;
import com.dw.imximeng.activitys.signIn.SignInActivity;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserInfo;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class LoadActivity extends BaseActivity {
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //设置全屏
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_load;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
                if (sharedPreferencesHelper.getThirdKey() == null && sharedPreferencesHelper.getThirdType() == null) {
                    userLogin(sharedPreferencesHelper.getUserPhone(), sharedPreferencesHelper.getUserPassword(), sharedPreferencesHelper.isSwitchLanguage());
                }else {
                    userLogin(sharedPreferencesHelper.getThirdType(), sharedPreferencesHelper.getThirdKey());
                }
            }
        }, 300);
    }

    private void userLogin(final String phone, final String password, boolean language) {
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
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
                Intent intent = new Intent();
                if (sharedPreferencesHelper.isFirstTimeLaunch()) {
                    intent.setClass(getApplicationContext(), GuideActivity.class);
                } else {
                    intent.setClass(getApplicationContext(), SignInActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponse(Result response, int id) {
                Intent intent = new Intent();
                if (response.getStatus() == 1) {
                    SharedPreferencesHelper sph = new SharedPreferencesHelper(getApplicationContext());
                    sph.setUserPhone(phone);
                    sph.setUserPassword(password);

                    String data = new Gson().toJson(response.getData());
                    BaseApplication.userInfo = new Gson().fromJson(data, UserInfo.class);

                    intent.setClass(getApplicationContext(), MainActivity.class);
                } else {
                    if (sharedPreferencesHelper.isFirstTimeLaunch()) {
                        intent.setClass(getApplicationContext(), GuideActivity.class);
                    } else {
                        intent.setClass(getApplicationContext(), MainActivity.class);
                    }
                }
                startActivity(intent);
                finish();
            }
        });
    }

    private void userLogin(final String type, final String key) {
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
                Intent intent = new Intent();
                if (sharedPreferencesHelper.isFirstTimeLaunch()) {
                    intent.setClass(getApplicationContext(), GuideActivity.class);
                } else {
                    intent.setClass(getApplicationContext(), SignInActivity.class);
                }
                startActivity(intent);
                finish();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                Intent intent = new Intent();
                if (response.getStatus() == 1) {
                    SharedPreferencesHelper sph = new SharedPreferencesHelper(getApplicationContext());
                    sph.setThirdInfo(type, key);

                    String data = new Gson().toJson(response.getData());
                    BaseApplication.userInfo = new Gson().fromJson(data, UserInfo.class);

                    intent.setClass(getApplicationContext(), MainActivity.class);
                } else {
                    if (sharedPreferencesHelper.isFirstTimeLaunch()) {
                        intent.setClass(getApplicationContext(), GuideActivity.class);
                    } else {
                        intent.setClass(getApplicationContext(), MainActivity.class);
                    }
                }
                startActivity(intent);
                finish();
            }
        });
    }
}
