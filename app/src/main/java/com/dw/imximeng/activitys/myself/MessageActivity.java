package com.dw.imximeng.activitys.myself;

import android.os.Bundle;
import android.util.Log;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.WebActivity;
import com.dw.imximeng.adapters.MessageListAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserMessage;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AutoListView;
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
 * @Created_Time 2018\6\30 0030
 */
public class MessageActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {
    @BindView(R.id.lv_message)
    AutoListView lvMessage;

    private int page = 1;
    private MessageListAdapter adapter;
    private List<UserMessage> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        setTitle("系统消息");

        adapter = new MessageListAdapter(this, list, R.layout.item_message);
        lvMessage.setAdapter(adapter);
        lvMessage.setOnRefreshListener(this);
        lvMessage.setOnLoadListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        lvMessage.firstOnRefresh();
    }

    private void getMessageList(String sessionid, int cpage, boolean language) {
        OkHttpUtils.post().url(MethodHelper.MESSAGE_LIST)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))//中文：cn，蒙古文：mn
                .addParams("cpage", String.valueOf(cpage))//中文：cn，蒙古文：mn
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                lvMessage.onRefreshFailue();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                lvMessage.onRefreshComplete();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    List<UserMessage> messagesList = new Gson().fromJson(data, new TypeToken<List<UserMessage>>() {
                    }.getType());
                    if (page == 1) {
                        list.clear();
                    } else {
                        lvMessage.onLoadComplete();
                    }
                    lvMessage.setResultPage(page, messagesList.size());
                    list.addAll(messagesList);
                    adapter.notifyDataSetChanged();
                } else {
                    lvMessage.onRefreshFailue();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        getMessageList(BaseApplication.userInfo.getSessionid(), page, sharedPreferencesHelper.isSwitchLanguage());
    }

    @Override
    public void onLoad() {
        page++;
        getMessageList(BaseApplication.userInfo.getSessionid(), page, sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnItemClick({R.id.lv_message})
    public void onItemClick(int position) {
        if (position == 0) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, list.get(position - 1).getWeburl());
        bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, list.get(position - 1).getTitle());
        ActivityUtils.overlay(this, WebActivity.class, bundle);
    }
}
