package com.dw.imximeng.widgets;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;

import com.dw.imximeng.helper.MaDensityUtils;

/**
 * @author hjd
 * @create-time 2018-07-03 16:02:46
 */
public class ItemPasswordLayout extends EditText {
    /**
     * 间隔
     */
    private final int PWD_SPACING = 5;
    /**
     * 密码大小
     */
    private final int PWD_SIZE = 5;
    /**
     * 密码长度
     */
    private final int PWD_LENGTH = 6;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;
    /**
     * 密码框
     */
    private Rect mRect;

    /**
     * 密码画笔
     */
    private Paint mPwdPaint;

    /**
     * 密码框画笔
     */
    private Paint mRectPaint;
    /**
     * 输入的密码长度
     */
    private int mInputLength;

    /**
     * 输入结束监听
     */
    private OnInputFinishListener mOnInputFinishListener;

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public ItemPasswordLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化密码画笔
        mPwdPaint = new Paint();
        mPwdPaint.setColor(Color.BLACK);
        mPwdPaint.setStyle(Paint.Style.FILL);
        mPwdPaint.setAntiAlias(true);
        // 初始化密码框
        mRectPaint = new Paint();
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(Color.LTGRAY);
        mRectPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();

        // 这三行代码非常关键，大家可以注释点在看看效果
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
//        canvas.drawRect(0, 2, mWidth, mHeight-2, paint);
        canvas.drawRoundRect(new RectF(0, 0, mWidth, mHeight), 25, 25, paint);

        mRectPaint.setStrokeWidth(MaDensityUtils.dp2pix(getContext(),1));
        mRectPaint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(MaDensityUtils.dp2pix(getContext(),1), MaDensityUtils.dp2pix(getContext(),1), mWidth-MaDensityUtils.dp2pix(getContext(),1), mHeight-MaDensityUtils.dp2pix(getContext(),1));
        canvas.drawRoundRect(rectF, 25, 25, mRectPaint);
        // 计算每个密码框宽度
        int rectWidth = (mWidth - PWD_SPACING * (PWD_LENGTH - 1)) / PWD_LENGTH;
        // 绘制密码框
        for (int i = 0; i < PWD_LENGTH-1; i++) {
            int left = (rectWidth + PWD_SPACING) * i;
            int top = 2;
            int right = left + rectWidth;
            int bottom = mHeight - top;
            mRectPaint.setStyle(Paint.Style.FILL);
//            if (i == 0 || i == PWD_LENGTH - 1) {
//                RectF rectF = new RectF(left, top, right, bottom);
//                canvas.drawRoundRect(rectF, 25, 25, mRectPaint);
//            }else {
//
//                mRect = new Rect(left, top, right, bottom);
//                canvas.drawRect(mRect, mRectPaint);
//            }
            canvas.drawLine(right,2,right,mHeight-2,mRectPaint);
        }

        // 绘制密码
        for (int i = 0; i < mInputLength; i++) {
            int cx = rectWidth / 2 + (rectWidth + PWD_SPACING) * i;
            int cy = mHeight / 2;
            canvas.drawCircle(cx, cy, PWD_SIZE, mPwdPaint);
        }

    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mInputLength = text.toString().length();
        invalidate();
        if (mInputLength == PWD_LENGTH && mOnInputFinishListener != null) {
            mOnInputFinishListener.onInputFinish(text.toString());
        }
    }

    public interface OnInputFinishListener {
        /**
         * 密码输入结束监听
         *
         * @param password
         */
        void onInputFinish(String password);
    }

    /**
     * 设置输入完成监听
     *
     * @param onInputFinishListener
     */
    public void setOnInputFinishListener(
            OnInputFinishListener onInputFinishListener) {
        this.mOnInputFinishListener = onInputFinishListener;
    }
}