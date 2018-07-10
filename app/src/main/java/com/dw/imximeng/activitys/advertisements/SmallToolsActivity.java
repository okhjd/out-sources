package com.dw.imximeng.activitys.advertisements;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
    public void onItemClick(int position) {
        if (list.get(position).getName().equals("普通计算器")){
            openJS();
        }else {
            Bundle bundle = new Bundle();
            bundle.putString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, list.get(position).getUrl());
            bundle.putString(ActivityExtras.EXTRAS_WEB_TITLE, list.get(position).getName());
            ActivityUtils.overlay(this, WebActivity.class, bundle);
        }
    }

    /**
     * 打开计算机
     */
    private void openJS() {
        PackageInfo pak = getAllApps(this, "Calculator", "calculator"); //大小写
        if (pak != null) {
            Intent intent = new Intent();
            intent = this.getPackageManager().getLaunchIntentForPackage(pak.packageName);
            startActivity(intent);
        } else {
            showToast("未找到计算器");
        }
    }

    private PackageInfo getAllApps(Context context, String app_flag_1, String app_flag_2) {
        PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);
        for (int i = 0; i < packlist.size(); i++) {
            PackageInfo pak = (PackageInfo) packlist.get(i);
            if (pak.packageName.contains(app_flag_1) || pak.packageName.contains(app_flag_2)) {
                return pak;
            }
        }
        return null;
    }
}
