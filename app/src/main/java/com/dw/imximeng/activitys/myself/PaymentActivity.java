package com.dw.imximeng.activitys.myself;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dw.imximeng.R;
import com.dw.imximeng.adapters.PaymentListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.PayResult;
import com.dw.imximeng.bean.Payment;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.Wxinfo;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MD5;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\9 0009
 */
public class PaymentActivity extends BaseActivity {

    @BindView(R.id.lv_payment)
    ListView lvPayment;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    private IWXAPI api;

    private PaymentListAdapter adapter;
    private List<Payment.PtypeListBean> mList = new ArrayList<>();
    private String price;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        price = ActivityUtils.getStringExtra(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    public void initView() {
        setTitle("支付方式");

        tvPrice.setText(price);

        adapter = new PaymentListAdapter(this, mList, R.layout.item_payment);
        lvPayment.setAdapter(adapter);
    }

    @Override
    public void initData() {
        api = WXAPIFactory.createWXAPI(this, null);
        payment(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
    }

    private void recharge(String sessionid, String price, final String type) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.RECHARGE_SUBMIT)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("oprice", price)
                .addParams("ptype", type)
                .build().execute(new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                closeProgressBar();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(String string, int id) {
                closeProgressBar();
                if (type.equals("wxpay")) {
                    Wxinfo wxinfo = new Gson().fromJson(string, Wxinfo.class);
                    PayReq req = new PayReq();
                    api.registerApp(wxinfo.getData().getAppid());
                    req.appId = wxinfo.getData().getAppid();
                    req.partnerId = wxinfo.getData().getPartnerid();
                    req.prepayId = wxinfo.getData().getPrepayid();
                    req.nonceStr = wxinfo.getData().getNoncestr();
                    req.timeStamp = wxinfo.getData().getTimestamp();
                    req.packageValue = wxinfo.getData().getPackageX();
                    req.sign = wxinfo.getData().getPaysign();
                    req.extData = "app data"; // optional

                    api.sendReq(req);
                }else if(type.equals("alipay")){
                    Result result = new Gson().fromJson(string, Result.class);
                    alipay(result.getData().toString());
                }
            }
        });
    }
    private static final int SDK_PAY_FLAG = 1001;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
            String resultInfo = payResult.getResult();
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                showToast("支付成功");
                finish();
            } else {
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {

                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                   showToast("支付失败");
                }
            }
        };
    };
    private void alipay(final String orderInfo){
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PaymentActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(orderInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @OnClick(R.id.tv_sure_recharge)
    public void onClick() {
//        sendPayRequest();
        if (tvPrice.getText().toString().isEmpty()) {
            showToast("请输入金额");
            return;
        }
        if (adapter.checkCode() == null) {
            showToast("请选择支付方式");
            return;
        }
        if (adapter.checkCode().toLowerCase().equals("wxpay") && !isWeiXinInstalled()){
            showToast("微信未安装");
            return;
        }
        recharge(BaseApplication.userInfo.getSessionid(), tvPrice.getText().toString(), adapter.checkCode());
    }

    private void payment(String sessionid, boolean language) {
        OkHttpUtils.post().url(MethodHelper.PAYMENT)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    Payment payment = new Gson().fromJson(data, Payment.class);

                    mList.clear();
                    mList.addAll(payment.getPtypeList());
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    @OnItemClick(R.id.lv_payment)
    public void onItemClick(int position) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setCheck(false);
        }
        mList.get(position).setCheck(!mList.get(position).isCheck());
        adapter.notifyDataSetChanged();
    }

    private boolean isWeiXinInstalled(){
        if(api.isWXAppInstalled() && api.isWXAppSupportAPI()) {
            return true;
        } else {
            final PackageManager packageManager = getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equalsIgnoreCase("com.tencent.mm")) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
