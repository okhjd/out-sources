package com.dw.imximeng.activitys.myself;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.widgets.AlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hjd
 * @Created_Time 2018\7\1 0001
 */
public class OnlineServiceActivity extends BaseActivity {
    @BindView(R.id.tv_telephone)
    TextView tvTelephone;

    @Override
    public int getLayoutId() {
        return R.layout.activity_online_service;
    }

    @Override
    public void initView() {
        setTitle("在线客服");
        if (BaseApplication.userSiteInfo != null) {
            tvTelephone.setText(BaseApplication.userSiteInfo.getData().getCs_phone());
        }
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.ll_hotline, R.id.tv_tecent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_hotline:
                showDialog();
                break;
            case R.id.tv_tecent:
                // 跳转之前，可以先判断手机是否安装QQ
                if (isQQClientAvailable(this)) {
                    // 跳转到客服的QQ
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + BaseApplication.userSiteInfo.getData().getCservice_qq();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    // 跳转前先判断Uri是否存在，如果打开一个不存在的Uri，App可能会崩溃
                    if (isValidIntent(this, intent)) {
                        startActivity(intent);
                    }
                }else {
                    showToast("您手机上没有QQ应用");
                }
                break;
        }
    }

    private void showDialog() {
        new AlertDialog(this)
                .builder()
                .setMsg("是否拨打热线电话")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        diallPhone(tvTelephone.getText().toString().trim());
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 Uri是否有效
     */
    public static boolean isValidIntent(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return !activities.isEmpty();
    }
}
