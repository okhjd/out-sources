package com.dw.imximeng.activitys.myself;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Bank;
import com.dw.imximeng.bean.BankCard;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityForResultCode;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.ItemPasswordLayout;
import com.dw.imximeng.widgets.PayPasswordDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\9 0009
 */
public class PutForwardActivity extends BaseActivity {
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tv_balance)
    TextView tvBalance;

    private BankCard bankCard;
    @Override
    public int getLayoutId() {
        return R.layout.activity_put_forward;
    }

    @Override
    public void initView() {
        setTitle("提现申请");
    }

    @Override
    public void initData() {
        tvBalance.setText("钱包余额￥" + BaseApplication.userInfo.getBalance());
    }

    @OnClick({R.id.tv_bank, R.id.tv_all_put, R.id.tv_put_forward})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_bank:
                ActivityUtils.startForResult(this, MyBankCardActivity.class, ActivityForResultCode.BNAK_CODE, ActivityExtras.EXTRAS_MY_BANK_CARD_TYPE);
                break;
            case R.id.tv_all_put:
                etPrice.setText(BaseApplication.userInfo.getBalance());
                break;
            case R.id.tv_put_forward:
                final String price = etPrice.getText().toString();
                if (bankCard == null){
                    showToast("请选择银行卡");
                    return;
                }
                if (price.isEmpty()){
                    showToast("请填写金额");
                    return;
                }
                new PayPasswordDialog(this).builder().setPositiveButton(new ItemPasswordLayout.OnInputFinishListener() {
                    @Override
                    public void onInputFinish(String password) {
                        putForward(BaseApplication.userInfo.getSessionid(), password, String.valueOf(bankCard.getId()), price);
                    }
                }).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case ActivityForResultCode.BNAK_CODE:
                    bankCard = ActivityUtils.getParcelableExtra(data);
                    tvBank.setText(bankCard.getBankName()+"(" + bankCard.getLastNumber() +")");
                    break;
            }
        }
    }

    private void putForward(String sessionid, String paypwd, String bankid, String price) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.PUT_FORWARD)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("paypwd", paypwd)
                .addParams("bankid", bankid)
                .addParams("price", price)
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
                    finish();
                }
            }
        });
    }
}
