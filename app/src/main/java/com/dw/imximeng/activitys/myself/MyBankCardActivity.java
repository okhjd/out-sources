package com.dw.imximeng.activitys.myself;

import android.util.Log;
import android.widget.ListView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.BankCardListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.BankCard;
import com.dw.imximeng.bean.MyWallets;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
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
public class MyBankCardActivity extends BaseActivity {
    @BindView(R.id.lv_bank_card)
    ListView lvBankCard;

    private List<BankCard> list = new ArrayList<>();
    private BankCardListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_card;
    }

    @Override
    public void initView() {
        setTitle("我的银行卡");

        adapter = new BankCardListAdapter(this, list, R.layout.item_bank_card);
        lvBankCard.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        getBankCard(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnClick(R.id.tv_add_bank_card)
    public void onClick() {
    }

    private void getBankCard(String sessionid, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.BANK_CARD_LIST)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
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
                    String data = new Gson().toJson(response.getData());
                    List<BankCard> resultList = new Gson().fromJson(data, new TypeToken<List<BankCard>>(){}.getType());

                    list.clear();
                    list.addAll(resultList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
