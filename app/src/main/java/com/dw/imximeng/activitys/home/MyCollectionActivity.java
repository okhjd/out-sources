package com.dw.imximeng.activitys.home;

import android.util.Log;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.SmallTools;
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
public class MyCollectionActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener{
    @BindView(R.id.alv_collection)
    AutoListView alvCollection;

    private int page = 1;
    private List<String> list = new ArrayList<>();
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

    }

    private void getCollection(String sessionid, int page, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.MY_COLLECTION)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))//中文：cn，蒙古文：mn
                .addParams("cpage", String.valueOf(page))//当前页码（非必传，默认1）
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
                    List<SmallTools> smallTools = new Gson().fromJson(data, new TypeToken<List<SmallTools>>() {
                    }.getType());

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
        if (position == 0) {
            return;
        }
    }
}
