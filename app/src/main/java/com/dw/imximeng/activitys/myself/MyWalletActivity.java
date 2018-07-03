package com.dw.imximeng.activitys.myself;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.MyWalletListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MyPoints;
import com.dw.imximeng.bean.MyWallets;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AutoListView;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\3 0003
 */
public class MyWalletActivity extends BaseActivity implements AutoListView.OnRefreshListener {
    @BindView(R.id.lv_details)
    AutoListView lvDetails;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;

    private List<MyWallets.Wallet> list = new ArrayList<>();
    private MyWalletListAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#696f77"), true);
        setTitle("我的钱包");

        adapter = new MyWalletListAdapter(this, list, R.layout.item_wallet_details);
        lvDetails.setAdapter(adapter);

        lvDetails.setOnRefreshListener(this);
        lvDetails.firstOnRefresh();
    }

    @Override
    public void initData() {

    }

    private void getWallets(String sessionid, int cPage, boolean language) {
        OkHttpUtils.post().url(MethodHelper.USER_WALLET)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("cpage", String.valueOf(cPage))
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                lvDetails.onRefreshFailue();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                lvDetails.onRefreshComplete();
                lvDetails.onLoadComplete();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    MyWallets resultList = new Gson().fromJson(data, MyWallets.class);

                    list.clear();

//                    lvDetails.setResultPage(1, resultList.getList().size());
                    list.addAll(resultList.getList());
                    adapter.notifyDataSetChanged();

                    tvTotalNum.setText("￥" + resultList.getBalance());
                } else {
                    lvDetails.onRefreshFailue();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getWallets(BaseApplication.userInfo.getSessionid(), 1, sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnClick({R.id.tv_record, R.id.tv_bank_card})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_record:
                ActivityUtils.overlay(this, WalletRecordActivity.class);
                break;
            case R.id.tv_bank_card:
                ActivityUtils.overlay(this, MyBankCardActivity.class);
                break;
        }
    }
}
