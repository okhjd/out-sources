package com.dw.imximeng.activitys.advertisements;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.signIn.SignInActivity;
import com.dw.imximeng.adapters.GvCateListAdapter;
import com.dw.imximeng.adapters.InformationAdapter;
import com.dw.imximeng.adapters.VpCateListAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.CateList;
import com.dw.imximeng.bean.Information;
import com.dw.imximeng.bean.RegionList;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AutoListView;
import com.dw.imximeng.widgets.ImageViewRoundOval;
import com.dw.imximeng.widgets.SigninAlertDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
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
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_sign_in)
    ImageView ivSignIn;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.ll_load_error)
    LinearLayout llLoadError;
    private VpCateListAdapter mAdapter;
    private List<CateList.CateItem> list = new ArrayList<>();
    private List<GridView> gridList = new ArrayList<>();

    private String city;
    private InformationAdapter adapter;
    private List<Information.ListBean> listBeans = new ArrayList<>();
    private int page;
    private String cateId = "";

    private long mClickTime = 0;
    private String orderby = "zxfb";

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
        mAdapter = new VpCateListAdapter();
        viewpager.setAdapter(mAdapter);

        ivHead.setType(ImageViewRoundOval.TYPE_ROUND);
        ivHead.setRoundRadius(MaDensityUtils.dp2px(this, 5));//圆角大小
        if (BaseApplication.userInfo.getShowHportrait() != null) {
            ImageLoader.getInstance().displayImage(BaseApplication.userInfo.getShowHportrait(), ivHead);
        }
        if (BaseApplication.userInfo.isIssign()) {
            ivSignIn.setVisibility(View.GONE);
        } else {
            ivSignIn.setVisibility(View.VISIBLE);
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
        getArea(BaseApplication.userInfo.getSessionid(), city, sharedPreferencesHelper.isSwitchLanguage());
    }

    private void getCateList(String sessionid, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.INFORMATION_CATE_LIST)
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
                    if (!list.isEmpty()) {
                        list.get(0).setCheck(true);
                    }

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
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setCheck(false);
        }
        list.get(position).setCheck(true);
        CateList.CateItem item = (CateList.CateItem) parent.getAdapter().getItem(position);
        cateId = item.getId() + "";
        lvInfo.firstOnRefresh();
        ((GvCateListAdapter) parent.getAdapter()).notifyDataSetChanged();
    }

    @OnItemClick(R.id.lv_info)
    public void onListViewItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID, city);
        bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_ID, listBeans.get(position - 1).getId());
        ActivityUtils.overlay(this, InformationDetailsActivity.class, bundle);
    }

    private void getInfoList(final String area, String sessionid, String cid, String keywords,
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
                lvInfo.setVisibility(View.GONE);
                llLoadError.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(Result response, int id) {
                lvInfo.onRefreshComplete();
                lvInfo.onLoadComplete();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    Information information = new Gson().fromJson(data, Information.class);

                    if (page == 1) {
                        listBeans.clear();
                    }
                    lvInfo.setResultPage(1, information.getList().size());
                    listBeans.addAll(information.getList());
                    if (listBeans.isEmpty()) {
                        lvInfo.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lvInfo.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                    }
                    if (information.getCateList().isEmpty()) {
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.setComment(information.getCateList().get(0).getIscomment().equals("1"));
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        getInfoList(city, BaseApplication.userInfo.getSessionid(), cateId, "", orderby,
                String.valueOf(page), sharedPreferencesHelper.isSwitchLanguage());
    }

    @Override
    public void onLoad() {
        page++;
        getInfoList(city, BaseApplication.userInfo.getSessionid(), cateId, "", "",
                String.valueOf(page), sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnClick({R.id.tv_title, R.id.iv_add, R.id.iv_sign_in, R.id.ll_load_error, R.id.iv_push,
            R.id.tv_new_release, R.id.tv_collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                ActivityUtils.overlay(this, ReleaseInfoActivity.class, BaseApplication.userInfo.getArea());
                break;
            case R.id.iv_sign_in:
                if (BaseApplication.userInfo.getSessionid() != null) {
                    userSignIn(BaseApplication.userInfo.getSessionid());
                } else {
                    ActivityUtils.overlay(this, SignInActivity.class);
                }
                break;
            case R.id.tv_title:
                if (System.currentTimeMillis() - mClickTime < 800) {
                    //此处做双击具体业务逻辑
                    getCateList(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
                    lvInfo.firstOnRefresh();
                } else {
                    mClickTime = System.currentTimeMillis();
                    //表示单击，此处也可以做单击的操作
                }
                break;
            case R.id.ll_load_error:
                lvInfo.firstOnRefresh();
                llLoadError.setVisibility(View.GONE);
                lvInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_push:
                ActivityUtils.overlay(this, ReleaseInfoActivity.class, BaseApplication.userInfo.getArea());
                break;
            case R.id.tv_new_release:
                page = 1;
                orderby = "zxfb";
                lvInfo.firstOnRefresh();
                break;
            case R.id.tv_collect:
                page = 1;
                orderby = "sczd";
                lvInfo.firstOnRefresh();
                break;
        }
    }

    private void userSignIn(String sessionid) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.USER_SIGN_IN)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))//非必传
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
                showToast(response.getMessage());
                if (response.getStatus() == 1) {
                    showSignInDialog(3);
                }
            }
        });
    }

    private void showSignInDialog(int num) {
        new SigninAlertDialog(this)
                .builder()
                .setTitle("签到成功")
                .setNum("+" + String.valueOf(num))
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseApplication.userInfo.setIssign(true);
                        ivSignIn.setVisibility(View.GONE);
                    }
                }).show();
    }

    private void getArea(String sessionid, String area, boolean language) {
        OkHttpUtils.post().url(MethodHelper.USER_AREA_INFO)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("area", StringUtils.stringsIsEmpty(area))//地区ID
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    RegionList.DataBean dataBean = new Gson().fromJson(data, RegionList.DataBean.class);
                    setTitle(dataBean.getName());
                }
            }
        });
    }
}
