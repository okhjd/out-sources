package com.dw.imximeng.fragments.main;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.advertisements.CityInformationActivity;
import com.dw.imximeng.activitys.advertisements.SmallToolsActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.base.BaseFragment;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.RegionList;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class AdvertisementFragment extends BaseFragment {
    @BindView(R.id.tv_current_area_1)
    TextView tvCurrentArea1;
    @BindView(R.id.tv_city_service)
    TextView tvCityService;
    @BindView(R.id.tv_small_tools)
    TextView tvSmallTools;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_advertisement;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getArea(BaseApplication.userInfo.getSessionid(),
                BaseApplication.userInfo.getArea(),
                sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnClick({R.id.tv_city_service, R.id.tv_small_tools, R.id.tv_current_area_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_city_service:
                ActivityUtils.overlay(getActivity(), CityInformationActivity.class, tvCurrentArea1.getText().toString());
                break;
            case R.id.tv_small_tools:
                ActivityUtils.overlay(getActivity(), SmallToolsActivity.class);
                break;
            case R.id.tv_current_area_1:
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setMsgCode(MessageEvent.MessageType.SWITCH_PAGE);
                EventBus.getDefault().post(messageEvent);
                break;
        }
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
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(final MessageEvent event) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MessageEvent.MessageType.AREA == event.getMsgCode()) {
                    getArea(BaseApplication.userInfo.getSessionid(),
                            BaseApplication.userInfo.getArea(),
                            sharedPreferencesHelper.isSwitchLanguage());
                }
                if (MessageEvent.MessageType.REFRESH_MAIN == event.getMsgCode()) {
                    tvCurrentArea1.setText("您未选择地区");
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
