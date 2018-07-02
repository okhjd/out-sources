package com.dw.imximeng.activitys.myself;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.MyPointsListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MyPoints;
import com.dw.imximeng.bean.Result;
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
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\1 0001
 */
public class MyPointsActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {
    @BindView(R.id.tv_point_num)
    TextView tvPointNum;
    @BindView(R.id.lv_point_record)
    AutoListView lvPointRecord;

    private int page = 1;
    private MyPointsListAdapter adapter;
    private List<MyPoints.PointsItem> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_points;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#696f77"), true);
        setTitle("我的积分");
        adapter = new MyPointsListAdapter(this, list, R.layout.item_my_points);
        lvPointRecord.setAdapter(adapter);
        lvPointRecord.setOnRefreshListener(this);
        lvPointRecord.setOnLoadListener(this);
        lvPointRecord.firstOnRefresh();
    }

    @Override
    public void initData() {

    }

    private void getPoints(String sessionid, int cpage, boolean language) {
        OkHttpUtils.post().url(MethodHelper.GET_POINTS_RECORD)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
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
                lvPointRecord.onRefreshFailue();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                lvPointRecord.onRefreshComplete();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    MyPoints resultList = new Gson().fromJson(data, MyPoints.class);
                    tvPointNum.setText(resultList.getIntegral());
                    if (page == 1) {
                        list.clear();
                    } else {
                        lvPointRecord.onLoadComplete();
                    }

                    lvPointRecord.setResultPage(page, resultList.getList().size());
                    list.addAll(resultList.getList());
                    adapter.notifyDataSetChanged();
                } else {
                    lvPointRecord.onRefreshFailue();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        getPoints(BaseApplication.userInfo.getSessionid(), page, sharedPreferencesHelper.isSwitchLanguage());
    }

    @Override
    public void onLoad() {
        page++;
        getPoints(BaseApplication.userInfo.getSessionid(), page, sharedPreferencesHelper.isSwitchLanguage());
    }
}
