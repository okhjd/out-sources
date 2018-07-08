package com.dw.imximeng.activitys.myself;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.advertisements.ReleaseInfoActivity;
import com.dw.imximeng.adapters.MyReleaseAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MyRelease;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AlertDialog;
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
 * @Created_Time 2018\7\8 0008
 */
public class MyReleaseActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener, MyReleaseAdapter.OnDeteleInfoListener {

    @BindView(R.id.lv_my_release)
    AutoListView lvMyRelease;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    private int page = 1;
    private List<MyRelease> list = new ArrayList<>();
    private MyReleaseAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_release;
    }

    @Override
    public void initView() {
        setTitle("我的发布");

        adapter = new MyReleaseAdapter(this, list, R.layout.item_my_release);
        lvMyRelease.setAdapter(adapter);
        lvMyRelease.setOnRefreshListener(this);
        lvMyRelease.setOnLoadListener(this);
        lvMyRelease.firstOnRefresh();
        adapter.setOnDeteleInfoListener(this);
    }

    @Override
    public void initData() {

    }

    private void getMyRelease(String sessionid, int cpage) {
        OkHttpUtils.post().url(MethodHelper.MY_RELEASE)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("cpage", String.valueOf(cpage))
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                lvMyRelease.onRefreshFailue();
                llEmpty.setVisibility(View.VISIBLE);
                lvMyRelease.setVisibility(View.GONE);
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                lvMyRelease.onRefreshComplete();
                lvMyRelease.onLoadComplete();
                if (response.getStatus() == 1) {
                    List<MyRelease> temp = new Gson().fromJson(new Gson().toJson(response.getData()),
                            new TypeToken<List<MyRelease>>() {
                            }.getType());
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(temp);
                    lvMyRelease.setResultPage(page, temp.size());
                    adapter.notifyDataSetChanged();
                    if (list.isEmpty()) {
                        llEmpty.setVisibility(View.VISIBLE);
                        lvMyRelease.setVisibility(View.GONE);
                    } else {
                        llEmpty.setVisibility(View.GONE);
                        lvMyRelease.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        getMyRelease(BaseApplication.userInfo.getSessionid(), page);
    }

    @Override
    public void onLoad() {
        page++;
        getMyRelease(BaseApplication.userInfo.getSessionid(), page);
    }

    @OnClick(R.id.tv_add_release)
    public void onClick() {
        ActivityUtils.overlay(this, ReleaseInfoActivity.class, BaseApplication.userInfo.getArea());
    }

    @Override
    public void onDeteleInfo(MyRelease item) {
        showDialog(item);
    }

    private void showDialog(final MyRelease item) {
        new AlertDialog(this)
                .builder()
                .setMsg("是否删除该信息")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteInfo(BaseApplication.userInfo.getSessionid(), item.getId());
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void deleteInfo(String sessionid, int id) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.DELETE_RELEASE)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("id", String.valueOf(id))
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
                showToast(response.getMessage());
                if (response.getStatus() == 1) {
                    lvMyRelease.firstOnRefresh();
                }
            }
        });
    }
}
