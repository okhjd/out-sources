package com.dw.imximeng.activitys.advertisements;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.WebActivity;
import com.dw.imximeng.adapters.SmallToolsAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.SmallTools;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\30 0030
 */
public class SmallToolsActivity extends BaseActivity {
    @BindView(R.id.gv_tools)
    GridView gvTools;

    private SmallToolsAdapter adapter;
    private List<SmallTools> list = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_small_tools;
    }

    @Override
    public void initView() {
        setTitle("小工具");
        adapter = new SmallToolsAdapter(this, list, R.layout.item_small_tools);
        gvTools.setAdapter(adapter);
    }

    @Override
    public void initData() {
        toolsList(sharedPreferencesHelper.isSwitchLanguage());
    }

//    @OnClick({R.id.tv_garage_loan, R.id.tv_ordinary})
//    public void onClick(View view) {
//        Bundle bundle = new Bundle();
//        String url = "";
//        String title = "";
//        switch (view.getId()) {
//            case R.id.tv_garage_loan:
//                url = "http://m.db.house.qq.com/index.php?mod=calculator&type=sd&rf=";
//                title = tvGarageLoan.getText().toString();
//                bundle.clear();
////                bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, url);
////                bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, tvGarageLoan.getText().toString());
////                ActivityUtils.overlay(this, WebActivity.class, bundle);
//                break;
//            case R.id.tv_ordinary:
//                url = "http://www.zxjsq.net/";
//                title = tvOrdinary.getText().toString();
//                bundle.clear();
////                bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, url);
////                bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, tvOrdinary.getText().toString());
////                ActivityUtils.overlay(this, WebActivity.class, bundle);
//                break;
//        }
//        bundle.clear();
//        bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, url);
//        bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, title);
//        ActivityUtils.overlay(this, WebActivity.class, bundle);
//    }

    private void toolsList(boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.TOOLS_LIST)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
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
                    List<SmallTools> smallTools = new Gson().fromJson(data, new TypeToken<List<SmallTools>>() {
                    }.getType());

                    list.clear();
                    list.addAll(smallTools);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnItemClick(R.id.gv_tools)
    public void onItemClick(int position){
        Bundle bundle = new Bundle();
        bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, list.get(position).getUrl());
        bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, list.get(position).getName());
        ActivityUtils.overlay(this, WebActivity.class, bundle);
    }
}
