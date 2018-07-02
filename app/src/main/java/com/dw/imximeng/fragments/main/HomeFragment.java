package com.dw.imximeng.fragments.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.WebActivity;
import com.dw.imximeng.activitys.home.MyCollectionActivity;
import com.dw.imximeng.activitys.home.SearchActivity;
import com.dw.imximeng.activitys.notices.NoticeListActivity;
import com.dw.imximeng.adapters.GlideImageLoader;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.base.BaseFragment;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserIndexData;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.SharedPreferencesHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class HomeFragment extends BaseFragment implements OnBannerClickListener {
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_notice)
    TextView tvNotice;

    private UserIndexData indexData;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View view) {
        banner.setOnBannerClickListener(this);
        userIndexData(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
    }

    @Override
    public void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @OnClick({R.id.tv_language,R.id.ll_more,R.id.iv_search ,R.id.tv_collection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_language:
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setMsgCode(MessageEvent.MessageType.LANGUAGE);
                messageEvent.setMessage(!new SharedPreferencesHelper(getContext()).isSwitchLanguage());
                EventBus.getDefault().post(messageEvent);
                break;
            case R.id.ll_more:
                ActivityUtils.overlay(getActivity(), NoticeListActivity.class);
                break;
            case R.id.iv_search:
                ActivityUtils.overlay(getActivity(), SearchActivity.class);
                break;
            case R.id.tv_collection:
                ActivityUtils.overlay(getActivity(), MyCollectionActivity.class);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    private void userIndexData(final String sessionid, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.USER_INDEX_DATA)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
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
                    UserIndexData userIndexData = new Gson().fromJson(data, UserIndexData.class);

                    tvNotice.setText(userIndexData.getNoticeInfo().getContent());

                    indexData = userIndexData;
                    //设置图片加载器
                    banner.setImageLoader(new GlideImageLoader());
                    //设置图片集合
                    banner.setImages(userIndexData.getLbList());
                    //banner设置方法全部调用完毕时最后调用
                    banner.start();
                }
            }
        });
    }

    @Override
    public void OnBannerClick(int position) {
        Bundle bundle = new Bundle();
        //none：无跳转，link：外部跳转，information：信息详情，inside：内部HTML，appskip：APP跳转
        switch (indexData.getLbList().get(position - 1).getType().toLowerCase()) {
            case "none":
                break;
            case "link":
                bundle.clear();
                bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, indexData.getLbList().get(position - 1).getUrl());
                bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, "");
                ActivityUtils.overlay(getActivity(), WebActivity.class, bundle);
                break;
            case "information":
                break;
            case "inside":
                bundle.clear();
                bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, indexData.getLbList().get(position - 1).getInsideHtml());
                bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, "");
                ActivityUtils.overlay(getActivity(), WebActivity.class, bundle);
                break;
            case "appskip":
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(indexData.getLbList().get(position - 1).getArd_schemeurl()));
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    showToast("找不到APP");
                }

                break;
        }

    }
}
