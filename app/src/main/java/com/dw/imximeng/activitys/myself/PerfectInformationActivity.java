package com.dw.imximeng.activitys.myself;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Bank;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityForResultCode;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @create-time 2018-07-04 09:42:05
 */
public class PerfectInformationActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.et_bank_card)
    EditText etBankCard;
    @BindView(R.id.tv_complete)
    TextView tvComplete;

    private Bank bank;
    @Override
    public int getLayoutId() {
        return R.layout.activity_perfect_info;
    }

    @Override
    public void initView() {
        setTitle("完善资料");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_bank, R.id.tv_complete, R.id.iv_picture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_bank:
                ActivityUtils.startForResult(this, BankListActivity.class, ActivityForResultCode.BNAK_CODE);
                break;
            case R.id.tv_complete:
                if (bank == null){
                    showToast("请选择银行");
                    return;
                }
                addBankCard(BaseApplication.userInfo.getSessionid(), bank.getCode(), etName.getText().toString().trim(),
                        etPhone.getText().toString().trim(), etBankCard.getText().toString().trim());
                break;
            case R.id.iv_picture:
                break;
        }
    }

    @OnTextChanged({R.id.et_name, R.id.et_phone, R.id.et_bank_card, R.id.tv_bank})
    public void onTextChanged(CharSequence c) {
        if (!etName.getText().toString().isEmpty() && !etPhone.getText().toString().isEmpty()
                && !etBankCard.getText().toString().isEmpty() && !tvBank.getText().toString().isEmpty()) {
            tvComplete.setBackgroundResource(R.drawable.shape_sign_in_btn_enable);
            tvComplete.setEnabled(true);
        } else {
            tvComplete.setBackgroundResource(R.drawable.shape_modify_btn);
            tvComplete.setEnabled(false);
        }
    }

    private void addBankCard(String sessionid, String code, String username, String telephone, String cnumber) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.ADD_BANK_CARD)
                .addParams("code", code)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("username", username)
                .addParams("telephone", telephone)
                .addParams("cnumber", cnumber)
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
                if (response.getStatus() == 1) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case ActivityForResultCode.BNAK_CODE:
                    bank = ActivityUtils.getParcelableExtra(data);
                    tvBank.setText(bank.getName());
                    break;
            }
        }
    }
}
