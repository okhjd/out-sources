package com.dw.imximeng.activitys.myself;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.MessageEvent;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.ActivityForResultCode;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.CodeHelper;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.PhotoUtils;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.AlertViewDialog.AlertView;
import com.dw.imximeng.widgets.AlertViewDialog.OnItemClickListener;
import com.dw.imximeng.widgets.DateTimeDialog.PickerTimeActivityDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\30 0030
 */
public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_autograph)
    TextView tvAutograph;

    private int output_X = 480;
    private int output_Y = 480;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        setTitle("个人资料");
    }

    @Override
    public void initData() {
        if (BaseApplication.userInfo.getSessionid() != null) {
            setViewData();
        }
    }

    @OnClick({R.id.rl_head, R.id.rl_nickname, R.id.rl_sex, R.id.rl_birthday, R.id.rl_autograph})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.rl_head:
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
            case R.id.rl_nickname:
                bundle.putString(ActivityExtras.EXTRAS_EDIT_CONTENT, tvNickname.getText().toString());
                bundle.putString(ActivityExtras.EXTRAS_EDIT_TITLE, "修改昵称");
                bundle.putInt(ActivityExtras.EXTRAS_EDIT_TYPE, CodeHelper.NICKNAME);
                ActivityUtils.startForResult(this, EditActivity.class, ActivityForResultCode.MODIFY_NICKNAME, bundle);
                break;
            case R.id.rl_sex:
                new AlertView(null, null, "取消", null,
                        new String[]{"男", "女"},
                        this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                saveData(postMap(BaseApplication.userInfo.getSessionid(), 1, null,
                                        null, sharedPreferencesHelper.isSwitchLanguage()), null);
                                break;
                            case 1:
                                saveData(postMap(BaseApplication.userInfo.getSessionid(), 2, null,
                                        null, sharedPreferencesHelper.isSwitchLanguage()), null);
                                break;
                        }
                    }
                }).show();
                break;
            case R.id.rl_birthday:
                showTimeSelectDialog(tvBirthday.getText().toString(), tvBirthday);
                break;
            case R.id.rl_autograph:
                bundle.putString(ActivityExtras.EXTRAS_EDIT_CONTENT, tvAutograph.getText().toString());
                bundle.putString(ActivityExtras.EXTRAS_EDIT_TITLE, "修改签名");
                bundle.putInt(ActivityExtras.EXTRAS_EDIT_TYPE, CodeHelper.AUTOGRAPH);
                ActivityUtils.startForResult(this, EditActivity.class, ActivityForResultCode.MODIFY_AUTOGRAPH, bundle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ActivityForResultCode.MODIFY_NICKNAME:
                    BaseApplication.userInfo.setNickname(ActivityUtils.getStringExtra(data));
                    tvNickname.setText(ActivityUtils.getStringExtra(data));
                    break;
                case ActivityForResultCode.MODIFY_AUTOGRAPH:
                    BaseApplication.userInfo.setSignature(ActivityUtils.getStringExtra(data));
                    tvAutograph.setText(ActivityUtils.getStringExtra(data));
                    break;
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.dw.imximeng.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        showToast("设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                        File file = PhotoUtils.getFileByUri(this, cropImageUri);
                        saveData(postMap(BaseApplication.userInfo.getSessionid(), BaseApplication.userInfo.getSex(), null,
                                bitmap.toString(), sharedPreferencesHelper.isSwitchLanguage()), file);
                    }
                    break;
            }
        }
    }

    @Override
    public void finish() {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setMsgCode(MessageEvent.MessageType.REFRESH_MAIN);
        EventBus.getDefault().post(messageEvent);
        super.finish();
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

    private void showImages(Bitmap bitmap) {
        ivHead.setImageBitmap(bitmap);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private LinkedHashMap<String, String> postMap(String sessionid, int sex, String birthday, String hportrait, boolean language) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("language", language ? "cn" : "mn");//中文：cn，蒙古文：mn
        map.put("sessionid", StringUtils.stringsIsEmpty(sessionid));
        map.put("nickname", BaseApplication.userInfo.getNickname());
        map.put("sex", String.valueOf(sex));
        if (birthday != null) {
            map.put("birthday", birthday);
        }
        if (hportrait != null) {
            map.put("hportrait", hportrait);
        }
        return map;
    }

    private void saveData(final LinkedHashMap<String, String> map, File file) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.SAVE_DATA)
                .params(map)
                .addFile("hportrait", file.getName(), file)
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
                    if (map.get("birthday") != null) {
                        BaseApplication.userInfo.setBirthday(map.get("birthday"));
                    }
                    if (map.get("sex") != null) {
                        BaseApplication.userInfo.setSex(map.get("sex"));
                        BaseApplication.userInfo.setShowSex(map.get("sex").equals("1") ? "男" : "女");
                    }
                    if (map.get("hportrait") != null) {
                        BaseApplication.userInfo.setShowHportrait("file://" + cropImageUri.getPath());
                    }
                    setViewData();
                }
            }
        });
    }

    private void setViewData() {
        ImageLoader.getInstance().displayImage(BaseApplication.userInfo.getShowHportrait(), ivHead);
        tvNickname.setText(BaseApplication.userInfo.getNickname());
        tvSex.setText(BaseApplication.userInfo.getShowSex().equals("未设置") ? "" : BaseApplication.userInfo.getShowSex());
        tvBirthday.setText(BaseApplication.userInfo.getBirthday());
        tvAutograph.setText(BaseApplication.userInfo.getSignature());
    }

    /**
     * 时间选择控件方法
     */
    private void showTimeSelectDialog(String time, final TextView dateTextView) {
        PickerTimeActivityDialog dialog = new PickerTimeActivityDialog(this, time, true, dateTextView);
        dialog.show();
        dialog.setPickerDataOnClickListener(new PickerTimeActivityDialog.PickerDataOnClickListener() {
            @Override
            public void Onclick(String choiceTime) {
                saveData(postMap(BaseApplication.userInfo.getSessionid(), BaseApplication.userInfo.getSex(), choiceTime,
                        null, sharedPreferencesHelper.isSwitchLanguage()), null);
            }
        });
        WindowManager.LayoutParams wmParams = dialog.getWindow().getAttributes();
        wmParams.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(wmParams);
    }
}
