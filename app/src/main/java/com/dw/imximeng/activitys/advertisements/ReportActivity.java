package com.dw.imximeng.activitys.advertisements;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.ReportTypeAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.CommentItem;
import com.dw.imximeng.bean.ReportType;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.commonsdk.statistics.common.MLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\7 0007
 */
public class ReportActivity extends BaseActivity {
    @BindView(R.id.lv_report)
    ListView lvReport;

    private List<ReportType> mList = new ArrayList<>();
    private ReportTypeAdapter adapter;

    private String id;
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        if (bundle != null) {
            id = bundle.getString(ActivityExtras.EXTRAS_INFO_DETAILS_ID);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    public void initView() {
        setTitle("举报");
    }

    @Override
    public void initData() {
        getReportList(sharedPreferencesHelper.isSwitchLanguage());
        adapter = new ReportTypeAdapter(this, mList, R.layout.item_selection);
        lvReport.setAdapter(adapter);
    }

    @OnClick(R.id.tv_submit_report)
    public void onClick() {
        if (adapter.checkReport().isEmpty()){
            showToast("请选择举报内容");
            return;
        }
        pushReport(BaseApplication.userInfo.getSessionid(), id, adapter.checkReport(), adapter.checkReport());
    }

    private void pushReport(String sessionid,String id, String type, String content) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.TO_REPORT)
                .addParams("iid", id)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("type", type)
                .addParams("content", content)
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
                    ActivityUtils.forward(ReportActivity.this,ReportResultActivity.class);
                }
            }
        });
    }

    private void getReportList(boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.REPORT_TYPE)
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
                    List<String> list = new Gson().fromJson(data, new TypeToken<List<String>>(){}.getType());
                    mList.clear();
                    for (int i=0;i<list.size();i++){
                        ReportType reportType = new ReportType();
                        reportType.setContent(list.get(i));
                        mList.add(reportType);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnItemClick(R.id.lv_report)
    public void onItemClick(int position){
        for (int i = 0;i< mList.size();i++){
            mList.get(i).setCheck(false);
        }
        mList.get(position).setCheck(!mList.get(position).isCheck());
        adapter.notifyDataSetChanged();
    }

}
