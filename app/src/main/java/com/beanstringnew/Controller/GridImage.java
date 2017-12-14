package com.beanstringnew.Controller;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Abc on 4/18/2016.
 */
public class GridImage extends ImageView {


    public GridImage(Context context) {
        super(context);
    }
    public GridImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
