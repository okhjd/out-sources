package com.dw.imximeng.activitys.myself;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.WebActivity;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.UserInfo;
import com.dw.imximeng.helper.ActivityForResultCode;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.DataCleanManager;
import com.dw.imximeng.widgets.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class SetActivity extends BaseActivity {
    @BindView(R.id.tv_user_exit)
    TextView tvUserExit;
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.tv_not_set_pay)
    TextView tvNotSetPay;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void initView() {
        setTitle("设置");
        try {
            tvCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
            tvCacheSize.setText("0MB");
        }

        if (BaseApplication.userInfo.isIspaypwd()){
            tvNotSetPay.setText("修改");
        }else {
            tvNotSetPay.setText("未设置");
        }
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_user_exit, R.id.tv_about, R.id.tv_privacy, R.id.rl_clear_cahce, R.id.tv_help_center,
            R.id.tv_service_statement, R.id.tv_feedback, R.id.tv_online_service, R.id.rl_payment_password,
            R.id.rl_modify_password})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_user_exit:
                showDialog();
                break;
            case R.id.tv_about:
                ActivityUtils.overlay(this, AboutActivity.class);
                break;
            case R.id.tv_privacy:
                ActivityUtils.overlay(this, PrivacySetActivity.class);
                break;
            case R.id.rl_clear_cahce:
                DataCleanManager.clearAllCache(this);
                try {
                    tvCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
                } catch (Exception e) {
                    e.printStackTrace();
                    tvCacheSize.setText("0K");
                }
                break;
            case R.id.tv_help_center:
                bundle.clear();
                bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, BaseApplication.userSiteInfo.getData().getHelpcenter_url());
                bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, "帮助中心");
                ActivityUtils.overlay(this, WebActivity.class, bundle);
                break;
            case R.id.tv_service_statement://广告服务声明
                bundle.clear();
                bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, BaseApplication.userSiteInfo.getData().getStatement_url());
                bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, "广告服务声明");
                ActivityUtils.overlay(this, WebActivity.class, bundle);
                break;
            case R.id.tv_feedback:
                ActivityUtils.overlay(this, FeedbackActivity.class);
                break;
            case R.id.tv_online_service:
                ActivityUtils.overlay(this, OnlineServiceActivity.class);
                break;
            case R.id.rl_payment_password:
                ActivityUtils.overlay(this, PaymentPasswordActivity.class, ActivityForResultCode.SET_PAY_PASSWORD);
                break;
            case R.id.rl_modify_password:
                ActivityUtils.overlay(this, ModifyPasswordActivity.class);
                break;
        }
    }

    private void showDialog() {
        new AlertDialog(this)
                .builder()
                .setMsg("是否注销该账号")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseApplication.userInfo = new UserInfo();
                        sharedPreferencesHelper.setUserPassword("");
                        MessageEvent messageEvent = new MessageEvent();
                        messageEvent.setMsgCode(MessageEvent.MessageType.REFRESH_MAIN);
                        EventBus.getDefault().post(messageEvent);
                        finish();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == ActivityForResultCode.SET_PAY_PASSWORD){
                if (BaseApplication.userInfo.isIspaypwd()){
                    tvNotSetPay.setText("修改");
                }else {
                    tvNotSetPay.setText("未设置");
                }
            }
        }
    }
}
