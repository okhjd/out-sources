package com.dw.imximeng.activitys.advertisements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.CityServiceAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.CateList;
import com.dw.imximeng.bean.CityService;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
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
 * @Created_Time 2018\7\7 0007
 */
public class CityServiceSearchResultActivity extends BaseActivity {

    @BindView(R.id.lv_city_service)
    ListView lvCityService;
    @BindView(R.id.ll_load_error)
    LinearLayout llLoadError;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    private String city = "";
    private String cateId = "";
    private String keywords = "";
    private CityServiceAdapter adapter;
    private List<CityService.ListBean> list = new ArrayList<>();

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
        return R.layout.activity_city_service_result;
    }

    @Override
    public void initView() {
        setTitle("搜索");

        adapter = new CityServiceAdapter(this, list, R.layout.item_city_service);
        lvCityService.setAdapter(adapter);

    }

    @Override
    public void initData() {
        getServiceList(BaseApplication.userInfo.getSessionid(), city, cateId, keywords, sharedPreferencesHelper.isSwitchLanguage());
    }

    private void getServiceList(String sessionid, final String area, String cid, String keywords, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.SERVICE_LIST)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
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
                llLoadError.setVisibility(View.VISIBLE);
                llEmpty.setVisibility(View.GONE);
                lvCityService.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(Result response, int id) {
                llLoadError.setVisibility(View.GONE);
                lvCityService.setVisibility(View.VISIBLE);
                closeProgressBar();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    CityService dataBean = new Gson().fromJson(data, CityService.class);
                    list.clear();
                    list.addAll(dataBean.getList());
                    adapter.notifyDataSetChanged();

                    if (list.isEmpty()) {
                        llEmpty.setVisibility(View.VISIBLE);
                        lvCityService.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
