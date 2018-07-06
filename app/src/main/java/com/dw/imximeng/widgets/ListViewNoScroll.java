package com.dw.imximeng.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

public class ListViewNoScroll extends ListView{

    public ListViewNoScroll(Context context, AttributeSet attrs) {
            super(context, attrs) ;
    }
    
    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                            MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

    }

}
