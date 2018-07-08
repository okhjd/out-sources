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
public class CityServiceActivity extends BaseActivity {
    @BindView(R.id.horizontalscrollview)
    HorizontalScrollView horizontalscrollview;

    RadioGroup mRadioGroup;
    @BindView(R.id.lv_city_service)
    ListView lvCityService;
    @BindView(R.id.ll_load_error)
    LinearLayout llLoadError;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    private int width;
    private String city = "";
    private String cateId = "";
    private CityServiceAdapter adapter;
    private List<CityService.ListBean> list = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        city = ActivityUtils.getStringExtra(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_city_service;
    }

    @Override
    public void initView() {
        setTitle("同城服务");
        setSearchVisible();

        adapter = new CityServiceAdapter(this, list, R.layout.item_city_service);
        lvCityService.setAdapter(adapter);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        //获取mRadioGroup horizontalscrollview布局
        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.product_view, null);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group);

    }

    @Override
    public void initData() {
        getCateList(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
    }

    @SuppressLint("ResourceType")
    private void setHorizontalscrollview(final List<CateList.CateItem> list) {
        for (int i = 0; i < list.size(); i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(list.get(i).getName());
            rb.setTextSize(13);
            rb.setGravity(Gravity.CENTER);
            rb.setButtonDrawable(null);
            //根据需要设置显示初始标签的个数，这里显示7个
            rb.setLayoutParams(new ViewGroup.LayoutParams((int) (width / 7), MaDensityUtils.dp2px(this, 40)));
            rb.setCompoundDrawables(null, null, null, getResources().getDrawable(R.drawable.selector_radiobutton_bg));
            rb.setTextColor(getResources().getColorStateList(R.drawable.selector_radio_button_typeface));
            mRadioGroup.addView(rb);
        }
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
        cateId = list.get(0).getId() + "";
        getServiceList(BaseApplication.userInfo.getSessionid(), city, cateId, "", sharedPreferencesHelper.isSwitchLanguage());
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int RadiobuttonId = group.getCheckedRadioButtonId();
                //获取radiobutton对象
                RadioButton bt = (RadioButton) group.findViewById(RadiobuttonId);
                //获取单个对象中的位置
                int index = group.indexOfChild(bt);
                //设置滚动位置，可使点击radiobutton时，将该radiobutton移动至第二位置
                horizontalscrollview.smoothScrollTo(bt.getLeft() - (int) (width / 7), 0);
//                item_check_ID = index;
//                webUrlRefresh(ParentItemArr, index);
                cateId = list.get(index).getId() + "";
                getServiceList(BaseApplication.userInfo.getSessionid(), city, cateId, "", sharedPreferencesHelper.isSwitchLanguage());
            }
        });
        horizontalscrollview.addView(mRadioGroup);
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
                    setHorizontalscrollview(cateList.getCateList());
                }
            }
        });
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

    @Override
    public void search(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID, city);
        bundle.putString(ActivityExtras.EXTRAS_CATE_ID, cateId);
        ActivityUtils.overlay(this, ServiceSearchActivity.class, bundle);
    }
}
