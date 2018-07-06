package com.dw.imximeng.activitys.advertisements;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.SelectionActivity;
import com.dw.imximeng.adapters.GvCateListAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.CateList;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.Selection;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.PhotoUtils;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AlertViewDialog.AlertView;
import com.dw.imximeng.widgets.AlertViewDialog.OnItemClickListener;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
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
//    @BindView(R.id.tv_house_type)
//    TextView tvHouseType;
    @BindView(R.id.et_phone)
    EditText etPhone;
//    @BindView(R.id.tv_houser_size)
//    TextView tvHouserSize;
    private String city;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private Uri imageUri;
    private File fileUri = null;
    private List<Uri> uris = new ArrayList<>();

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
        setTitle(city);
        tvSubmit.setVisibility(View.VISIBLE);
        tvSubmit.setText("发布");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_submit, R.id.iv_add, R.id.rl_cate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
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
            }
        }
    }

    private void showImages() {
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
                    CateList cateList = new Gson().fromJson(data, CateList.class);
                    ArrayList<Selection> list = new ArrayList<>();
                    for (int i=0;i<cateList.getCateList().size();i++){
                        Selection selection = new Selection();
                        selection.setId(cateList.getCateList().get(i).getId()+"");
                        selection.setName(cateList.getCateList().get(i).getName());
                        list.add(selection);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", list);
                    bundle.putBoolean("single", true);
                    ActivityUtils.overlay(ReleaseInfoActivity.this, SelectionActivity.class, bundle);
                }
            }
        });
    }
}
