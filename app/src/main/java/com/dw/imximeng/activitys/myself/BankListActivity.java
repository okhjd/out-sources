package com.dw.imximeng.activitys.myself;

import android.util.Log;
import android.widget.ListView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.BankListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Bank;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @create-time 2018-07-04 11:13:34
 */
public class BankListActivity extends BaseActivity {

    @BindView(R.id.lv_bank_card)
    ListView lvBankCard;
    private List<Bank> list = new ArrayList<>();
    private BankListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_list;
    }

    @Override
    public void initView() {
        setTitle("银行列表");
        adapter = new BankListAdapter(this, list, R.layout.item_bank);
        lvBankCard.setAdapter(adapter);
    }

    @Override
    public void initData() {
        getBankList(sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnItemClick(R.id.lv_bank_card)
    public void onItemClick(int position){
        ActivityUtils.setResult(this, RESULT_OK, list.get(position));
    }

    private void getBankList(boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.BANK_LIST)
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
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    List<Bank> bankList = new Gson().fromJson(data, new TypeToken<List<Bank>>(){}.getType());
                    list.clear();
                    list.addAll(bankList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
