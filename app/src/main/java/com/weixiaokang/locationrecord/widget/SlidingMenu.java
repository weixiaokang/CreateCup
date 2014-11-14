package com.weixiaokang.locationrecord.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.weixiaokang.locationrecord.R;


public class SlidingMenu extends HorizontalScrollView{

    private int contentWidth;
    private int menuWidth;
    private int halfMenuWidth;
    private int rightPadding = 50;
    private boolean flag = false;
    private boolean isFlag = false;
    private ViewGroup menu;
    private ViewGroup content;
    public SlidingMenu(Context context) {
        this(context, null, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        contentWidth = getResources().getDisplayMetrics().widthPixels;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    rightPadding = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!flag) {
            ViewGroup slidingMenu = (ViewGroup) getChildAt(0);
            menu = (ViewGroup) slidingMenu.getChildAt(0);
            content = (ViewGroup) slidingMenu.getChildAt(1);
            menuWidth = contentWidth - rightPadding;
            halfMenuWidth = menuWidth / 2;
            menu.getLayoutParams().width = menuWidth;
            content.getLayoutParams().width = contentWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            smoothScrollTo(menuWidth, 0);
            flag = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > halfMenuWidth) {
                    smoothScrollTo(menuWidth, 0);
                    isFlag = false;
                } else {
                    smoothScrollTo(0, 0);
                    isFlag = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / menuWidth;
        float leftScaleX = 1 - 0.3f * scale;
        float leftScaleY = 1 - 0.3f * scale;
        float rightScaleX = 0.8f + 0.2f * scale;
        float rightScaleY = 0.8f + 0.2f * scale;
        menu.setScaleX(leftScaleX);
        menu.setScaleY(leftScaleY);
        menu.setAlpha( 0.6f + 0.4f * (1 - scale));
        menu.setTranslationX(menuWidth * scale * 0.6f);
        content.setPivotX(0);
        content.setPivotY(content.getHeight() / 2);
        content.setScaleX(rightScaleX);
        content.setScaleY(rightScaleY);
    }

    public void openOrCloseMenu() {
        if (!isFlag) {
            smoothScrollTo(0, 0);
            flag = false;
        } else {
            smoothScrollTo(menuWidth, 0);
            flag = true;
        }
    }
}