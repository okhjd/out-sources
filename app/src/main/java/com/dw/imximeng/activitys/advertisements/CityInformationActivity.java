package com.dw.imximeng.activitys.advertisements;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.GridView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.GvCateListAdapter;
import com.dw.imximeng.adapters.VpCateListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.CateList;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
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
 * @Created_Time 2018\7\4 0004
 */
public class CityInformationActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private VpCateListAdapter mAdapter;
    private List<CateList.CateItem> list = new ArrayList<>();
    private List<GridView> gridList = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_city_info;
    }

    @Override
    public void initView() {
        setTitle("城市");
        mAdapter = new VpCateListAdapter();
        viewpager.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        getCateList(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
    }

    private void getCateList(String sessionid, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.INFORMATION_CATE_LIST)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))//中文：cn，蒙古文：mn
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
                    CateList cateList = new Gson().fromJson(data, CateList.class);
                    if (gridList.size() > 0) {
                        gridList.clear();
                    }
                    list.clear();
                    list.addAll(cateList.getCateList());
                    list.addAll(cateList.getCateList());

                    //计算viewpager一共显示几页
                    int pageSize = list.size() % 8 == 0
                            ? list.size() / 8
                            : list.size() / 8 + 1;
                    for (int i = 0; i < pageSize; i++) {
                        GridView gridView = new GridView(CityInformationActivity.this);
                        gridView.setVerticalSpacing(MaDensityUtils.dp2px(getApplicationContext(),24));
                        GvCateListAdapter adapter = new GvCateListAdapter(list, i);
                        gridView.setNumColumns(4);
                        gridView.setAdapter(adapter);
                        gridList.add(gridView);
                    }

                    mAdapter.add(gridList);
                }
            }
        });
    }
}
