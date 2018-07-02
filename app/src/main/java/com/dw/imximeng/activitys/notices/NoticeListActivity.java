package com.dw.imximeng.activitys.notices;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.GlideImageLoader;
import com.dw.imximeng.adapters.NoticeListAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserIndexData;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
 * @Created_Time 2018\6\28 0028
 */
public class NoticeListActivity extends BaseActivity {
    @BindView(R.id.lv_notice_list)
    ListView lvNoticeList;

    private NoticeListAdapter adapter;
    private List<UserIndexData.NoticeListBean> mList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_list;
    }

    @Override
    public void initView() {
        setTitle("公告列表");
        adapter = new NoticeListAdapter(this, mList, R.layout.item_notice);
        lvNoticeList.setAdapter(adapter);
    }

    @Override
    public void initData() {
        userNoticeList(sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnItemClick(R.id.lv_notice_list)
    public void onItemClick(int position){

    }

    private void userNoticeList(boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.USER_NOTICE_LIST)
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
                    List<UserIndexData.NoticeListBean> userNotice = new Gson().fromJson(data, new TypeToken<List<UserIndexData.NoticeListBean>>(){}.getType());
                    mList.clear();
                    mList.addAll(userNotice);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
