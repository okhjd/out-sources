package com.dw.imximeng.activitys.myself;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.widgets.ItemPasswordLayout;

import butterknife.BindView;

/**
 * @author hjd
 * @create-time 2018-07-03 16:05:46
 */
public class PaymentPasswordActivity extends BaseActivity {
    @BindView(R.id.item_password)
    ItemPasswordLayout itemPassword;

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_password;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        itemPassword.setOnInputFinishListener(new ItemPasswordLayout.OnInputFinishListener() {

            @Override
            public void onInputFinish(String password) {

                showToast(password);
            }
        });
    }
}
