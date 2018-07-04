package com.dw.imximeng.activitys.myself;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.MyClickButton;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\1 0001
 */
public class PrivacySetActivity extends BaseActivity {
    @BindView(R.id.tv_phone_privacy_title)
    TextView tvPhonePrivacyTitle;
    @BindView(R.id.sc_phone)
    MyClickButton scPhone;
    @BindView(R.id.tv_user_info_title)
    TextView tvUserInfoTitle;
    @BindView(R.id.sc_user_info)
    MyClickButton scUserInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_privacy;
    }

    @Override
    public void initView() {
        setTitle("隐私设置");

        scPhone.setOnMbClickListener(new MyClickButton.OnMClickListener() {
            @Override
            public void onClick(boolean isRight) {
                String type = MethodHelper.PRIVACY_PHONE_NUM;
                if (type.isEmpty()) {
                    return;
                }
                postPrivacySet(BaseApplication.userInfo.getSessionid(), type, isRight ? "1" : "2", sharedPreferencesHelper.isSwitchLanguage());
            }
        });
        scUserInfo.setOnMbClickListener(new MyClickButton.OnMClickListener() {
            @Override
            public void onClick(boolean isRight) {
                String type = MethodHelper.PRIVACY_HOME_PAGE;
                if (type.isEmpty()) {
                    return;
                }
                postPrivacySet(BaseApplication.userInfo.getSessionid(), type, isRight ? "1" : "2", sharedPreferencesHelper.isSwitchLanguage());
            }
        });
    }

    @Override
    public void initData() {
        if (BaseApplication.userInfo.getSessionid() != null) {
            if (BaseApplication.userInfo.getNumopen().equals("1")) {//开启
                scPhone.setRight(true);
            } else if (BaseApplication.userInfo.getNumopen().equals("2")) {//关闭
                scPhone.setRight(false);
            }

            if (BaseApplication.userInfo.getHpageopen().equals("1")) {//开启
                scUserInfo.setRight(true);
            } else if (BaseApplication.userInfo.getHpageopen().equals("2")) {//关闭
                scUserInfo.setRight(false);
            }
        }

    }

    private void postPrivacySet(String sessionid, final String type, final String value, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.PRIVACY_SET)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("setkey", type)
                .addParams("setval", value)
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
                    if (type.equals(MethodHelper.PRIVACY_PHONE_NUM)){
                        BaseApplication.userInfo.setNumopen(value);
                    }
                    if (type.equals(MethodHelper.PRIVACY_HOME_PAGE)){
                        BaseApplication.userInfo.setHpageopen(value);
                    }

                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setMsgCode(MessageEvent.MessageType.SIGN_IN);
                    EventBus.getDefault().post(messageEvent);
                }
            }
        });
    }

    @OnClick({R.id.sc_phone, R.id.sc_user_info})
    public void onClick(View view) {
        String type = "";
        boolean isChecked = false;
        switch (view.getId()) {
            case R.id.sc_phone:
                isChecked = scPhone.isRight();
                type = MethodHelper.PRIVACY_PHONE_NUM;
                break;
            case R.id.sc_user_info:
                isChecked = scUserInfo.isRight();
                type = MethodHelper.PRIVACY_HOME_PAGE;
                break;
        }
        if (type.isEmpty()) {
            return;
        }
        postPrivacySet(BaseApplication.userInfo.getSessionid(), type, isChecked ? "1" : "2", sharedPreferencesHelper.isSwitchLanguage());
    }
}
