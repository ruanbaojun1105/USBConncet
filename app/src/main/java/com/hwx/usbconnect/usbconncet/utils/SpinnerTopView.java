package com.hwx.usbconnect.usbconncet.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.hwx.usbconnect.usbconncet.App;
import com.hwx.usbconnect.usbconncet.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

/**
 * 方向盘控件。
 */
public class SpinnerTopView extends View {
    private static final String TAG = "SteeringWheelView";
    /**
     * 当前方向无效，方向盘没有触摸时处于该状态
     */
    public static final int INVALID = -1;
    /**
     * 向右
     */
    public static final int RIGHT = 0;
    /**
     * 向上
     */
    public static final int UP = 1;
    /**
     * 向左
     */
    public static final int LEFT = 2;
    /**
     * 向下
     */
    public static final int DOWN = 4;
    /**
     * 外部监听器
     */
    private SteeringWheelListener mListener;
    private static final int mDefaultWidthDp = 200;
    private static final int mDefaultHeightDp = 200;
    /**
     * 当采用wrap_content测量模式时，默认宽度
     */
    private static int mDefaultWidth;
    /**
     * 当采用wrap_content测量模式时，默认高度
     */
    private static int mDefaultHeight;
    /**
     * 画笔对象
     */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint3 = new Paint();
    /**
     * 画笔的颜色
     */
    private int mColor;
    /**
     * 当前中心X
     */
    private float mCenterX;
    /**
     * 当前中心Y
     */
    private float mCenterY;
    /**
     * 球
     */
    private Drawable mBallDrawable;
    /**
     * 被按下后，球的图片
     */
    private Drawable mBallPressedDrawable;
    /**
     * 当前球中心X坐标
     */
    private float mBallCenterX;
    /**
     * 当前球中心Y坐标
     */
    private float mBallCenterY;
    /**
     * 球的半径
     */
    private float mBallRadius;
    /**
     * 当前半径
     */
    private float mRadius;
    /**
     * 当前角度
     */
    private double mAngle;
    /**
     * 当前偏离中心的百分比，取值为 0 - 100
     */
    private int mPower;
    /**
     * 通知的时间最小间隔
     */
    private long mNotifyInterval = 0;
    /**
     * 通知者
     */
    private Runnable mNotifyRunnable;
    /**
     * 上次通知监听者的时间
     */
    private long mLastNotifyTime;
    /**
     * 当前方向
     */
    private int mDirection = INVALID;
    /**
     * 背景
     */
    private Drawable mBgDrawable;
    /**
     * 回弹动画
     */
    private ObjectAnimator mAnimator;

    /**
     * 时间插值器
     */
    private TimeInterpolator mInterpolator;
    private boolean mWasTouched;

    /**
     * 获取球X坐标
     *
     * @return 球X坐标
     */
    public float getBallX() {
        return mBallCenterX;
    }

    /**
     * 设置球X坐标。目前该API的执行时机为Choreographer中每帧中的动画阶段,由底层动画框架反射调用
     *
     * @param ballX 球X坐标
     */
    public void setBallX(float ballX) {
        if (ballX != mBallCenterX) {
            mBallCenterX = ballX;
            updatePower();
            updateDirection();
            invalidate();
            notifyStatusChanged();
        }
    }

    /**
     * 获取球Y坐标
     *
     * @return 球Y坐标
     */
    public float getBallY() {
        return mBallCenterY;
    }

    /**
     * 设置球Y坐标。目前该API的执行时机为Choreographer中每帧中的动画阶段,由底层动画框架反射调用
     *
     * @param ballY 球Y坐标
     */
    public void setBallY(float ballY) {
        if (mBallCenterY != ballY) {
            mBallCenterY = ballY;
            updatePower();
            updateDirection();
            invalidate();
            notifyStatusChanged();
        }
    }

    public SpinnerTopView(Context context) {
        super(context);
        init(null, 0);
    }

