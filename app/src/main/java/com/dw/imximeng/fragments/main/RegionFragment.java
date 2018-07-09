package com.dw.imximeng.fragments.main;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.dw.imximeng.R;
import com.dw.imximeng.activitys.advertisements.CityInformationActivity;
import com.dw.imximeng.activitys.home.SearchActivity;
import com.dw.imximeng.activitys.signIn.SignInActivity;
import com.dw.imximeng.adapters.RegionListAdapter;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.base.BaseFragment;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.RegionList;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.LocationService;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AlertDialog;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
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
    @BindView(R.id.tv_address)
    TextView tvAddress;

    private RegionListAdapter adapter;
    private List<RegionList.DataBean> list = new ArrayList<>();

    private ScaleAnimation sato0 = new ScaleAnimation(1, 1, 1, 0,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    private ScaleAnimation sato1 = new ScaleAnimation(1, 1, 0, 1,
            Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);

    private LocationService locationService;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initData() {
        adapter = new RegionListAdapter(getActivity(), list, R.layout.item_region_list);
        lvRegion.setAdapter(adapter);
        getRegionList(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());

        // -----------location config ------------
        locationService = new LocationService(getApplication());
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        getArea(BaseApplication.userInfo.getSessionid(),
                BaseApplication.userInfo.getArea(),
                sharedPreferencesHelper.isSwitchLanguage());
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //长按弹出提示框，点击确定设置默认地区
        if (BaseApplication.userInfo.getSessionid() != null) {
            showDialog(list.get(position));
        } else {
            ActivityUtils.overlay(getActivity(), SignInActivity.class);
        }
        return true;
    }

    @OnItemClick(R.id.lv_region)
    public void onItemClick(int position) {
        ActivityUtils.overlay(getActivity(), CityInformationActivity.class, list.get(position).getId());
    }

    private void showDialog(final RegionList.DataBean item) {
        SpannableString spannableString = new SpannableString("默认地址设置为：" + item.getName());
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#689df5"));
        spannableString.setSpan(colorSpan, 8, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        new AlertDialog(getActivity())
                .builder()
                .setTitle("温馨提示")
                .setMsg(spannableString)
                .setPositiveButton("是的", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postSetArea(BaseApplication.userInfo.getSessionid(), item.getId(), item.getName());
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void postSetArea(final String sessionid, final String areaId, final String area) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.SET_DEFAULT_AREA)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("area", areaId)//地区ID
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
                    if (tvCurrentArea1.getVisibility() == View.VISIBLE) {
                        tvCurrentArea2.setText(area);
                        tvCurrentArea1.startAnimation(sato0);
                    } else {
                        tvCurrentArea1.setText(area);
                        tvCurrentArea2.startAnimation(sato0);
                    }
                    BaseApplication.userInfo.setArea(areaId);
                    getRegionList(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());

                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setMsgCode(MessageEvent.MessageType.AREA);
                    EventBus.getDefault().post(messageEvent);
                }
            }
        });
    }

    private void getArea(String sessionid, String area, boolean language) {
        showProgressBar();
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
                closeProgressBar();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                closeProgressBar();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    RegionList.DataBean dataBean = new Gson().fromJson(data, RegionList.DataBean.class);
                    tvCurrentArea1.setText(dataBean.getName());
                    tvCurrentArea2.setText(dataBean.getName());
                }
            }
        });
    }

    @OnClick(R.id.iv_search)
    public void onClick() {
        ActivityUtils.overlay(getActivity(), SearchActivity.class);
    }

    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                locationService.stop();
                tvAddress.setText(location.getAddrStr());
            }
        }
    };

    /***
     * Stop location service
     */
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }
}
