package com.dw.imximeng.activitys.advertisements;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.ServiceSearchAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.ServiceSearch;
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
 * @Created_Time 2018\7\8 0008
 */
public class ServiceSearchActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.lv_search)
    ListView lvSearch;
    private String city = "";
    private String cateId = "";
    private ServiceSearchAdapter adapter;
    private List<ServiceSearch> list = new ArrayList<>();
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        city = bundle.getString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID);
        cateId = bundle.getString(ActivityExtras.EXTRAS_CATE_ID);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_service_search;
    }

    @Override
    public void initView() {
        etSearch.setOnEditorActionListener(this);
        adapter = new ServiceSearchAdapter(this, list, R.layout.item_service_search);
        lvSearch.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    private void getServiceList(final String area, String cid, String keywords, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.SERVICE_TITLE)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("area", area)
                .addParams("cid", cid)//分类ID（非必传，默认：0）
                .addParams("keywords", keywords)//搜索关键词（非必传）
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
                closeProgressBar();
            }

            @Override
            public void onResponse(Result response, int id) {
                closeProgressBar();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    List<ServiceSearch> dataBean = new Gson().fromJson(data, new TypeToken<List<ServiceSearch>>(){}.getType());

                    list.clear();
                    list.addAll(dataBean);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        /*判断是否是“SEARCH”键*/
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v
                    .getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }

            getServiceList(city, cateId, etSearch.getText().toString(), sharedPreferencesHelper.isSwitchLanguage());

            return true;
        }
        return false;
    }

    @OnItemClick(R.id.lv_search)
    public void onItemClick(int position){
        Bundle bundle = new Bundle();
        bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID, city);
        bundle.putString(ActivityExtras.EXTRAS_CATE_ID, cateId);
        bundle.putString(ActivityExtras.EXTRAS_KEY_WORD, list.get(position).getName());
        ActivityUtils.overlay(this, CityServiceSearchResultActivity.class, bundle);
    }
}