    public SpinnerTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SpinnerTopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SpinnerTopView);
        //读取XML配置
        mColor = a.getColor(R.styleable.SpinnerTopView_ballColor, Color.TRANSPARENT);
        mBgDrawable = a.getDrawable(R.styleable.SpinnerTopView_arrowBg);
        //mArrowRightDrawable = a.getDrawable(R.styleable.SteeringWheelView_arrowRight);
        //mBallDrawable = a.getDrawable(R.styleable.SteeringWheelView_ballSrc);
        mBallDrawable=new IconDrawable(getContext(), FontAwesomeIcons.fa_dot_circle_o).colorRes(R.color.blue_light).sizeDp(25);
        //mBallPressedDrawable = a.getDrawable(R.styleable.SteeringWheelView_ballPressedSrc);
        mBallPressedDrawable=new IconDrawable(getContext(), FontAwesomeIcons.fa_circle).colorRes(R.color.light_orange).sizeDp(35);
        a.recycle();
        mBallRadius = mBallDrawable.getIntrinsicWidth() >> 1;
        mDefaultWidth = App.dip2px(mDefaultWidthDp);
        mDefaultHeight = App.dip2px(mDefaultHeightDp);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
        //handle wrap_content
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        //解析上层ViewGroup传下来的数据，高两位是模式，低30位是大小
        //主要需要特殊处理wrap_content情形
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, mDefaultHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mDefaultHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: w=" + w + "#h=" + h + "#oldw=" + oldw + "#oldh=" + oldh);
        //在layout过程中会回调该方法
        //handle padding
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        mRadius = (Math.min(width, height) >> 1);
        mBallCenterX = mCenterX = paddingLeft + (width >> 1);
        mBallCenterY = mCenterY = paddingTop + (height >> 1);

    }


    byte[] Picture1_ByteT=new byte[240];

    public byte[] getPicture1_ByteT() {
        return Picture1_ByteT;
    }

    public void setPicture1_ByteT(byte[] picture1_ByteT) {
        Picture1_ByteT = picture1_ByteT;
        invalidate();
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 360f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(5000);
        //anim.setRepeatCount(-1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                Log.d("TAG", "cuurent value is " + currentValue);
                setRotation(currentValue);
            }

        });
        anim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画横线
        canvas.drawLine(mCenterX - mRadius, mCenterY, mCenterX + mRadius, mCenterY, mPaint);
        //画竖线
        canvas.drawLine(mCenterX, mCenterY - mRadius, mCenterX, mCenterY + mRadius, mPaint);
        //画大圆
        float sR=(mRadius/2);
        double bbb=Math.sqrt(Math.pow(sR,2)+Math.pow(sR,2));
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        canvas.drawCircle(mCenterX, mCenterY, sR/3, mPaint);//中心小圆
        mPaint3.setColor(Color.BLUE);
        mPaint3.setAntiAlias(true);
        canvas.drawCircle(mCenterX, (mCenterY), sR*3/5, mPaint3);//上小圆
        canvas.drawCircle(mCenterX, (float) (mCenterY-bbb), sR*3/5, mPaint3);//上小圆
        canvas.drawCircle(mCenterX-sR, mCenterY+sR, sR*3/5, mPaint3);//左下小圆
        canvas.drawCircle(mCenterX+sR, mCenterY+sR, sR*3/5, mPaint3);//右下小圆
        //drawBigBall(canvas);
        //画球
        drawBall(canvas);
        //换图区域，宽高自己设定，要圆要扁请随意
        //RectF rectF = new RectF(0, 0, 1000, 500);
        //这个功能的重要点 其实是先画一个轨迹 然后再把文字画到这个轨迹上 所以 想要什么样的扭曲，就画什么样的轨迹，下面就是画一个轨迹
        Path path=new Path();
        //画一个弧形 也可以是椭圆  上面的矩形就是这个图形的区域大小了 后面两个参数记住 一个是起始位置 一个是所画角度而不是终点位置
        //path.addArc(rectF,-160,180);
        path.addCircle(mCenterX, mCenterY, (float) bbb,Path.Direction.CW);
        mPaint2.setColor(Color.WHITE);
        //去锯齿的 不多说
        mPaint2.setAntiAlias(true);
        int small= (int) ((int) bbb-(sR*3/5));
        mPaint2.setTextSize(mRadius-small);

        // 画布下移
        //canvas.translate(0, 160);
        // 绘制路径
        mPaint2.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, mPaint2);

        // 下面就是绘制文本了
        mPaint2.setStyle(Paint.Style.FILL);
        //把传进来的string转成char【】类型 下面要用到
        //char[] chars= text.toCharArray();
        canvas.drawTextOnPath(text,path,0,small/2,mPaint2);
        //canvas.rotate(180f);
        //canvas.save();
        /*for(int i=0;i<chars.length;i++){
            //这里就是让文字由小到大了，里面的参数嘛 自己看着整 发挥创造性了
            mPaint2.setTextSize(60);
            char[] chars1=new char[1];
            chars1[0]=chars[i];
            //下面就是一个字符一个字符的往后画了 根据自己的需求调整宽度 尺寸
            //这里说明一下参数，免得大家自己又要去看去猜去摸索 如果单纯的不需要文字逐渐变大或者啥的 只需要扭曲
            //那就直接用canvas.drawTextOnPath（）；把string path 往里面传就好了
            //然后下面的0 的位置是X的偏移 1是写Y轴的偏移 这个就可以让那个path是在文字下面 或者穿过中间 或者在下面了
            //差不多了 我很懒 只给个思路 毕竟大家拿过去都要自己改改的
            canvas.drawTextOnPath(chars1,0,1, path, i, 20+i, mPaint2);
            canvas.drawTextOnPath();

        }*/
        float x = 0;
        float y = 0;
        int Age;

        for(Age=0;Age<360;Age++)
        {
            //int small= (int) ((int) bbb-(sR*3/5));
            int ret= (int) ((mRadius-small)/16);
            int big=(int)mRadius;
            for (int R = big; R > small; R = R - ret)
            {
                x = mBallCenterX- (float)(R * Math.cos( (Age/180.0)*3.1415926));
                y = mBallCenterY- (float)(R * Math.sin((Age / 180.0) * 3.1415926));
                mPaint3.setColor(Color.GRAY);
                canvas.drawCircle(x,y,3,mPaint3);
                int color = 0;
                color =( (0x0001 << ((R - small) /ret)) & (Picture1_ByteT[(Age / 3) * 2] * 256 + Picture1_ByteT[(Age / 3) * 2 + 1]) );
                //color = 1;

                if (color > 0)
                {
                    mPaint3.setColor(Color.RED);
                    canvas.drawCircle(x,y,5,mPaint3);
                }
                else
                {
                }
            }
        }

    }

    String text="test你好min明天dfaaaaaaaaaaaaaaa";

    public void setText(String text)
    {
        this.text=text;
        invalidate();
    }

    private void drawBall(Canvas canvas) {
        Drawable drawable;
        //point to the right drawable instance
        if (mWasTouched) {
            drawable = mBallPressedDrawable;
        } else {
            drawable = mBallDrawable;
        }
        drawable.setBounds((int) (mBallCenterX - (mWasTouched?drawable.getIntrinsicWidth():(drawable.getIntrinsicWidth()/1.5))),
                (int) (mBallCenterY - (mWasTouched?drawable.getIntrinsicHeight():(drawable.getIntrinsicHeight()/1.5))),
                (int) (mBallCenterX + (mWasTouched?drawable.getIntrinsicWidth():(drawable.getIntrinsicWidth()/1.5))),
                (int) (mBallCenterY + (mWasTouched?drawable.getIntrinsicHeight():(drawable.getIntrinsicHeight()/1.5))));
        drawable.draw(canvas);
    }
    private void drawBigBall(Canvas canvas) {
        //Drawable drawable=getResources().getDrawable(R.drawable.rocker_base);
        //point to the right drawable instance
        mBgDrawable.setBounds((int) (mCenterX - mRadius),
                (int) (mCenterY - mRadius),
                (int) (mCenterX + mRadius),
                (int) (mCenterY + mRadius));
        mBgDrawable.draw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int power=checkTouch(x,y);
                mWasTouched = power<25?true:false;
                if (!mWasTouched)
                    return false;
                if (mAnimator != null && mAnimator.isRunning()) {
                    //在本次触摸事件序列中，如果上一个复位动画还没执行完毕，则需要取消动画，及时响应用户输入
                    mAnimator.cancel();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!mWasTouched)
                    return false;
                //updateBallData(x, y);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mWasTouched = false;
                resetBall();
                break;
            }
            default:
                break;
        }

        notifyStatusChanged();
        return false;
    }

    private int checkTouch(int x,int y) {
        //x+=mArrowRightDrawable.getIntrinsicWidth();
        //y+=mArrowRightDrawable.getIntrinsicWidth();
        int mPower = (int) (100 * Math.sqrt(Math.pow(x - mCenterX, 2) + Math.pow(y - mCenterY, 2)) / (mRadius - mBallRadius));
        Log.e("当前的距离：",mPower+"");
        return mPower;
    }

    /**
     * 指定球回弹动画时间插值器
     *
     * @param value 插值器
     */
    public SpinnerTopView interpolator(TimeInterpolator value) {
        if (value != null) {
            mInterpolator = value;
        } else {
            mInterpolator = new OvershootInterpolator();
        }
        return this;
    }

    private TimeInterpolator getInterpolator() {
        if (mInterpolator == null) {
            mInterpolator = new OvershootInterpolator();
        }
        return mInterpolator;
    }

    /**
     * 弹性滑动
     */
    private void resetBall() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("BallX", mBallCenterX, mCenterX);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("BallY", mBallCenterY, mCenterY);
        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY).setDuration(150);
        mAnimator.setInterpolator(getInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAngle = 0;
                mPower = 0;
                mDirection = INVALID;
                notifyStatusChanged();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    private void updateBallData(int x, int y) {
        mBallCenterX = x;
        mBallCenterY = y;
        boolean outOfRange = outOfRange(x, y);
        //采用(a, b]开闭区间
        if (x >= mCenterX && y < mCenterY) {
            //第一象限
            mAngle = Math.toDegrees(Math.atan((mCenterY - y) / (x - mCenterX)));
            if (outOfRange) {
                mBallCenterX = (float) (mCenterX + Math.cos(Math.toRadians(mAngle)) * (mRadius - mBallRadius));
                mBallCenterY = (float) (mCenterY - Math.sin(Math.toRadians(mAngle)) * (mRadius - mBallRadius));
            }
        } else if (x < mCenterX && y <= mCenterY) {
            //第二象限
            mAngle = 180 - Math.toDegrees(Math.atan((mCenterY - y) / (mCenterX - x)));
            if (outOfRange) {
                mBallCenterX = (float) (mCenterX - Math.cos(Math.toRadians(180 - mAngle)) * (mRadius - mBallRadius));
                mBallCenterY = (float) (mCenterY - Math.sin(Math.toRadians(180 - mAngle)) * (mRadius - mBallRadius));
            }
        } else if (x <= mCenterX && y > mCenterY) {
            //第三象限
            mAngle = 270 - Math.toDegrees(Math.atan((mCenterX - x) / (y - mCenterY)));
            if (outOfRange) {
                mBallCenterX = (float) (mCenterX - Math.cos(Math.toRadians(mAngle - 180)) * (mRadius - mBallRadius));
                mBallCenterY = (float) (mCenterY + Math.sin(Math.toRadians(mAngle - 180)) * (mRadius - mBallRadius));
            }
        } else if (x > mCenterX && y >= mCenterY) {
            //第四象限
            mAngle = 360 - Math.toDegrees(Math.atan((y - mCenterY) / (x - mCenterX)));
            if (outOfRange) {
                mBallCenterX = (float) (mCenterX + Math.cos(Math.toRadians(360 - mAngle)) * (mRadius - mBallRadius));
                mBallCenterY = (float) (mCenterY + Math.sin(Math.toRadians(360 - mAngle)) * (mRadius - mBallRadius));
            }
        }
        updatePower();
        updateDirection();
        invalidate();
    }

    private void updatePower() {
        mPower = (int) (100 * Math.sqrt(Math.pow(mBallCenterX - mCenterX, 2) + Math.pow(mBallCenterY - mCenterY, 2)) / (mRadius - mBallRadius));
        Log.d(TAG, "updatePower: mPower = " + mPower);
    }

    private boolean outOfRange(int newX, int newY) {
        return (Math.pow(newX - mCenterX, 2) + Math.pow(newY - mCenterY, 2)) > Math.pow(mRadius - mBallRadius, 2);
    }

    /**
     * 采用(a,b]开闭区间
     *
     * @return 方向值
     */
    private int updateDirection() {
        if (mPower<30)//防止力度太小时急速转变方向，同时设备转换方向的时候也需要短暂转向的处理，不然直接摔死
            return mDirection;
        if (Math.abs(mCenterX - mBallCenterX) < 0.00000001
                && Math.abs(mCenterY - mBallCenterY) < 0.00000001)
            mDirection = INVALID;
        else if (mAngle <= 45 || mAngle > 315)
            mDirection = RIGHT;
        else if (mAngle > 45 && mAngle <= 135)
            mDirection = UP;
        else if (mAngle > 135 && mAngle <= 225)
            mDirection = LEFT;
        else
            mDirection = DOWN;
        return mDirection;
    }

    /**
     * 通知监听者方向盘状态改变
     */
    private void notifyStatusChanged() {
        if (mListener == null)
            return;

        long delay = 0;
        if (mNotifyRunnable == null) {
            mNotifyRunnable = createNotifyRunnable();
        } else {
            long now = System.currentTimeMillis();
            if (now - mLastNotifyTime < mNotifyInterval) {
                //移除旧消息
                removeCallbacks(mNotifyRunnable);
                delay = mNotifyInterval - (now - mLastNotifyTime);
            }
        }

        postDelayed(mNotifyRunnable, delay);
    }

    private Runnable createNotifyRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                mLastNotifyTime = System.currentTimeMillis();
                //取当前数据，而非过去数据的snapshot
                mListener.onStatusChanged(SpinnerTopView.this, (int) mAngle, mPower, mDirection);
            }
        };
    }

    /**
     * 设置回调时间间隔
     *
     * @param interval 回调时间间隔
     */
    public SpinnerTopView notifyInterval(long interval) {
        if (interval < 0) {
            throw new RuntimeException("notifyInterval interval < 0 is not accept");
        }

        mNotifyInterval = interval;
        return this;
    }

    /**
     * 设置监听器
     *
     * @param listener 监听器对象
     */
    public SpinnerTopView listener(SteeringWheelListener listener) {
        mListener = listener;
        return this;
    }

    public interface SteeringWheelListener {
        /**
         * 方向盘状态改变的回调
         *
         * @param view      方向盘实例对象
         * @param angle     当前角度。范围0-360，其中右0，上90，左180，下270
         * @param power     方向上的力度。范围0-100
         * @param direction 大致方向。取值为 {@link #RIGHT} {@link #UP} {@link #LEFT} {@link #DOWN}
         */
        void onStatusChanged(SpinnerTopView view, int angle, int power, int direction);
    }
}
