package com.dw.imximeng.activitys.myself;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.helper.ActivityUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hjd
 * @Created_Time 2018\7\9 0009
 */
public class RechargeActivity extends BaseActivity {
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.et_price)
    EditText etPrice;

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
        tvBalance.setText("当前余额：￥" + BaseApplication.userInfo.getBalance());
    }

    @OnClick({R.id.tv_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_recharge:
                String price = etPrice.getText().toString();
                if (price.isEmpty()){
                    showToast("请输入金额");
                    return;
                }
                if (Double.valueOf(price) < 0.01){
                    showToast("金额太低");
                    return;
                }
                ActivityUtils.overlay(this, PaymentActivity.class, price);
                break;
        }
    }
}
