package com.dw.imximeng.fragments.main;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.myself.MessageActivity;
import com.dw.imximeng.activitys.myself.MyPointsActivity;
import com.dw.imximeng.activitys.myself.MyWalletActivity;
import com.dw.imximeng.activitys.myself.SetActivity;
import com.dw.imximeng.activitys.myself.UserInfoActivity;
import com.dw.imximeng.activitys.signIn.SignInActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.base.BaseFragment;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.ShareInfo;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.ImageLoaderUtils;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.widgets.ImageViewRoundOval;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class MyselfFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_set)
    TextView tvSet;
    @BindView(R.id.iv_head)
    ImageViewRoundOval ivHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_tips)
    TextView tvUserTips;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.tv_my_release)
    TextView tvMyRelease;
    @BindView(R.id.tv_share)
    TextView tvShare;

    private ShareInfo shareInfo;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_myself;
    }

    @Override
    public void initView(View view) {
        tvTitle.setText("我");
        ivHead.setType(ImageViewRoundOval.TYPE_ROUND);
        ivHead.setRoundRadius(MaDensityUtils.dp2px(getActivity(), 5));//圆角大小

    }

    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (BaseApplication.userInfo.getSessionid() != null) {
            ImageLoader.getInstance().displayImage(BaseApplication.userInfo.getShowHportrait(), ivHead,
                    ImageLoaderUtils.getDisplayImageOptionsRound(R.mipmap.pic32, MaDensityUtils.dp2px(getActivity(), 5)));
            tvUserName.setText(BaseApplication.userInfo.getNickname());
            tvUserTips.setText("欢迎回来");
        } else {
            ivHead.setImageResource(R.mipmap.pic32);
            tvUserName.setText("点击登录");
            tvUserTips.setText("您还没有登录，点我前往登录");
        }
    }

    @OnClick({R.id.rl_user_info, R.id.tv_message, R.id.tv_wallet, R.id.tv_integral,
            R.id.tv_my_release, R.id.tv_share, R.id.tv_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_set:
                if (BaseApplication.userInfo.getSessionid() != null) {
                    ActivityUtils.overlay(getActivity(), SetActivity.class);
                } else {
                    ActivityUtils.overlay(getActivity(), SignInActivity.class);
                }
                break;
            case R.id.rl_user_info:
                if (BaseApplication.userInfo.getSessionid() == null) {
                    ActivityUtils.overlay(getActivity(), SignInActivity.class);
                } else {
                    ActivityUtils.overlay(getActivity(), UserInfoActivity.class);
                }
                break;
            case R.id.tv_message://消息
                if (BaseApplication.userInfo.getSessionid() != null) {
                    ActivityUtils.overlay(getActivity(), MessageActivity.class);
                } else {
                    ActivityUtils.overlay(getActivity(), SignInActivity.class);
                }
                break;
            case R.id.tv_wallet://钱包
                if (BaseApplication.userInfo.getSessionid() != null) {
                    ActivityUtils.overlay(getActivity(), MyWalletActivity.class);
                } else {
                    ActivityUtils.overlay(getActivity(), SignInActivity.class);
                }
                break;
            case R.id.tv_integral://积分
                if (BaseApplication.userInfo.getSessionid() != null) {
                    ActivityUtils.overlay(getActivity(), MyPointsActivity.class);
                } else {
                    ActivityUtils.overlay(getActivity(), SignInActivity.class);
                }
                break;
            case R.id.tv_my_release://我的发布
                if (BaseApplication.userInfo.getSessionid() != null) {

                } else {
                    ActivityUtils.overlay(getActivity(), SignInActivity.class);
                }
                break;
            case R.id.tv_share://分享
                if (shareInfo == null) {
                    return;
                }
                UMImage umImage = new UMImage(getActivity(), shareInfo.getShare_icon());
                UMWeb web = new UMWeb(shareInfo.getShare_url());
                web.setTitle(shareInfo.getShare_title());//标题
                web.setThumb(umImage);  //缩略图
                web.setDescription(shareInfo.getShare_brief());//描述
                new ShareAction(getActivity())
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(shareListener).open();
                break;
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToast("分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showToast("失败" + t.getMessage());
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showToast("分享取消");
        }
    };

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(final MessageEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MessageEvent.MessageType.REFRESH_MAIN == event.getMsgCode()) {
                    initData();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        getShareInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void getShareInfo() {
        OkHttpUtils.post().url(MethodHelper.SHARE_INFO)
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {

                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    shareInfo = new Gson().fromJson(data, ShareInfo.class);
                }
            }
        });
    }
}
