package com.dw.imximeng.activitys.advertisements;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.CommentItem;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\7 0007
 */
public class ReportActivity extends BaseActivity {
    @BindView(R.id.lv_report)
    ListView lvReport;

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

    }

    @OnClick(R.id.tv_submit_report)
    public void onClick() {
    }

    private void pushReport(String sessionid,String id, String type, String content) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.TO_REPORT)
                .addParams("iid", id)
                .addParams("sessionid", StringUtils.stringsIsEmpty())
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
}
