package com.dw.imximeng.activitys.myself;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.MainActivity;
import com.dw.imximeng.R;
import com.dw.imximeng.activitys.signIn.SignInActivity;
import com.dw.imximeng.app.AppManager;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserInfo;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\3 0003
 */
public class ModifyPasswordActivity extends BaseActivity {
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.cb_old_password)
    CheckBox cbOldPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.cb_new_password)
    CheckBox cbNewPassword;
    @BindView(R.id.et_repeat_password)
    EditText etRepeatPassword;
    @BindView(R.id.cb_repeat_password)
    CheckBox cbRepeatPassword;
    @BindView(R.id.tv_complete)
    TextView tvComplete;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    public void initView() {
        setTitle("修改登录密码");
    }

    @Override
    public void initData() {

    }

    @OnTextChanged({R.id.et_old_password, R.id.et_new_password, R.id.et_repeat_password})
    public void onTextChanged(CharSequence c) {
        if (!etOldPassword.getText().toString().isEmpty() && !etNewPassword.getText().toString().isEmpty() &&
                !etRepeatPassword.getText().toString().isEmpty()) {
            tvComplete.setBackgroundResource(R.drawable.shape_sign_in_btn_enable);
            tvComplete.setEnabled(true);
        } else {
            tvComplete.setBackgroundResource(R.drawable.shape_modify_btn);
            tvComplete.setEnabled(false);
        }
    }

    @OnClick({R.id.cb_old_password, R.id.cb_new_password, R.id.cb_repeat_password, R.id.tv_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_old_password:
                if (cbOldPassword.isChecked()) {
                    etOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etOldPassword.setSelection(etOldPassword.getText().toString().length());
                break;
            case R.id.cb_new_password:
                if (cbNewPassword.isChecked()) {
                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etNewPassword.setSelection(etNewPassword.getText().toString().length());
                break;
            case R.id.cb_repeat_password:
                if (cbRepeatPassword.isChecked()) {
                    etRepeatPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etRepeatPassword.setSelection(etRepeatPassword.getText().toString().length());
                break;
            case R.id.tv_complete:
                String oldPassword = etOldPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String repeatPassword = etRepeatPassword.getText().toString().trim();
                if (oldPassword.isEmpty()) {
                    showToast("请输入旧密码");
                    return;
                }
                if (newPassword.isEmpty()) {
                    showToast("请输入新密码");
                    return;
                }
                if (repeatPassword.isEmpty()) {
                    showToast("请重复新密码");
                    return;
                }
                if (!newPassword.equals(repeatPassword)) {
                    showToast("密码不一致");
                    return;
                }
                modifyPassword(BaseApplication.userInfo.getSessionid(), oldPassword, newPassword, repeatPassword);
                break;
        }
    }

    private void modifyPassword(String sessionid, String loginpass, String password, String suerpass) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.MODIFY_PASSWORD)
                .addParams("sessionid", sessionid)
                .addParams("loginpass", loginpass)
                .addParams("password", password)
                .addParams("suerpass", suerpass)
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
                    BaseApplication.userInfo = new UserInfo();
                    sharedPreferencesHelper.setUserPassword("");
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setMsgCode(MessageEvent.MessageType.REFRESH_MAIN);
                    EventBus.getDefault().post(messageEvent);

                    ActivityUtils.forward(getApplicationContext(), SignInActivity.class);
                    AppManager.getAppManager().finishActivity(SetActivity.class);
                    finish();
                }
            }
        });
    }
}
