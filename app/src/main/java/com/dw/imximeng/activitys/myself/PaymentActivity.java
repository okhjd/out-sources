package com.dw.imximeng.activitys.myself;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.PaymentListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Payment;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.Wxinfo;
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    public void initView() {
        setTitle("支付方式");

        adapter = new PaymentListAdapter(this, mList, R.layout.item_payment);
        lvPayment.setAdapter(adapter);
    }

    @Override
    public void initData() {
        //"wx0179314396d8efb8"
        api = WXAPIFactory.createWXAPI(this, null);
        payment(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
    }

    private void sendPayRequest() {
        String url = "https://wxpay.wxutil.com/pub_v2/app/app_pay.php";
        OkHttpUtils.post().url(url)
                .build().execute(new Callback<Wxinfo>() {
            @Override
            public Wxinfo parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Wxinfo.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Wxinfo response, int id) {
//                JSONObject json = null;
//                try {
//                    json = new JSONObject(response);
//                    if (null != json && !json.has("retcode")) {
                PayReq req = new PayReq();
                req.appId = response.getAppid();//json.getString("appid");
                req.partnerId = response.getPartnerid();//json.getString("partnerid");
                req.prepayId = response.getPrepayid();//json.getString("prepayid");
                req.nonceStr = response.getNoncestr();//json.getString("noncestr");
                req.timeStamp = response.getTimestamp();//json.getString("timestamp");
                req.packageValue = response.getPackageX();//json.getString("package");
                req.sign = response.getSign();//json.getString("sign");
                req.extData = "app data"; // optional

                api.sendReq(req);
//                    } else {
//                        Log.d("PAY_GET", "���ش���" + json.getString("retmsg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        });
    }

    private void recharge(String sessionid, String price, String type) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.RECHARGE_SUBMIT)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("oprice", price)
                .addParams("ptype", type)
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
                if (response.getStatus() == 1){
                    String data = new Gson().toJson(response.getData());
                    Wxinfo wxinfo = new Gson().fromJson(data, Wxinfo.class);

                    PayReq req = new PayReq();
                    api.registerApp("wx0179314396d8efb8");
                    req.appId = wxinfo.getAppid();//json.getString("appid");
                    req.partnerId = wxinfo.getPartnerid();//json.getString("partnerid");
                    req.prepayId = wxinfo.getPrepayid();//json.getString("prepayid");
                    req.nonceStr = wxinfo.getNoncestr();//json.getString("noncestr");
                    req.timeStamp = wxinfo.getTimestamp();//json.getString("timestamp");
                    req.packageValue = wxinfo.getPackageX();//json.getString("package");
//                    req.sign = wxinfo.getSign();//json.getString("sign");
                    req.sign =  signNum(wxinfo.getPartnerid(),wxinfo.getPrepayid(),wxinfo.getPackageX(),wxinfo.getNoncestr(),
                            wxinfo.getTimestamp(),"1my4JfWSX85ium9bKu5nI22SUr6xVgOm");
                    req.extData = "app data"; // optional

                    api.sendReq(req);
                }
            }
        });
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

    private String signNum(String partnerId,String prepayId,String packageValue,String nonceStr,String timeStamp,String key){
        String stringA=
                "appid="+"wx0179314396d8efb8"
                        +"&noncestr="+nonceStr
                        +"&package="+packageValue
                        +"&partnerid="+partnerId
                        +"&prepayid="+prepayId
                        +"&timestamp="+timeStamp;


        String stringSignTemp = stringA+"&key="+key;
        String sign = MD5.getMessageDigest(stringSignTemp.getBytes()).toUpperCase();
        return  sign;
    }
}
