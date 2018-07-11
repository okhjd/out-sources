package com.dw.imximeng.activitys;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.ImagePageAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.BitmapUtil;
import com.dw.imximeng.helper.ScannerUtils;
import com.dw.imximeng.widgets.MyViewPager;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * @author hjd
 * @create-time 2018-07-11 09:22:56
 */
public class LargerImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener, ImagePageAdapter.OnPhotoClickListener {
    @BindView(R.id.vp_image)
    MyViewPager vpImage;
    @BindView(R.id.rl_bar)
    RelativeLayout rlBar;

    private ImagePageAdapter adapter;
    private List<String> imgList = new ArrayList<>();
    private int position = 0;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        if (bundle != null) {
            imgList.addAll((ArrayList<String>) bundle.getSerializable(ActivityExtras.EXTRAS_IMAGE_LARGER_LIST));
            position = bundle.getInt(ActivityExtras.EXTRAS_IMAGE_LARGER_POSITION);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_larger_image;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.black), false);
        setTitle(String.valueOf(position + 1) + "/" + imgList.size());

        adapter = new ImagePageAdapter(imgList, this);
        vpImage.setAdapter(adapter);
        vpImage.setCurrentItem(position);
        vpImage.addOnPageChangeListener(this);
        adapter.setOnPhotoClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.position = position;
        setTitle(String.valueOf(position + 1) + "/" + imgList.size());
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPhotoClick() {
        if (rlBar.getVisibility() == View.GONE) {
//            quitFullScreen();
            rlBar.setVisibility(View.VISIBLE);
        } else {
//            setFullScreen();
            rlBar.setVisibility(View.GONE);
        }
    }

    private void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void quitFullScreen() {
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * @param activity
     * @return 判断当前手机是否是全屏
     */
    private boolean isFullScreen(Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        if ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        } else {
            return false;
        }
    }

    @OnClick(R.id.tv_submit)
    public void onClick() {
        PhotoView image = (PhotoView) vpImage.getChildAt(position);
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ScannerUtils.saveImageToGallery(this, bitmap,
                ScannerUtils.ScannerType.RECEIVER);
    }
}
