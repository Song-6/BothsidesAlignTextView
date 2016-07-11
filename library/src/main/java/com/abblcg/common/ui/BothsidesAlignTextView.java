package com.abblcg.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.abblcg.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abb on 16/7/8.
 */
public class BothsidesAlignTextView extends View {

    private String abbText;
    private int abbTextColor;
    private int abbTextSize;

    private List<Rect> mBounds;
    private Paint mPaint;

    private int size;

    public BothsidesAlignTextView(Context context) {
        this(context, null);
    }

    public BothsidesAlignTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BothsidesAlignTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);

            if (attr == R.styleable.CustomTitleView_abbText) {
                abbText = a.getString(attr);
            } else if (attr == R.styleable.CustomTitleView_abbTextColor) {
                abbTextColor = a.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.CustomTitleView_abbTextSize) {
                abbTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
            }
        }
        a.recycle();

        size = abbText.length();

        if (size < 2) {
            throw new RuntimeException("文本长度必须大于1");
        }

        Paint tempPaint = new Paint();
        tempPaint.setColor(abbTextColor);
        tempPaint.setTextSize(abbTextSize);

        mBounds = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Rect mBound = new Rect();
            tempPaint.getTextBounds(abbText, i, i + 1, mBound);
            mBounds.add(mBound);
        }
        mPaint = new Paint();
        mPaint.setColor(abbTextColor);
        mPaint.setTextSize(abbTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("测试", "getWidth:" + getWidth());
        Log.i("测试", "getLeft:" + getLeft());
        Log.i("测试", "getHeight:" + getHeight());
        Log.i("测试", "getTop:" + getTop());
        Log.i("测试", "getPaddingLeft:" + getPaddingLeft());
        Log.i("测试", "getPaddingRight:" + getPaddingRight());

        Log.i("测试", "--------------------------");
        int size = abbText.length();
        int interval = (getWidth() - getPaddingLeft() - getPaddingRight() - mBounds.get(0).width() / 2 - mBounds.get(size - 1).width() / 2) / (size - 1);
        Log.i("测试", "interval:" + interval);
        for (int i = 0; i < size; i++) {
            Rect mBound = mBounds.get(i);
            int seftPadTop = getHeight() / 2 - mBound.top / 2;
            int leftPosition;
            if (i == 0) {
                leftPosition = getPaddingLeft();
            } else if (i == abbText.length() - 1) {
                leftPosition = getWidth() - getPaddingRight() - mBound.width();
            } else {
                leftPosition = mBounds.get(0).width() / 2 + interval * i + getPaddingLeft() - mBound.width() / 2;
            }
            canvas.drawText(abbText.charAt(i) + "", leftPosition, seftPadTop, mPaint);
            Log.i("测试", "mBound.height:" + mBound.height() + "\t" + "mBound.width:" + mBound.width() + "\t" + leftPosition + "\t" + seftPadTop);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(abbTextSize);
            float textWidth = getTotalWidth();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(abbTextSize);
            float textHeight = getMaxHeight();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

    private int getTotalWidth() {
        int total = 0;
        for (Rect mBound : mBounds) {
            total += mBound.width();
        }
        return total;
    }

    private int getMaxHeight() {
        int max = 0;
        for (Rect mBound : mBounds) {
            if (mBound.height() > max) {
                max = mBound.height();
            }
        }
        return max;
    }
}
