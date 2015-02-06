package com.example.jaymichels.sunshine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jaymichels on 2/6/15.
 */
public class MyView extends View {

    private float mSpeed;
    private float mDegree;
    private WindDirection mDirection;

    private Paint mPaint;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setProperties(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        setProperties(context, attrs);
    }

    @Override
    public void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        int hSpecMode = MeasureSpec.getMode(hMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(hMeasureSpec);
        int myHeight = hSpecSize;

        int wSpecMode = MeasureSpec.getMode(wMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(wMeasureSpec);
        int myWidth = wSpecSize;

        if(hSpecMode == MeasureSpec.EXACTLY) {
            myHeight = hSpecSize;
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            //Wrap content
        }

        if(wSpecMode == MeasureSpec.EXACTLY) {
            myWidth = wSpecSize;
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            //Wrap content
        }

        setMeasuredDimension(myWidth, myHeight);
    }

    private void setProperties(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyView,
                0, 0);

        try {
            mDegree = a.getFloat(R.styleable.MyView_degree, 0.0f);
            mSpeed = a.getFloat(R.styleable.MyView_speed, 5.0f);
            mDirection = WindDirection.valueOf(Utility.calculateDirection(mDegree));
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPaint == null)
            mPaint = new Paint();                   // cache your paint object

        drawArrow(canvas);
    }

    private void drawArrow(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.sunshine_blue));

        //  arrow from x0, y0 to x1, y1
        int x0, y0, x1, y1;

        switch(mDirection) {
            case N:
                x0 = this.getWidth() / 2;
                y0 = this.getHeight();
                x1 = this.getWidth() / 2;
                y1 = 0;
                break;
            case E:
                x0 = 0;
                y0 = this.getHeight() / 2;
                x1 = this.getWidth();
                y1 = this.getHeight() / 2;
                break;
            case W:
                x0 = this.getWidth();
                y0 = this.getHeight() / 2;
                x1 = 0;
                y1 = this.getHeight() / 2;
                break;
            case S:
                x0 = this.getWidth() / 2;
                y0 = 0;
                x1 = this.getWidth() / 2;
                y1 = this.getHeight();
                break;
            case NW:
                x0 = this.getWidth();
                y0 = this.getHeight();
                x1 = 0;
                y1 = 0;
                break;
            case NE:
                x0 = 0;
                y0 = this.getHeight();
                x1 = this.getWidth();
                y1 = 0;
                break;
            case SW:
                x0 = this.getWidth();
                y0 = 0;
                x1 = 0;
                y1 = this.getHeight();
                break;
            case SE:
                x0 = 0;
                y0 = 0;
                x1 = this.getWidth();
                y1 = this.getHeight();
                break;
            default:
                x0 = this.getWidth() / 2;
                y0 = this.getHeight();
                x1 = this.getWidth() / 2;
                y1 = 0;
                break;
        }

        float deltaX = x1 - x0;
        float deltaY = y1 - y0;

        float frac = 0.99f;
        if(mSpeed >= 0 && mSpeed < 10) {
            frac = 0.2f;
        } else if(mSpeed >= 10 && mSpeed < 25) {
            frac = 0.4f;
        } else if(mSpeed >= 25 && mSpeed < 50) {
            frac = 0.6f;
        } else if(mSpeed >= 50 && mSpeed < 100) {
            frac = 0.8f;
        }

        float point_x_1 = x0 + (float) ((1 - frac) * deltaX + frac * deltaY);
        float point_y_1 = y0 + (float) ((1 - frac) * deltaY - frac * deltaX);

        float point_x_2 = x1;
        float point_y_2 = y1;

        float point_x_3 = x0 + (float) ((1 - frac) * deltaX - frac * deltaY);
        float point_y_3 = y0 + (float) ((1 - frac) * deltaY + frac * deltaX);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo(point_x_1, point_y_1);
        path.lineTo(point_x_2, point_y_2);
        path.lineTo(point_x_3, point_y_3);
        path.lineTo(point_x_1, point_y_1);
        path.lineTo(point_x_1, point_y_1);
        path.close();

        canvas.drawPath(path, mPaint);
    }

    public WindDirection getDirection() {
        return mDirection;
    }

    public void setDirection(WindDirection direction) {
        mDirection = direction;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    public enum WindDirection {
        N,
        S,
        E,
        W,
        NW,
        NE,
        SW,
        SE
    }
}
