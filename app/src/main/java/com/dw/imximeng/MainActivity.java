package com.dw.imximeng;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dw.imximeng.adapters.ViewPagerAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserInfo;
import com.dw.imximeng.bean.UserSiteInfo;
import com.dw.imximeng.fragments.main.AdvertisementFragment;
import com.dw.imximeng.fragments.main.HomeFragment;
import com.dw.imximeng.fragments.main.MyselfFragment;
import com.dw.imximeng.fragments.main.RegionFragment;
import com.dw.imximeng.helper.LanguageHelper;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.ViewPagerSlide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnTouch;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.viewpager)
    ViewPagerSlide viewpager;
    @BindView(R.id.radio)
    RadioGroup radio;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        radio.setOnCheckedChangeListener(this);
        viewpager.addOnPageChangeListener(this);
        setupViewPager(viewpager);
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserSiteInfo(sharedPreferencesHelper.isSwitchLanguage());
        if (BaseApplication.userInfo.getSessionid() != null) {
            getUserInfo(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((RadioButton) radio.getChildAt(position)).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new RegionFragment());
        adapter.addFragment(new AdvertisementFragment());
        adapter.addFragment(new MyselfFragment());
        viewPager.setAdapter(adapter);
    }

    //禁止viewpager滑动
    @OnTouch(R.id.viewpager)
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        switch (messageEvent.getMsgCode()) {
            case LANGUAGE:
                switchLanguage(messageEvent.isMessage());
                break;
            case SIGN_IN:
                if (BaseApplication.userInfo.getSessionid() != null) {
                    getUserInfo(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
                }
                break;
            case SWITCH_PAGE:
                viewpager.setCurrentItem(1);
                break;
        }

    }

    @Override
    protected void switchLanguage(boolean language) {
        LanguageHelper languageHelper = new LanguageHelper(this, language);
        for (int i = 0; i < radio.getChildCount(); i++) {
            RadioButton item = (RadioButton) radio.getChildAt(i);
            switch (item.getId()) {
                case R.id.rb_home:
                    item.setText(languageHelper.getHome());
                    break;
                case R.id.rb_region:
                    item.setText(languageHelper.getRegion());
                    break;
                case R.id.rb_advertisement:
                    item.setText(languageHelper.getAdvertisement());
                    break;
                case R.id.rb_myself:
                    item.setText(languageHelper.getMyself());
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                viewpager.setCurrentItem(0);
                break;
            case R.id.rb_region:
                viewpager.setCurrentItem(1);
                break;
            case R.id.rb_advertisement:
                viewpager.setCurrentItem(2);
                break;
            case R.id.rb_myself:
                viewpager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getUserInfo(String sessionid, boolean language) {
        OkHttpUtils.post().url(MethodHelper.GET_USER_CENTER)
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
            }

            @Override
            public void onResponse(Result response, int id) {

                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    BaseApplication.userInfo = new Gson().fromJson(data, UserInfo.class);
                }
            }
        });
    }

    private void getUserSiteInfo(boolean language) {
        OkHttpUtils.post().url(MethodHelper.GET_USER_SITE_INFO)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .build().execute(new Callback<UserSiteInfo>() {
            @Override
            public UserSiteInfo parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, UserSiteInfo.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(UserSiteInfo response, int id) {
                if (response.getStatus() == 1) {
                    BaseApplication.userSiteInfo = response;
                }
            }
        });
    }
}
