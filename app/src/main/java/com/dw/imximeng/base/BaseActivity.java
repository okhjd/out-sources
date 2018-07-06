package com.dw.imximeng.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dw.imximeng.R;
import com.dw.imximeng.app.AppManager;
import com.dw.imximeng.app.AppVersionInfo;
import com.dw.imximeng.helper.SharedPreferencesHelper;
import com.dw.imximeng.interf.BaseViewInterface;
import com.dw.imximeng.widgets.LoadingAlertDialog;
import com.githang.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseViewInterface {
    protected LayoutInflater mInflater;
    private Unbinder unbinder;
    private LoadingAlertDialog dialog;
    public SharedPreferencesHelper sharedPreferencesHelper;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppManager.getAppManager().addActivity(this);
        init(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white), true);
        mInflater = getLayoutInflater();

        unbinder = ButterKnife.bind(this);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        initView();
        initData();
        switchLanguage(new SharedPreferencesHelper(this).isSwitchLanguage());

        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);

    }

    protected void switchLanguage(boolean language) {

    }

    protected void init(Bundle savedInstanceState) {
    }

    public void setTitle(String title) {
        if (getLayoutId() != 0) {
            TextView tvTitle = (TextView) findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
    }

    public void setSubmit(String str) {
        if (getLayoutId() != 0) {
            TextView tvSubmit = (TextView) findViewById(R.id.tv_submit);
            tvSubmit.setText(str);
            tvSubmit.setVisibility(View.VISIBLE);
        }
    }

    public void showToast(String msgStr) {
        Toast.makeText(this, msgStr, Toast.LENGTH_LONG).show();
    }

    public void showToast(int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_LONG).show();
    }

    public void showProgressBar() {
        dialog = new LoadingAlertDialog(this);
        dialog.show("请稍候...");
    }

    public void closeProgressBar() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void back(View view) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //取消某个请求
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 退出当前应用
     */
    protected void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            MobclickAgent.onKillProcess(this);
            AppManager.getAppManager().appExit();
        }
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public String getAppVersionName() {
        return "V" + AppVersionInfo.getVersionName(this);
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getAppVersionCode() {
        return AppVersionInfo.getVersionCode(this);
    }
}
