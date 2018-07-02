package com.dw.imximeng.activitys.myself;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.CodeHelper;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\30 0030
 */
public class EditActivity extends BaseActivity {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_save)
    TextView tvSave;

    private String title;
    private String content;
    private int type;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        title = bundle.getString(ActivityExtras.EXTRAS_EDIT_TITLE);
        content = bundle.getString(ActivityExtras.EXTRAS_EDIT_CONTENT);
        type = bundle.getInt(ActivityExtras.EXTRAS_EDIT_TYPE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit;
    }

    @Override
    public void initView() {
        setTitle(title);
        etContent.setText(content);
        etContent.setSelection(etContent.getText().toString().length());
    }

    @Override
    public void initData() {

    }

    @OnTextChanged({R.id.et_content})
    public void onTextChanged(CharSequence c) {
        if (!etContent.getText().toString().isEmpty()) {
            tvSave.setEnabled(true);
        } else {
            tvSave.setEnabled(false);
        }
    }

    @OnClick(R.id.tv_save)
    public void onClick() {
        String content = etContent.getText().toString();
        saveData(postMap(BaseApplication.userInfo.getSessionid(), content, 0, "",
                content, "", sharedPreferencesHelper.isSwitchLanguage()));
    }

    private LinkedHashMap<String, String> postMap(String sessionid, String nickname, int sex, String birthday,
                                                  String signature, String hportrait, boolean language) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("language", language ? "cn" : "mn");//中文：cn，蒙古文：mn
        map.put("sessionid", StringUtils.stringsIsEmpty(sessionid));
        map.put("sex", BaseApplication.userInfo.getSex() + "");
//        map.put("birthday", birthday)
//        map.put("hportrait", hportrait)
        switch (type) {
            case CodeHelper.NICKNAME:
                map.put("nickname", nickname);
                break;
            case CodeHelper.AUTOGRAPH:
                map.put("nickname", BaseApplication.userInfo.getNickname());
                map.put("signature", signature);
                break;
        }
        return map;
    }

    private void saveData(LinkedHashMap<String, String> map) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.SAVE_DATA)
                .params(map)
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
                    //通知MainActivity做登录获取用户信息（没有单独的获取用户信息的接口），暂不使用
//                    MessageEvent messageEvent = new MessageEvent();
//                    messageEvent.setMsgCode(MessageEvent.MessageType.SIGN_IN);
//                    EventBus.getDefault().post(messageEvent);
                    ActivityUtils.setResult(EditActivity.this, RESULT_OK, etContent.getText().toString());
                }
            }
        });
    }
}
