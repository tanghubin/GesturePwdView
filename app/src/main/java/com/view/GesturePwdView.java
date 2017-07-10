package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by thbpc on 2017/7/5 0005.
 */

public class GesturePwdView extends View implements View.OnTouchListener {

    public GesturePwdView(Context context) {
        this(context, null);
    }

    public GesturePwdView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GesturePwdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint mBgPaint;
    private Paint mLinePaint;
    private int mWidth;
    private int mHeight;
    private int mPosition = 0;
    private int mPointRadius = 15;


    float mStartX;
    float mStartY;
    float mEndX;
    float mEndY;

    private CallBack mCallBack;

    private boolean isFirst = true;
    private boolean isError = false;

    private ArrayList<String> pointList = new ArrayList<>();
    private ArrayList<Integer> positionList1 = new ArrayList<>();
    private ArrayList<Integer> positionList2 = new ArrayList<>();


    private int mPointColor = Color.RED;
    private int mLineColor = Color.GREEN;
    private int mErrorLineColor = Color.RED;
    private float mLineStrokeWidth =0f;

    private void init() {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);                       //设置画笔为无锯齿
        mBgPaint.setColor(mPointColor);                    //设置画笔颜色
        mBgPaint.setStyle(Paint.Style.FILL);//填充

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);

        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(15);
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLinePaint.setStrokeWidth(mLineStrokeWidth);
        canvas.drawCircle(mPointRadius, mPointRadius, mPointRadius, mBgPaint);//圆心坐标
        canvas.drawCircle(mWidth / 2, 0 + mPointRadius, mPointRadius, mBgPaint);
        canvas.drawCircle(mWidth - mPointRadius, mPointRadius, mPointRadius, mBgPaint);

        canvas.drawCircle(0 + mPointRadius, mHeight / 2, mPointRadius, mBgPaint);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mPointRadius, mBgPaint);
        canvas.drawCircle(mWidth - mPointRadius, mHeight / 2, mPointRadius, mBgPaint);

        canvas.drawCircle(0 + mPointRadius, mHeight - mPointRadius, mPointRadius, mBgPaint);
        canvas.drawCircle(mWidth / 2, mHeight - mPointRadius, mPointRadius, mBgPaint);
        canvas.drawCircle(mWidth - mPointRadius, mHeight - mPointRadius, mPointRadius, mBgPaint);

        switch (mPosition) {
            case 1:
                drawGradually(canvas, 0 + mPointRadius, 0 + mPointRadius, mBgPaint);
                break;
            case 2:
                drawGradually(canvas, mWidth / 2, 0 + mPointRadius, mBgPaint);
                break;
            case 3:
                drawGradually(canvas, mWidth - mPointRadius, 0 + mPointRadius, mBgPaint);
                break;
            case 4:
                drawGradually(canvas, 0 + mPointRadius, mHeight / 2, mBgPaint);
                break;
            case 5:
                drawGradually(canvas, mWidth / 2, mHeight / 2, mBgPaint);
                break;
            case 6:
                drawGradually(canvas, mWidth - mPointRadius, mHeight / 2, mBgPaint);
                break;
            case 7:
                drawGradually(canvas, 0 + mPointRadius, mHeight - mPointRadius, mBgPaint);
                break;
            case 8:
                drawGradually(canvas, mWidth / 2, mHeight - mPointRadius, mBgPaint);
                break;
            case 9:
                drawGradually(canvas, mWidth - mPointRadius, mHeight - mPointRadius, mBgPaint);
                break;
        }

        mPosition = 0;
        if (isError) {
            mLinePaint.setColor(mErrorLineColor);
        } else {
            mLinePaint.setColor(mLineColor);
        }
        for (int i = 0; i < pointList.size(); i++) {
            if (i < pointList.size() - 1 && pointList.size() > 1) {
                String[] split1 = pointList.get(i).split(",");
                String[] split2 = pointList.get(i + 1).split(",");
                canvas.drawLine(Float.parseFloat(split1[0]), Float.parseFloat(split1[1]),
                        Float.parseFloat(split2[0]), Float.parseFloat(split2[1]), mLinePaint);
            } else {
                String[] split = pointList.get(i).split(",");
                canvas.drawLine(Float.parseFloat(split[0]), Float.parseFloat(split[1]), mEndX, mEndY, mLinePaint);
            }
        }
    }

    private void drawGradually(final Canvas canvas, final int cx, final int cy, final Paint bgPaint) {
        canvas.drawCircle(cx, cy, mPointRadius + 5, bgPaint);
        canvas.drawCircle(cx, cy, mPointRadius + 10, bgPaint);
        canvas.drawCircle(cx, cy, mPointRadius + 15, bgPaint);
        canvas.drawCircle(cx, cy, mPointRadius + 10, bgPaint);
        canvas.drawCircle(cx, cy, mPointRadius + 5, bgPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                float[] test = test(x, y);
                if (test != null) {
                    mStartX = test[0];
                    mStartY = test[1];
                    String s = test[0] + "," + test[1];
                    if (!pointList.contains(s)) {
                        pointList.add(test[0] + "," + test[1]);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                float[] moveTest = test(moveX, moveY);
                if (moveTest != null) {
                    drawLine(moveTest[0], moveTest[1]);
                } else {
                    mEndX = moveX;
                    mEndY = moveY;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mPosition = 0;
                if (pointList.size() < 4) {
                    pointList.clear();
                    if (isFirst) {
                        positionList1.clear();
                    } else {
                        positionList2.clear();
                    }
                } else {
                    String s = pointList.get(pointList.size() - 1);
                    String[] split = s.split(",");
                    mEndX = Float.parseFloat(split[0]);
                    mEndY = Float.parseFloat(split[1]);
                    setEnabled(false);
                    if (mCallBack != null) {
                        if (isFirst) {
                            mCallBack.onComplete1(positionList1);
                        } else {
                            mCallBack.onComplete2(positionList2);
                        }
                    }
                }
                invalidate();
                break;
        }
        return true;
    }


    private void drawLine(float x, float y) {
        if (pointList.size() == 0) {//当集合为0时，第一个传来的坐标为起始点
            mStartX = x;
            mStartY = y;
        }
        String s = x + "," + y;
        if (!pointList.contains(s)) {//不包含再添加，否则重复
            pointList.add(x + "," + y);
            mEndX = x;
            mEndY = y;

            mStartX = x;//设置为下次绘制起始点
            mStartY = y;
        }
        invalidate();
    }

    private void addPointPosition(int position) {
        if (isFirst) {
            if (!positionList1.contains(position)) {
                mPosition = position;
                positionList1.add(position);
                VibratorUtil.Vibrate(getContext(), 40);   //震动
            }
        } else {
            if (!positionList2.contains(position)) {
                mPosition = position;
                positionList2.add(position);
                VibratorUtil.Vibrate(getContext(), 40);   //震动
            }
        }
    }

    private float[] test(float x, float y) {
        //判断第一排
        if (x < 0 + mPointRadius + 50 && x > 0 + mPointRadius - 50 &&
                y < 0 + mPointRadius + 50 && y + mPointRadius > 0 - 50) {
            addPointPosition(1);
            return new float[]{0 + mPointRadius, 0 + mPointRadius};
        } else if (x < mWidth / 2 + 50 && x > mWidth / 2 - 50 &&
                y < 0 + mPointRadius + 50 && y > 0 + mPointRadius - 50) {
            addPointPosition(2);
            return new float[]{mWidth / 2, 0 + mPointRadius};
        } else if (x < mWidth - mPointRadius + 50 && x > mWidth - mPointRadius - 50 &&
                y < 0 + mPointRadius + 50 && y > 0 + mPointRadius - 50) {
            addPointPosition(3);
            return new float[]{mWidth - mPointRadius, 0 + mPointRadius};
            //判断第二排
        } else if (x < 0 + mPointRadius + 50 && x > 0 + mPointRadius - 50 &&
                y < mHeight / 2 + 50 && y > mHeight / 2 - 50) {
            addPointPosition(4);
            return new float[]{0 + mPointRadius, mHeight / 2};
        } else if (x < mWidth / 2 + 50 && x > mWidth / 2 - 50 &&
                y < mHeight / 2 + 50 && y > mHeight / 2 - 50) {
            addPointPosition(5);
            return new float[]{mWidth / 2, mHeight / 2};
        } else if (x < mWidth - mPointRadius + 50 && x > mWidth - mPointRadius - 50 &&
                y < mHeight / 2 + 50 && y > mHeight / 2 - 50) {
            addPointPosition(6);
            return new float[]{mWidth - mPointRadius, mHeight / 2};
            //判断第三排
        } else if (x < 0 + mPointRadius + 50 && x > 0 + mPointRadius - 50 &&
                y < mHeight - mPointRadius + 50 && y > mHeight - mPointRadius - 50) {
            addPointPosition(7);
            return new float[]{0 + mPointRadius, mHeight - mPointRadius};
        } else if (x < mWidth / 2 + 50 && x > mWidth / 2 - 50 &&
                y < mHeight - mPointRadius + 50 && y > mHeight - mPointRadius - 50) {
            addPointPosition(8);
            return new float[]{mWidth / 2, mHeight - mPointRadius};
        } else if (x < mWidth - mPointRadius + 50 && x > mWidth - mPointRadius - 50 &&
                y < mHeight - mPointRadius + 50 && y > mHeight - mPointRadius - 50) {
            addPointPosition(9);
            return new float[]{mWidth - mPointRadius, mHeight - mPointRadius};
        }
        return null;
    }

    /**
     * 重置状态
     */
    public boolean reSet() {
        pointList.clear();//清除索引顺序集合
        if (isFirst) {
            positionList1.clear();//清除轨迹
        } else {
            positionList2.clear();//清除轨迹
        }
        isError = false;//错误置为false
        invalidate();
        setEnabled(true);//可交互
        return isFirst;
    }

    /**
     * 继续
     */
    public void goOn() {
        pointList.clear();//清除索引顺序集合
        invalidate();
        setEnabled(true);//可交互
        isFirst = false;//设置第二次
    }

    public void setError() {
        isError = true;//错误，绘制红色线
        invalidate();//绘制
    }

    /**
     * 设置点的颜色
     *
     * @param color
     */
    public void setPointColor(int color) {
        this.mPointColor = color;
    }

    /**
     * 设置点半径
     *
     * @param radius
     */
    public void setPointRadius(int radius) {
        this.mPointRadius = radius;
    }

    public void setLineColor(int color) {
        this.mLineColor = color;
    }

    public void setLineStrokeWidth(float width) {
        this.mLineStrokeWidth = width;
    }

    public interface CallBack {
        void onComplete1(ArrayList<Integer> postionList);

        void onComplete2(ArrayList<Integer> postionList);
    }


    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }
}


