package com.dw.imximeng.activitys.advertisements;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.GvCateListAdapter;
import com.dw.imximeng.adapters.InformationAdapter;
import com.dw.imximeng.adapters.VpCateListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.CateList;
import com.dw.imximeng.bean.Information;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AutoListView;
import com.dw.imximeng.widgets.ImageViewRoundOval;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
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
public class CityInformationActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        AutoListView.OnRefreshListener, AutoListView.OnLoadListener {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_head)
    ImageViewRoundOval ivHead;
    @BindView(R.id.lv_info)
    AutoListView lvInfo;
    private VpCateListAdapter mAdapter;
    private List<CateList.CateItem> list = new ArrayList<>();
    private List<GridView> gridList = new ArrayList<>();

    private String city;
    private InformationAdapter adapter;
    private List<Information.ListBean> listBeans = new ArrayList<>();
    private int page;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        city = ActivityUtils.getStringExtra(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_city_info;
    }

    @Override
    public void initView() {
        setTitle(city);
        mAdapter = new VpCateListAdapter();
        viewpager.setAdapter(mAdapter);

        ivHead.setType(ImageViewRoundOval.TYPE_ROUND);
        ivHead.setRoundRadius(MaDensityUtils.dp2px(this, 5));//圆角大小
        if (BaseApplication.userInfo.getShowHportrait() != null) {
            ImageLoader.getInstance().displayImage(BaseApplication.userInfo.getShowHportrait(), ivHead);
        }

        adapter = new InformationAdapter(this, listBeans, R.layout.item_collection);
        lvInfo.setAdapter(adapter);
        lvInfo.setOnRefreshListener(this);
        lvInfo.setOnLoadListener(this);
        lvInfo.firstOnRefresh();
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

                    //计算viewpager一共显示几页
                    int pageSize = list.size() % 8 == 0
                            ? list.size() / 8
                            : list.size() / 8 + 1;
                    for (int i = 0; i < pageSize; i++) {
                        GridView gridView = new GridView(CityInformationActivity.this);
                        gridView.setVerticalSpacing(MaDensityUtils.dp2px(getApplicationContext(), 24));
                        GvCateListAdapter adapter = new GvCateListAdapter(list, i);
                        gridView.setNumColumns(4);
                        gridView.setAdapter(adapter);
                        gridView.setOnItemClickListener(CityInformationActivity.this);
                        gridList.add(gridView);
                    }

                    mAdapter.add(gridList);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CateList.CateItem item = (CateList.CateItem) parent.getAdapter().getItem(position);
        showToast(item.getName());
    }

    private void getInfoList(String area, String sessionid, String cid, String keywords,
                             String orderby, String cpage, boolean language) {
        OkHttpUtils.post().url(MethodHelper.INFORMATION_LIST)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("area", area)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))//非必传
                .addParams("cid", cid)//非必传，默认所有
                .addParams("keywords", keywords)//非必传
                .addParams("orderby", orderby)//非必传，最新发布：zxfb，收藏最多：sczd，默认zxfb
                .addParams("cpage", cpage)//非必传，默认1
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
                lvInfo.onRefreshFailue();
            }

            @Override
            public void onResponse(Result response, int id) {
                lvInfo.onRefreshComplete();
                lvInfo.onLoadComplete();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    Information information = new Gson().fromJson(data, Information.class);

                    if (page==1){
                        listBeans.clear();
                    }
                    lvInfo.setResultPage(1, information.getList().size());
                    listBeans.addAll(information.getList());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page=1;
        getInfoList(BaseApplication.userInfo.getArea(),
                BaseApplication.userInfo.getSessionid(), "", "", "",
                String.valueOf(page), sharedPreferencesHelper.isSwitchLanguage());
    }

    @Override
    public void onLoad() {
        page++;
        getInfoList(BaseApplication.userInfo.getArea(),
                BaseApplication.userInfo.getSessionid(), "", "", "",
                String.valueOf(page), sharedPreferencesHelper.isSwitchLanguage());
    }
}
