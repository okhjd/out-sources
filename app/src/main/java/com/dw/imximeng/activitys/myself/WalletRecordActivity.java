package com.dw.imximeng.activitys.myself;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.MyWalletListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MyWallets;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AutoListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
public class WalletRecordActivity extends BaseActivity implements  AutoListView.OnRefreshListener, AutoListView.OnLoadListener{
    @BindView(R.id.lv_details)
    AutoListView lvDetails;

    private List<MyWallets.Wallet> list = new ArrayList<>();
    private MyWalletListAdapter adapter;
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_record;
    }

    @Override
    public void initView() {
        setTitle("余额明细");

        adapter = new MyWalletListAdapter(this, list, R.layout.item_wallet_details);
        lvDetails.setAdapter(adapter);

        lvDetails.setOnRefreshListener(this);
        lvDetails.firstOnRefresh();
    }

    @Override
    public void initData() {

    }

    private void getWallets(String sessionid, int cPage, boolean language) {
        OkHttpUtils.post().url(MethodHelper.WALLET_RECORD)
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
                    List<MyWallets.Wallet> resultList = new Gson().fromJson(data,new TypeToken<List<MyWallets.Wallet>>(){}.getType());

                    if (page ==1) {
                        list.clear();
                    }

                    lvDetails.setResultPage(page, resultList.size());
                    list.addAll(resultList);
                    adapter.notifyDataSetChanged();
                } else {
                    lvDetails.onRefreshFailue();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        getWallets(BaseApplication.userInfo.getSessionid(), page, sharedPreferencesHelper.isSwitchLanguage());
    }
    @Override
    public void onLoad() {
        page++;
        getWallets(BaseApplication.userInfo.getSessionid(), page, sharedPreferencesHelper.isSwitchLanguage());
    }
}
