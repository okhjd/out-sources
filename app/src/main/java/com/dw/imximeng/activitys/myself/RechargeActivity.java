package com.dw.imximeng.activitys.myself;

import android.view.View;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.helper.ActivityUtils;

import butterknife.OnClick;

/**
 * @author hjd
 * @Created_Time 2018\7\9 0009
 */
public class RechargeActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initView() {
        setTitle("在线充值");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_recharge:
                ActivityUtils.overlay(this, PaymentActivity.class);
                break;
        }
    }
}
