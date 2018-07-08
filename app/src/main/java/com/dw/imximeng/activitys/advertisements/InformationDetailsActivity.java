package com.dw.imximeng.activitys.advertisements;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.CommentListAdapter;
import com.dw.imximeng.adapters.GvInfoImageAdapter;
import com.dw.imximeng.adapters.InfoDetailsCateAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.CateList;
import com.dw.imximeng.bean.CommentItem;
import com.dw.imximeng.bean.InfoDetails;
import com.dw.imximeng.bean.RegionList;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.ShareInfo;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AlertDialog;
import com.dw.imximeng.widgets.GridViewNoScroll;
import com.dw.imximeng.widgets.ImageViewRoundOval;
import com.dw.imximeng.widgets.ListViewNoScroll;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\7 0007
 */
public class InformationDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    ImageViewRoundOval ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_push_title)
    TextView tvPushTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.gv_image)
    GridViewNoScroll gvImage;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.lv_comments)
    ListViewNoScroll lvComments;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.rl_comments)
    RelativeLayout rlComments;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.et_push_content)
    EditText etPushContent;
    @BindView(R.id.tv_push_comment)
    TextView tvPushComment;
    @BindView(R.id.rl_push_comment)
    RelativeLayout rlPushComment;
    @BindView(R.id.iv_collection)
    ImageView ivCollection;
    @BindView(R.id.lv_cate)
    ListViewNoScroll lvCate;
    @BindView(R.id.iv_report)
    ImageView ivReport;
    private String city;
    private String id;
    private int page = 1;
    private CommentListAdapter adapter;
    private List<CommentItem> list = new ArrayList<>();
    private int pid = 0;
    private boolean isCollect = false;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        city = bundle.getString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID);
        id = bundle.getString(ActivityExtras.EXTRAS_INFO_DETAILS_ID);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_info_details;
    }

    @Override
    public void initView() {
        adapter = new CommentListAdapter(this, list, R.layout.item_comment);
        lvComments.setAdapter(adapter);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pid = (int) v.getTag();
                showKeyboard();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        getArea(BaseApplication.userInfo.getSessionid(), city, sharedPreferencesHelper.isSwitchLanguage());
        getInfoDetails(BaseApplication.userInfo.getSessionid(), id, sharedPreferencesHelper.isSwitchLanguage());
        getCommentList(id, page);
        getShareInfo();
    }

    private void getInfoDetails(String sessionid, String id, boolean language) {
        OkHttpUtils.post().url(MethodHelper.INFO_DETETAILS)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("id", StringUtils.stringsIsEmpty(id))//地区ID
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
            }

            @Override
            public void onResponse(Result response, int id) {
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    InfoDetails dataBean = new Gson().fromJson(data, InfoDetails.class);
                    setViewValue(dataBean);
                }
            }
        });
    }

    private void setViewValue(InfoDetails infoDetails) {
        ivHead.setType(ImageViewRoundOval.TYPE_ROUND);
        ivHead.setRoundRadius(MaDensityUtils.dp2px(this, 5));//圆角大小
        ImageLoader.getInstance().displayImage(infoDetails.getHportrait(), ivHead);

        tvName.setText(infoDetails.getShowName());
        tvType.setText(infoDetails.getCatename());
        tvPushTitle.setText(infoDetails.getTitle());
        tvContent.setText(infoDetails.getContent());
        tvPhone.setText(infoDetails.getShowTelephone());
        tvTime.setText(infoDetails.getShowTime());

        if (infoDetails.getImgList().size() > 1) {
            gvImage.setAdapter(new GvInfoImageAdapter(this, infoDetails.getImgList(), R.layout.item_details_image));
            ivImage.setVisibility(View.GONE);
            gvImage.setVisibility(View.VISIBLE);
        } else if (infoDetails.getImgList().size() == 1) {
            ImageLoader.getInstance().displayImage(infoDetails.getImgList().get(0).getShowImg(), ivImage);
            ivImage.setVisibility(View.VISIBLE);
            gvImage.setVisibility(View.GONE);
        } else {
            ivImage.setVisibility(View.GONE);
            gvImage.setVisibility(View.GONE);
        }

        ivCollection.setTag(infoDetails.isIscollect());
        isCollect = infoDetails.isIscollect();
        if (isCollect) {
            ivCollection.setImageResource(R.mipmap.pic6);
        } else {
            ivCollection.setImageResource(R.mipmap.pic43);
        }

        if (infoDetails.getAttrList().isEmpty()) {
            lvCate.setVisibility(View.GONE);
        } else {
            lvCate.setVisibility(View.VISIBLE);
            InfoDetailsCateAdapter adapter = new InfoDetailsCateAdapter(this, infoDetails.getAttrList(), R.layout.item_details_cate);
            lvCate.setAdapter(adapter);
        }

        if (infoDetails.isIsreport()){
            ivReport.setVisibility(View.GONE);
        }else {
            ivReport.setVisibility(View.VISIBLE);
        }

    }

    private void getArea(String sessionid, String area, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.USER_AREA_INFO)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("area", StringUtils.stringsIsEmpty(area))//地区ID
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
                    RegionList.DataBean dataBean = new Gson().fromJson(data, RegionList.DataBean.class);
                    setTitle(dataBean.getName());
                }
            }
        });
    }

    @OnClick({R.id.iv_report, R.id.iv_share, R.id.iv_collection, R.id.tv_phone, R.id.iv_comment, R.id.tv_more, R.id.tv_comment,
            R.id.tv_push_comment, R.id.rl_push_comment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_report:
                Bundle bundle = new Bundle();
                bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_ID,id);
                ActivityUtils.overlay(this, ReportActivity.class, bundle);
                break;
            case R.id.iv_share:
                if (shareInfo == null) {
                    return;
                }
                UMImage umImage = new UMImage(this, shareInfo.getShare_icon());
                UMWeb web = new UMWeb(shareInfo.getShare_url());
                web.setTitle(shareInfo.getShare_title());//标题
                web.setThumb(umImage);  //缩略图
                web.setDescription(shareInfo.getShare_brief());//描述
                new ShareAction(this)
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(shareListener).open();
                break;
            case R.id.iv_collection:
                collectInfo(BaseApplication.userInfo.getSessionid(), id);
                break;
            case R.id.tv_phone:
                showDialog();
                break;
            case R.id.iv_comment:
                showKeyboard();
                break;
            case R.id.tv_more:
                page++;
                getCommentList(id, page);
                break;
            case R.id.tv_comment:
                showKeyboard();
                break;
            case R.id.rl_push_comment:
                hideKeyboard();
                break;
            case R.id.tv_push_comment:
                String content = etPushContent.getText().toString();
                if (content.isEmpty()) {
                    showToast("请填写评论内容");
                    return;
                }
                hideKeyboard();
                pushCommentInfo(BaseApplication.userInfo.getSessionid(), content, id, pid);
                break;
        }
    }

    private void getCommentList(String id, int cpage) {
        OkHttpUtils.post().url(MethodHelper.COMMENT_LIST)
                .addParams("iid", id)
                .addParams("cpage", String.valueOf(cpage))
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
                rlComments.setVisibility(View.GONE);
                llEmpty.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(Result response, int id) {
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    List<CommentItem> dataBean = new Gson().fromJson(data, new TypeToken<List<CommentItem>>() {
                    }.getType());
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(dataBean);
                    adapter.notifyDataSetChanged();
                } else {
                    rlComments.setVisibility(View.GONE);
                    llEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void pushCommentInfo(String sessionid, String content, String id, int pid) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.PUSH_COMMENT_INFO)
                .addParams("iid", id)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("content", content)
                .addParams("pid", String.valueOf(pid))
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

                }
            }
        });
    }

    private void collectInfo(String sessionid, String id) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.COLLECT_INFO)
                .addParams("iid", id)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
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
                    isCollect = !isCollect;
                    if (isCollect) {
                        ivCollection.setImageResource(R.mipmap.pic6);
                    } else {
                        ivCollection.setImageResource(R.mipmap.pic43);
                    }
                }
            }
        });
    }

    //显示布局与键盘
    private void showKeyboard() {
        rlPushComment.setVisibility(View.VISIBLE);//显示布局
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //隐藏键盘与布局
    private void hideKeyboard() {
        rlPushComment.setVisibility(View.GONE);//隐藏布局
        etPushContent.setText("");//清空输入
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // 捕获返回键的方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //隐藏键盘与布局
        hideKeyboard();
        return true;
    }

    private void showDialog() {
        new AlertDialog(this)
                .builder()
                .setMsg("是否拨打电话")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        diallPhone(tvPhone.getText().toString().trim());
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

    private ShareInfo shareInfo;

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
}
