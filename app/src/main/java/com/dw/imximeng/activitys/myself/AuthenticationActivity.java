package com.dw.imximeng.activitys.myself;

import android.util.Log;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.ItemPasswordLayout;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @create-time 2018-07-04 09:25:11
 */
public class AuthenticationActivity extends BaseActivity {
    @BindView(R.id.item_password)
    ItemPasswordLayout itemPassword;

    @Override
    public int getLayoutId() {
        return R.layout.activity_authentication;
    }

    @Override
    public void initView() {
        setTitle("身份验证");
    }

    @Override
    public void initData() {
        itemPassword.setOnInputFinishListener(new ItemPasswordLayout.OnInputFinishListener() {
            @Override
            public void onInputFinish(String password) {
                authentication(BaseApplication.userInfo.getSessionid(), password);
            }
        });
    }

    private void authentication(String sessionid, String paypwd) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.USER_IDVERIFICATION)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("paypwd", paypwd)
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
                    ActivityUtils.forward(AuthenticationActivity.this, PerfectInformationActivity.class);
                }
            }
        });
    }
}
