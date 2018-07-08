package com.dw.imximeng.activitys.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.InformationAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Information;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AutoListView;
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
 * @Created_Time 2018\7\8 0008
 */
public class SearchResultActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.lv_search_result)
    AutoListView lvSearchResult;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.ll_load_error)
    LinearLayout llLoadError;

    private int page = 1;
    private InformationAdapter adapter;
    private List<Information.ListBean> listBeans = new ArrayList<>();
    private String city = "";
    private String keywords = "";
    private String cateId = "";

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        city = bundle.getString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID);
        cateId = bundle.getString(ActivityExtras.EXTRAS_CATE_ID);
        keywords = bundle.getString(ActivityExtras.EXTRAS_KEY_WORD);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView() {
        etSearch.setText(keywords);
        etSearch.setFocusable(false);
        adapter = new InformationAdapter(this, listBeans, R.layout.item_collection);
        lvSearchResult.setAdapter(adapter);
        lvSearchResult.setOnRefreshListener(this);
        lvSearchResult.setOnLoadListener(this);
        lvSearchResult.firstOnRefresh();
    }

    @Override
    public void initData() {

    }

    private void searchInfoList(String area, String sessionid, String cid, String keywords, String orderby, int cpage, boolean language) {
        OkHttpUtils.post().url(MethodHelper.INFORMATION_LIST)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("area", area)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))//非必传
                .addParams("cid", cid)//非必传，默认所有
                .addParams("keywords", keywords)//非必传
                .addParams("orderby", orderby)//非必传，最新发布：zxfb，收藏最多：sczd，默认zxfb
                .addParams("cpage", String.valueOf(cpage))//非必传，默认1
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                lvSearchResult.onRefreshFailue();
                lvSearchResult.setVisibility(View.GONE);
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                lvSearchResult.onRefreshComplete();
                lvSearchResult.onLoadComplete();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    Information information = new Gson().fromJson(data, Information.class);

                    if (page == 1) {
                        listBeans.clear();
                    }
                    lvSearchResult.setResultPage(1, information.getList().size());
                    listBeans.addAll(information.getList());
                    if (listBeans.isEmpty()) {
                        lvSearchResult.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        lvSearchResult.setVisibility(View.VISIBLE);
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
        searchInfoList(city, BaseApplication.userInfo.getSessionid(), cateId, keywords, "", page, sharedPreferencesHelper.isSwitchLanguage());
    }

    @Override
    public void onLoad() {
        page++;
        searchInfoList(city, BaseApplication.userInfo.getSessionid(), cateId, keywords, "", page, sharedPreferencesHelper.isSwitchLanguage());
    }
}
