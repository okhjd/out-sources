package com.dw.imximeng.activitys.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.advertisements.InformationDetailsActivity;
import com.dw.imximeng.adapters.MyCollectionAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MyCollection;
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
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\30 0030
 */
public class MyCollectionActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {
    @BindView(R.id.alv_collection)
    AutoListView alvCollection;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    private int page = 1;
    private List<MyCollection> list = new ArrayList<>();
    private MyCollectionAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_collection;
    }

    @Override
    public void initView() {
        setTitle("我的收藏");
        alvCollection.setOnRefreshListener(this);
        alvCollection.setOnLoadListener(this);
        alvCollection.firstOnRefresh();
    }

    @Override
    public void initData() {
        adapter = new MyCollectionAdapter(this, list, R.layout.item_collection);
        alvCollection.setAdapter(adapter);
    }

    private void getCollection(String sessionid, int cpage, boolean language) {
        OkHttpUtils.post().url(MethodHelper.MY_COLLECTION)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))//中文：cn，蒙古文：mn
                .addParams("cpage", String.valueOf(cpage))//当前页码（非必传，默认1）
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
                alvCollection.onRefreshFailue();
                alvCollection.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(Result response, int id) {
                alvCollection.onRefreshComplete();
                alvCollection.onLoadComplete();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    List<MyCollection> dataBean = new Gson().fromJson(data, new TypeToken<List<MyCollection>>() {
                    }.getType());
                    if (page == 1) {
                        list.clear();
                    }
                    alvCollection.setResultPage(1, dataBean.size());
                    list.addAll(dataBean);
                    if (list.isEmpty()) {
                        alvCollection.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        alvCollection.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        getCollection(BaseApplication.userInfo.getSessionid(), page, sharedPreferencesHelper.isSwitchLanguage());
    }

    @Override
    public void onLoad() {
        page++;
        getCollection(BaseApplication.userInfo.getSessionid(), page, sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnItemClick({R.id.alv_collection})
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID, BaseApplication.userInfo.getArea());
        bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_ID, list.get(position - 1).getIid() + "");
        ActivityUtils.overlay(this, InformationDetailsActivity.class, bundle);
    }
}
