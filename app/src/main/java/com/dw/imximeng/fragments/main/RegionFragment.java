package com.dw.imximeng.fragments.main;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.RegionListAdapter;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.base.BaseFragment;
import com.dw.imximeng.bean.RegionList;
import com.dw.imximeng.bean.UserInfo;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class RegionFragment extends BaseFragment {

    @BindView(R.id.tv_current_area_1)
    TextView tvCurrentArea1;
    @BindView(R.id.tv_current_area_2)
    TextView tvCurrentArea2;
    @BindView(R.id.lv_region)
    ListView lvRegion;

    private RegionListAdapter adapter;
    private List<RegionList.DataBean> list = new ArrayList<>();

    private ScaleAnimation sato0 = new ScaleAnimation(1, 1, 1, 0,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    private ScaleAnimation sato1 = new ScaleAnimation(1, 1, 0, 1,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    @Override
    public int getLayoutId() {
        return R.layout.fragment_region;
    }

    @Override
    public void initView(View view) {
        showImage1();
        sato0.setDuration(500);
        sato1.setDuration(500);

        sato0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (tvCurrentArea1.getVisibility() == View.VISIBLE) {
                    tvCurrentArea1.setAnimation(null);
                    showImage2();
                    tvCurrentArea2.startAnimation(sato1);
                } else {
                    tvCurrentArea2.setAnimation(null);
                    showImage1();
                    tvCurrentArea1.startAnimation(sato1);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showImage1() {

        tvCurrentArea1.setVisibility(View.VISIBLE);
        tvCurrentArea2.setVisibility(View.GONE);
    }

    private void showImage2() {

        tvCurrentArea1.setVisibility(View.GONE);
        tvCurrentArea2.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        adapter = new RegionListAdapter(getActivity(), list, R.layout.item_region_list);
        lvRegion.setAdapter(adapter);
        getRegionList(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnClick({R.id.fl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl:
                if (tvCurrentArea1.getVisibility() == View.VISIBLE) {
                    tvCurrentArea1.startAnimation(sato0);
                } else {
                    tvCurrentArea2.startAnimation(sato0);
                }
                break;
        }
    }

    private void getRegionList(final String sessionid, boolean language) {
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
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(RegionList response, int id) {
                if (response.getStatus() == 1) {
                    list.clear();
                    list.addAll(response.getData());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnItemLongClick(R.id.lv_region)
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
        //长按弹出提示框，点击确定设置默认地区
        return false;
    }
}
