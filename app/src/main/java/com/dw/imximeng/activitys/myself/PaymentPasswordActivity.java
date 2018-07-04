package com.dw.imximeng.activitys.myself;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.SetPaymentAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.MyCountDownTimer;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.ItemPasswordLayout;
import com.dw.imximeng.widgets.ViewPagerSlide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnTouch;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @create-time 2018-07-03 16:05:46
 */
public class PaymentPasswordActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    //    @BindView(R.id.item_password)
//    ItemPasswordLayout itemPassword;
    @BindView(R.id.viewpager)
    ViewPagerSlide viewpager;

    private int messageCode;
    private String paymentPassword;
    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_password;
    }

    @Override
    public void initView() {
        setTitle("支付密码");
        viewpager.addOnPageChangeListener(this);
        setupViewPager(viewpager);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setupViewPager(ViewPager viewPager) {
        SetPaymentAdapter adapter = new SetPaymentAdapter(getViews());
        viewPager.setAdapter(adapter);
    }

    private List<View> getViews(){
        List<View> views = new ArrayList<>();
        views.add(setPhone());
        views.add(setPaymentPassword());
        views.add(setPaymentPasswordRepeat());
        return views;
    }

    private View setPhone(){
        View view = LayoutInflater.from(this).inflate(R.layout.item_payment_phone, null);
        final TextView etAccountNum = (TextView)view.findViewById(R.id.et_account_number);
        final EditText etVerificationCode = (EditText)view.findViewById(R.id.et_verification_code);

        TextView tvVerificationCode = (TextView)view.findViewById(R.id.tv_verification_code);

        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000, tvVerificationCode);

        etAccountNum.setText(BaseApplication.userInfo.getPhone());

        tvVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etAccountNum.getText().toString().trim();
                if (phone.isEmpty()) {
                    showToast("请输入手机号");
                    return;
                }
                if (!StringUtils.isMobile(phone)) {
                    showToast("手机格式不正确");
                    return;
                }

                myCountDownTimer.start();

                verificationCode(phone, sharedPreferencesHelper.isSwitchLanguage());
            }
        });
        TextView tvNext = (TextView)view.findViewById(R.id.tv_next);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etAccountNum.getText().toString().trim();
                String code = etVerificationCode.getText().toString().trim();
                if (code.isEmpty()) {
                    showToast("请输入验证码");
                    return;
                }
                if (!code.equals(messageCode + "")) {
                    showToast("验证码不正确");
                    return;
                }
                if (!StringUtils.isMobile(phone)) {
                    showToast("手机格式不正确");
                    return;
                }
                viewpager.setCurrentItem(1);
            }
        });
        return view;
    }

    private View setPaymentPassword(){
        View view = LayoutInflater.from(this).inflate(R.layout.item_payment_password, null);
        final ItemPasswordLayout itemPasswordLayout = (ItemPasswordLayout)view.findViewById(R.id.item_password);
        itemPasswordLayout.setOnInputFinishListener(new ItemPasswordLayout.OnInputFinishListener() {

            @Override
            public void onInputFinish(String password) {

            }
        });
        TextView tvNext = (TextView)view.findViewById(R.id.tv_next);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemPasswordLayout.getText().toString().isEmpty()){
                    paymentPassword = itemPasswordLayout.getText().toString();
                    viewpager.setCurrentItem(2);
                }
            }
        });
        return view;
    }

    private View setPaymentPasswordRepeat(){
        View view = LayoutInflater.from(this).inflate(R.layout.item_payment_password_repeat, null);
        final ItemPasswordLayout itemPasswordLayout = (ItemPasswordLayout)view.findViewById(R.id.item_password);
        itemPasswordLayout.setOnInputFinishListener(new ItemPasswordLayout.OnInputFinishListener() {

            @Override
            public void onInputFinish(String password) {

            }
        });
        TextView tvNext = (TextView)view.findViewById(R.id.tv_complete);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemPasswordLayout.getText().toString().isEmpty() &&
                        itemPasswordLayout.getText().toString().equals(paymentPassword)){
                    setPayPassword(BaseApplication.userInfo.getSessionid(), paymentPassword, itemPasswordLayout.getText().toString());
                }
            }
        });
        return view;
    }

    //禁止viewpager滑动
    @OnTouch(R.id.viewpager)
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private void verificationCode(final String phone, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.USER_ID_CODE)
                .addParams("phone", phone)
                .addParams("cate", MethodHelper.TYPE_PAY)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
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
                showToast(response.getMessage() + messageCode);
                if (response.getStatus() == 1) {
                    messageCode = (int)((double)response.getData());
                }
            }
        });
    }

    private void setPayPassword(final String sessionid, String paypword, String suerpass) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.SET_PAY_PASSWORD)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("paypword", paypword)
                .addParams("suerpass",suerpass)
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
                    BaseApplication.userInfo.setIspaypwd(true);
                    ActivityUtils.setResult(PaymentPasswordActivity.this, RESULT_OK,"");
                }
            }
        });
    }
}
