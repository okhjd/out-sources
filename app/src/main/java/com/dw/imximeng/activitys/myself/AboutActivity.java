package com.dw.imximeng.activitys.myself;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.WebActivity;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.UserSiteInfo;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;

    private String webSite = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        setTitle("关于我们");
        tvVersionName.setText(getAppVersionName());
    }

    @Override
    public void initData() {
        getUserSiteInfo(sharedPreferencesHelper.isSwitchLanguage());
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
                    webSite = response.getData().getWebsite_url();
                }
            }
        });
    }

    @OnClick({R.id.tv_web_site})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_web_site:
                Bundle bundle = new Bundle();
                bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, webSite);
                bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, "企业官网");
                ActivityUtils.overlay(this, WebActivity.class, bundle);
                break;
        }
    }
}
