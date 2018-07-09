package com.dw.imximeng.wxapi;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.OnClick;

/**
 * 微信支付回调
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    /**
     * 查询支付结果
     */
    private void queryPayResult() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wx_pay_result;
    }

    @Override
    public void initView() {
        api = WXAPIFactory.createWXAPI(this,"wx0179314396d8efb8");
        api.handleIntent(getIntent(), this);
        queryPayResult();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e("TAG", "onPayFinish, errCode = " + req.openId);
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

        }
    }
}