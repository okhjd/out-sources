package com.dw.imximeng.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dw.imximeng.helper.BitmapUtil;
import com.dw.imximeng.helper.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author hjd
 * @time 2016-06-15 09:00
 */
public class ImagePageAdapter extends PagerAdapter {
    private List<String> views = null ;
    private Context mContext;
    private int mChildCount = 0;

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    private OnPhotoClickListener onPhotoClickListener;
    /**
     * 初始化数据源, 即View数组
     */
    public void addView(String view) {
        views.add(view);
    }

    public ImagePageAdapter(List<String> views , Context mContext) {
        this.views = views;
        this.mContext = mContext;
    }
    /**
     * 从ViewPager中删除集合中对应索引的View对象
     */
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((PhotoView)object);
    }
    /**
     * 获取ViewPager的个数
     */
    @Override
    public int getCount() {
        return views.size();
    }
    /**
     * 从View集合中获取对应索引的元素, 并添加到ViewPager中
     */
    @Override
    public Object instantiateItem(View container, final int position) {
        PhotoView img = new PhotoView(mContext);
        //设置imageview 铺满整个布局
        //按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        img.refreshDrawableState();
        img.setTag(position);
        if (StringUtils.isUrl(views.get(position))){
            ImageLoader.getInstance().displayImage(views.get(position).trim(), img);
        }else {
            img.setImageBitmap(BitmapUtil.getSmallBitmap(views.get(position)));
        }

        ((ViewPager) container).addView(img);
        img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (onPhotoClickListener != null)
                    onPhotoClickListener.onPhotoClick();
            }
        });
        return img;
    }
    /**
     * 是否将显示的ViewPager页面与instantiateItem返回的对象进行关联
     * 这个方法是必须实现的
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    public void clear() {
        views.clear();
    }

    @Override
    public int getItemPosition(Object object) {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    public interface OnPhotoClickListener{
        void onPhotoClick();
    }
}