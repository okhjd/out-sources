package com.dw.imximeng.activitys.myself;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserInfo;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.SharedPreferencesHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\1 0001
 */
public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_post)
    TextView tvPost;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        setTitle("意见反馈");
    }

    @Override
    public void initData() {

    }

    @OnTextChanged({R.id.et_content})
    public void onTextChanged(CharSequence c) {
        tvNum.setText(c.length() + "/300");
        if (!etContent.getText().toString().isEmpty() ) {
            tvPost.setBackgroundResource(R.drawable.shape_sign_in_btn_enable);
            tvPost.setEnabled(true);
        } else {
            tvPost.setBackgroundResource(R.drawable.shape_sign_in_btn);
            tvPost.setEnabled(false);
        }
    }

    @OnClick(R.id.tv_post)
    public void onClick() {
        postFeedback(BaseApplication.userInfo.getSessionid(), etContent.getText().toString());
    }

    private void postFeedback(String sessionid, String content) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.FEEDBACK)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
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
                showToast(response.getMessage());
                if (response.getStatus() == 1) {
                    finish();
                }
            }
        });
    }
}
