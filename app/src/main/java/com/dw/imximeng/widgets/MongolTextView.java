package com.dw.imximeng.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author hjd
 * @create-time 2018-07-04 15:00:46
 */
public class MongolTextView extends TextView {

    private TextPaint textPaint;

    // Constructors
    public MongolTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MongolTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MongolTextView(Context context) {
        super(context);
        init();
    }

    // This class requires the mirrored Mongolian font to be in the assets/fonts folder
    private void init() {

//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
//                "fonts/MongolFontMirrored.ttf");
//        setTypeface(tf);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // swap the height and width
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        // flip and rotate the canvas
        canvas.translate(getWidth(), 0);
        canvas.rotate(90);
        canvas.translate(0, getWidth());
        canvas.scale(1, -1);
        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

        getLayout().draw(canvas);

        canvas.restore();
    }
}