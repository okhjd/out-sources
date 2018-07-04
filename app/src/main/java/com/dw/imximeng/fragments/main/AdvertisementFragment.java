package com.dw.imximeng.fragments.main;

import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.advertisements.CityInformationActivity;
import com.dw.imximeng.activitys.advertisements.SmallToolsActivity;
import com.dw.imximeng.base.BaseFragment;
import com.dw.imximeng.helper.ActivityUtils;

import butterknife.BindView;
import butterknife.OnClick;

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

    }

    @OnClick({R.id.tv_city_service, R.id.tv_small_tools})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_city_service:
                ActivityUtils.overlay(getActivity(), CityInformationActivity.class);
                break;
            case R.id.tv_small_tools:
                ActivityUtils.overlay(getActivity(), SmallToolsActivity.class);
                break;
        }
    }
}
