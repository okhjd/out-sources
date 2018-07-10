package com.dw.imximeng.activitys.myself;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;

/**
 * 微信支付回调
 */
public class AlipayResultActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_wx_pay_result;
    }

    @Override
    public void initView() {
        setTitle("支付结果");
    }

    @Override
    public void initData() {

    }
}