package com.dw.imximeng.activitys.myself;

import android.util.Log;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Wxinfo;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\9 0009
 */
public class PaymentActivity extends BaseActivity {

    private IWXAPI api;

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    public void initView() {
        setTitle("支付方式");
    }

    @Override
    public void initData() {
        //"wx0179314396d8efb8"
        api = WXAPIFactory.createWXAPI(this, null);
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

    private void recharge(String sessionid) {
        OkHttpUtils.post().url(MethodHelper.RECHARGE_SUBMIT)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("oprice", "0.01")
                .addParams("ptype", "wx")
                .build().execute(new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
//                return new Gson().fromJson(string, Wxinfo.class);
                return string;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {

            }
        });
    }

    @OnClick(R.id.tv_sure_recharge)
    public void onClick() {
//        sendPayRequest();
        recharge(BaseApplication.userInfo.getSessionid());
    }
}
