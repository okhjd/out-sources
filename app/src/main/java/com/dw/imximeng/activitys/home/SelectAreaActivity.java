package com.dw.imximeng.activitys.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.advertisements.CityInformationActivity;
import com.dw.imximeng.activitys.advertisements.ServiceSearchActivity;
import com.dw.imximeng.adapters.RegionListAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.RegionList;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
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
 * @Created_Time 2018\7\8 0008
 */
public class SelectAreaActivity extends BaseActivity {
    @BindView(R.id.lv_region)
    ListView lvRegion;

    private RegionListAdapter adapter;
    private List<RegionList.DataBean> list = new ArrayList<>();
    private String keywords = "";

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        keywords = ActivityUtils.getStringExtra(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_area;
    }

    @Override
    public void initView() {
        setTitle("请选择地区");
        adapter = new RegionListAdapter(this, list, R.layout.item_region_list);
        lvRegion.setAdapter(adapter);

    }

    @Override
    public void initData() {
        getRegionList(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
    }

    private void getRegionList(final String sessionid, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.GET_REGION_LIST)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .build().execute(new Callback<RegionList>() {
            @Override
            public RegionList parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, RegionList.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                closeProgressBar();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(RegionList response, int id) {
                closeProgressBar();
                if (response.getStatus() == 1) {
                    list.clear();
                    list.addAll(response.getData());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnItemClick(R.id.lv_region)
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID, list.get(position).getId());
        bundle.putString(ActivityExtras.EXTRAS_CATE_ID, "");
        bundle.putString(ActivityExtras.EXTRAS_KEY_WORD, keywords);
        ActivityUtils.forward(this, SearchResultActivity.class, bundle);
    }
}
