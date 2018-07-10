package com.dw.imximeng.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dw.imximeng.MainActivity;
import com.dw.imximeng.R;
import com.dw.imximeng.activitys.signIn.SignInActivity;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.helper.SharedPreferencesHelper;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class VpGuideAdapter extends PagerAdapter implements View.OnClickListener {
    private int[] imageViews;
    private BaseActivity activity;

    public VpGuideAdapter(BaseActivity activity, int[] imageViews) {
        this.imageViews = imageViews;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return imageViews.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_guide_image, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.iv_guide);
        imageView.setImageResource(imageViews[position]);
        view.setForegroundGravity(Gravity.CENTER);
//        container.addView(view);
//        ImageView iv = new ImageView(activity);
//        iv.setLayoutParams(new ViewPager.LayoutParams());//设置布局
//        iv.setImageResource(imageViews[position]);//为ImageView添加图片资源
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);//这里也是一个图片的适配
//        if (position == imageViews.length -1 ){
//            //为最后一张图片添加点击事件
//            iv.setOnClickListener(this);
//        }
//        container.addView(iv);

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(activity.getResources(), imageViews[position], options);
//        options.inSampleSize = 2;
//        options.inJustDecodeBounds = false;
//        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), imageViews[position], options);
//        imageView.setImageBitmap(bitmap);
        if (position == imageViews.length - 1) {
            //为最后一张图片添加点击事件
            imageView.setOnClickListener(this);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        new SharedPreferencesHelper(activity).setFirstTimeLaunch(false);
        Intent toMainActivity = new Intent(activity, MainActivity.class);//跳转到主界面
        activity.startActivity(toMainActivity);
        activity.finish();
    }
}
