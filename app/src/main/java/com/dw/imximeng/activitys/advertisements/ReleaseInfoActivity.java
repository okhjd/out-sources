package com.dw.imximeng.activitys.advertisements;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.SelectionActivity;
import com.dw.imximeng.activitys.SelectionCateActivity;
import com.dw.imximeng.activitys.myself.PaymentActivity;
import com.dw.imximeng.adapters.CateListAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.CateList;
import com.dw.imximeng.bean.PushAdInfo;
import com.dw.imximeng.bean.RegionList;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.Selection;
import com.dw.imximeng.helper.ActivityForResultCode;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.PhotoUtils;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AlertDialog;
import com.dw.imximeng.widgets.AlertViewDialog.AlertView;
import com.dw.imximeng.widgets.AlertViewDialog.OnItemClickListener;
import com.dw.imximeng.widgets.ListViewNoScroll;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\5 0005
 */
public class ReleaseInfoActivity extends BaseActivity {

    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_picture_num)
    TextView tvPictureNum;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.ll_images)
    LinearLayout llImages;
    @BindView(R.id.et_release)
    EditText etRelease;
    @BindView(R.id.tv_cate)
    TextView tvCate;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.lv_cate)
    ListViewNoScroll lvCate;
    @BindView(R.id.et_content)
    EditText etContent;
    private String city;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private Uri imageUri;
    private File fileUri = null;
    private List<Uri> uris = new ArrayList<>();
    private CateList cateList;
    private List<CateList.AttrList> attrList = new ArrayList<>();
    private CateListAdapter adapter;
    private Selection selection;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        city = ActivityUtils.getStringExtra(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_release_info;
    }

    @Override
    public void initView() {
        tvSubmit.setVisibility(View.VISIBLE);
        tvSubmit.setText("发布");
    }

    @Override
    public void initData() {
        getArea(BaseApplication.userInfo.getSessionid(), city, sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnClick({R.id.tv_submit, R.id.iv_add, R.id.rl_cate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                String content = etContent.getText().toString();
                String releaseTitle = etRelease.getText().toString();
                String phone = etPhone.getText().toString();
                if (content.isEmpty()) {
                    showToast("请输入广告内容");
                    return;
                }
                if (releaseTitle.isEmpty()) {
                    showToast("请输入广告标题");
                    return;
                }
                if (phone.isEmpty()) {
                    showToast("请输入联系电话");
                    return;
                }
                if (!StringUtils.isMobile(phone)) {
                    showToast("手机格式不正确");
                    return;
                }
                if (city == null || city.isEmpty()) {
                    showToast("请选择城市");
                    return;
                }
                if (selection == null) {
                    showToast("请选择分类");
                    return;
                }
                List<PushAdInfo> pushAdInfos = new ArrayList<>();
                for (int i = 0; i < attrList.size(); i++) {
                    PushAdInfo pushAdInfo = new PushAdInfo();
                    pushAdInfo.setCode(attrList.get(i).getCode());
                    pushAdInfo.setVals(attrList.get(i).getContent());
                    pushAdInfos.add(pushAdInfo);
                }
                String attrs = new Gson().toJson(pushAdInfos);
                if (cateList != null) {
                    if (cateList.getPubInfo().getCostPrice() > 0) {
                        //收费模式剩余次数为0时可付费发布，剩余次数大于0时可直接发布
                        if (cateList.getPubInfo().getSurplusNum() > 0) {
                            showPushFreeDialog(cateList.getPubInfo().getSurplusNum(), attrs, releaseTitle, content, phone);
                        } else {
                            //收费
                            showPushChargeDialog(cateList.getPubInfo().getSurplusNum(), attrs, releaseTitle, content, phone);
                        }
                    } else {
                        //免费模式且剩余次数为0时不可再发布
                        if (cateList.getPubInfo().getSurplusNum() > 0) {
                            showPushFreeDialog(cateList.getPubInfo().getSurplusNum(), attrs, releaseTitle, content, phone);
                        } else {
                            showPushFinishDialog();
                        }
                    }
                }

                break;
            case R.id.iv_add:
                fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
                new AlertView(null, null, "取消", null,
                        new String[]{"打开相机", "从相册中选择"},
                        this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                autoObtainCameraPermission();
                                break;
                            case 1:
                                autoObtainStoragePermission();
                                break;
                        }
                    }
                }).show();
                break;
            case R.id.rl_cate:
                getCateList(BaseApplication.userInfo.getSessionid(), sharedPreferencesHelper.isSwitchLanguage());
                break;
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showToast("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(this, "com.dw.imximeng.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                showToast("设备没有SD卡！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(this, "com.dw.imximeng.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        showToast("设备没有SD卡！");
                    }
                } else {

                    showToast("请允许打开相机！！");
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    showToast("请允许打操作SDCard！！");
                }
                break;
        }
    }


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    uris.add(imageUri);
                    showImages();
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.dw.imximeng.fileprovider", new File(newUri.getPath()));
                        uris.add(newUri);
                        showImages();
                    } else {
                        showToast("设备没有SD卡！");
                    }
                    break;
                case ActivityForResultCode.RESULT_CODE_SELECTION:
                    selection = ActivityUtils.getSerializableExtra(data);
                    tvCate.setText(selection.getName());

                    attrList.clear();
                    for (int i = 0; i < cateList.getCateList().size(); i++) {
                        if (selection.getId().equals(cateList.getCateList().get(i).getId() + "")) {
                            attrList.addAll(cateList.getCateList().get(i).getAttrList());
                        }
                    }
                    adapter = new CateListAdapter(ReleaseInfoActivity.this, attrList, R.layout.item_release_cate);
                    lvCate.setAdapter(adapter);
                    break;
                case ActivityForResultCode.RESULT_CODE_SELECTION_ITEM:
                    Bundle bundle = ActivityUtils.getParcelableExtra(data);
                    String title = bundle.getString(ActivityExtras.EXTRAS_SELECTION_TITLE);
                    String result = bundle.getString(ActivityExtras.EXTRAS_SELECTION_DATA);
                    if (attrList.isEmpty()) {
                        return;
                    }
                    for (int i = 0; i < attrList.size(); i++) {
                        assert title != null;
                        if (title.equals(attrList.get(i).getName())) {
                            attrList.get(i).setContent(result);
                        }
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    private void showImages() {
        tvPictureNum.setText(uris.size() + "/3");
        if (uris.size() >= 3) {
            ivAdd.setVisibility(View.GONE);
        } else {
            ivAdd.setVisibility(View.VISIBLE);
        }
        llImages.removeAllViews();
        for (int i = 0; i < uris.size(); i++) {
            final View view = LayoutInflater.from(this).inflate(R.layout.item_release_image, null);
            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
            ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    MaDensityUtils.dp2px(this, 79), MaDensityUtils.dp2px(this, 79));

            layoutParams.rightMargin = MaDensityUtils.dp2px(this, 10);
            view.setLayoutParams(layoutParams);


            Bitmap bitmap = PhotoUtils.getBitmapFromUri(uris.get(i), this);
            ivItem.setImageBitmap(bitmap);
            final int finalI = i;
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uris.remove(finalI);
                    showImages();
                }
            });
            llImages.addView(view);
        }
    }

    private void getCateList(String sessionid, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.INFORMATION_CATE_LIST)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))//中文：cn，蒙古文：mn
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
                    cateList = new Gson().fromJson(data, CateList.class);
                    ArrayList<Selection> list = new ArrayList<>();
                    for (int i = 0; i < cateList.getCateList().size(); i++) {
                        Selection selection = new Selection();
                        selection.setId(cateList.getCateList().get(i).getId() + "");
                        selection.setName(cateList.getCateList().get(i).getName());
                        if (tvCate.getText().toString().equals(cateList.getCateList().get(i).getName())) {
                            selection.setCheck(true);
                        }
                        list.add(selection);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ActivityExtras.EXTRAS_SELECTION_DATA, list);
                    bundle.putBoolean(ActivityExtras.EXTRAS_SELECTION_TYPE, true);
                    bundle.putString(ActivityExtras.EXTRAS_SELECTION_TITLE, "分类");
                    ActivityUtils.startForResult(ReleaseInfoActivity.this, SelectionActivity.class,
                            ActivityForResultCode.RESULT_CODE_SELECTION, bundle);
                }
            }
        });
    }

    @OnItemClick(R.id.lv_cate)
    public void onItemClick(AdapterView<?> parent, int position) {
        CateList.AttrList item = (CateList.AttrList) parent.getAdapter().getItem(position);

        if (item.getType().equals("text")) {
            return;
        }
        String[] contents = null;
        if (item.getContent() != null && !item.getContent().isEmpty()) {
            contents = item.getContent().split(",");
        }
        ArrayList<Selection> list = new ArrayList<>();
        for (int i = 0; i < item.getOptionList().size(); i++) {
            Selection selection = new Selection();
            selection.setId(item.getOptionList().get(i).getKey() + "");
            selection.setName(item.getOptionList().get(i).getVal());
            for (int j = 0; contents != null && j < contents.length; j++) {
                if (item.getOptionList().get(i).getVal().equals(contents[j])) {
                    selection.setCheck(true);
                }
            }
            list.add(selection);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(ActivityExtras.EXTRAS_SELECTION_DATA, list);
        bundle.putString(ActivityExtras.EXTRAS_SELECTION_TITLE, item.getName());
        if (item.getType().equals("singlesel")) {
            bundle.putBoolean(ActivityExtras.EXTRAS_SELECTION_TYPE, true);

        } else if (item.getType().equals("multisel")) {
            bundle.putBoolean(ActivityExtras.EXTRAS_SELECTION_TYPE, false);
        }
        ActivityUtils.startForResult(ReleaseInfoActivity.this, SelectionCateActivity.class,
                ActivityForResultCode.RESULT_CODE_SELECTION_ITEM, bundle);
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

    private void pushInfo(String sessionid, String cid, String area, String title,
                          String content, String telephone, String attribute, List<Uri> uris) {
        showProgressBar();
        PostFormBuilder p = OkHttpUtils.post().url(MethodHelper.PUSH_ADVERTISMENT_INFO);
        p.addParams("sessionid", StringUtils.stringsIsEmpty(sessionid));
        p.addParams("cid", cid);
        p.addParams("area", StringUtils.stringsIsEmpty(area));//地区ID
        p.addParams("title", title);
        p.addParams("content", content);
        p.addParams("telephone", telephone);
        if (attribute != null) {
            p.addParams("attribute", attribute);
        }
        for (int i = 0; i < uris.size(); i++) {
            String path = PhotoUtils.getFromMediaUri(this, getContentResolver(), uris.get(i));
            p.addFile("image_" + (i + 1), "image_" + (i + 1), new File(path));
        }
        p.build().execute(new Callback<Result>() {
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

    private void showPushFreeDialog(int num, final String attrs, final String releaseTitle, final String content, final String phone) {
        new AlertDialog(this)
                .builder()
                .setTitle("温馨提示")
                .setMsg("今日剩余发布条数：" + num + "条")
                .setPositiveButton("立即发布", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pushInfo(BaseApplication.userInfo.getSessionid(), selection.getId(), city, releaseTitle, content, phone, attrs, uris);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void showPushChargeDialog(final int num, final String attrs, final String releaseTitle, final String content, final String phone) {

        SpannableString spannableString = new SpannableString("今日免费发布次数已用完\n本次发布需支付：￥" + num);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#689df5"));
        spannableString.setSpan(colorSpan, 21, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        new AlertDialog(this)
                .builder()
                .setTitle("温馨提示")
                .setMsg(spannableString)
                .setPositiveButton("立即支付", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //开启支付页面
                        ActivityUtils.overlay(ReleaseInfoActivity.this, PaymentActivity.class, num + "");
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void showPushFinishDialog() {
        new AlertDialog(this)
                .builder()
                .setTitle("温馨提示")
                .setMsg("今日剩余发布次数已用完")
                .setPositiveButton("明天再来", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
}
