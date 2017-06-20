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
import android.text.TextUtils;
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
 * 陀螺显示控件。
 */
public class SpinnerTopView extends View {
    private static final String TAG = "SpinnertopView";
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
    private Paint mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint3 = new Paint();
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
     * 背景
     */
    private IconDrawable mBgDrawable;
    private String text;//显示文本
    private boolean isVisiBgDot=false;
    private boolean isVisiImage=false;
    private boolean isVisiText=false;
    private boolean isVisiBg=true;
    private ObjectAnimator animator=null;
    private byte[] Picture1_ByteT=new byte[240];
    private int lastColor;
    private int prerent=1;//显示百分比精细度，越大越不清晰

    public void setPrerent(int prerent) {
        this.prerent = prerent;
    }

    public void setVisiBg(boolean visiBg) {
        isVisiBg = visiBg;
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
        //mBgDrawable = a.getDrawable(R.styleable.SpinnerTopView_arrowBg);//fa-futbol-o
        mBgDrawable=new IconDrawable(getContext(), FontAwesomeIcons.fa_futbol_o).colorRes(R.color.link_text_blue);//.sizeDp(25);
        mBallDrawable=new IconDrawable(getContext(), FontAwesomeIcons.fa_dot_circle_o).colorRes(R.color.blue_light).sizeDp(25);
        a.recycle();
        mBallRadius = mBallDrawable.getIntrinsicWidth() >> 1;
        mDefaultWidth = App.dip2px(mDefaultWidthDp);
        mDefaultHeight = App.dip2px(mDefaultHeightDp);

        setPadding(10,10,10,10);
        setmBgDrawable(mBgDrawable,false);
        animator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        animator.setInterpolator(new LinearInterpolator());
        setAnim3();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animators) {
                isVisiBgDot=true;
//                invalidate();
//                animator.setDuration(10000);
//                animator.setRepeatCount(-1);
//                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isVisiBgDot=true;
                //invalidate();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        //starZeroAnim();
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

    public void cleanAnim() {
        if (animator==null)
            return;
        else {
            if (!animator.isPaused())
                animator.pause();
        }
    }

    public void starZeroAnim() {
        if (animator==null)
            return;
        animator.start();
    }

    private void starAnim() {
//        animator.setDuration(2500);
//        animator.setRepeatCount(3);
        animator.start();
    }


    public SpinnerTopView setAnim1() {
        animator.setDuration(2000);
        animator.setRepeatCount(3);
        animator.setFloatValues( 0f, 480f);
        animator.setRepeatMode(ValueAnimator.RESTART);
        return this;
    }
    public SpinnerTopView setAnim2() {
        animator.setDuration(5000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        return this;
    }
    public SpinnerTopView setAnim3() {
        animator.setDuration(10000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        return this;
    }
    public void setPicture1_ByteT(byte[] picture1_ByteT) {
        setPicture1_ByteT(picture1_ByteT,true);
    }
    public void setPicture1_ByteT(byte[] picture1_ByteT,boolean starAnim) {
        if (picture1_ByteT==null)
            return;
        Picture1_ByteT = picture1_ByteT;
        isVisiImage=true;
        isVisiBgDot=true;
        isVisiText=false;
        invalidate();
        if (starAnim)
            starAnim();
//        ValueAnimator anim = ValueAnimator.ofFloat(0f, 360f);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setDuration(5000);
//        //anim.setRepeatCount(-1);
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float currentValue = (float) animation.getAnimatedValue();
//                //Log.d("TAG", "cuurent value is " + currentValue);
//                setRotation(currentValue);
//                if (currentValue==360f){
//
//                }
//            }
//
//        });
//        anim.start();
    }
    public void setText(String text){
        if (TextUtils.isEmpty(text))
            return;
        this.text=text;
        isVisiImage=false;
        isVisiText=true;
        isVisiBgDot=true;
        invalidate();
        starAnim();
    }

    public void setLastColor(int lastColor) {
        setLastColor(lastColor,true);
    }
    public void setLastColor(int lastColor,boolean relayout) {
        this.lastColor = lastColor;
        if (relayout)
            invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画横线
//        canvas.drawLine(mCenterX - mRadius, mCenterY, mCenterX + mRadius, mCenterY, mPaint3);
        //画竖线
//        canvas.drawLine(mCenterX, mCenterY - mRadius, mCenterX, mCenterY + mRadius, mPaint3);
        //画大圆
        float sR=(mRadius/2);
        double bbb=Math.sqrt(Math.pow(sR,2)+Math.pow(sR,2));
        int small= (int) ((int) bbb-(sR*3/5));
        int ret= (int) ((mRadius-small)/15);
        int big=(int)mRadius;

        initDraw(canvas, (int) sR, (float) bbb);
        if (isVisiText) {//奇怪
            if (!TextUtils.isEmpty(text)) {
                isVisiImage = false;
                isVisiText = true;
                mPaint2.setColor(lastColor==0?Color.WHITE:lastColor);
                //去锯齿的 不多说
                mPaint2.setAntiAlias(true);
                mPaint2.setTextSize(mRadius - small-50);
                mPaint2.setStyle(Paint.Style.STROKE);
                //mPaint2.setStyle(Paint.Style.FILL);
                Path path = new Path();
                path.addCircle(mCenterX, mCenterY, (float) bbb, Path.Direction.CW);
                //canvas.drawPath(path, mPaint2);
                canvas.drawTextOnPath(text, path, 0, small/3, mPaint2);
            }
        }
        if (false) {//isVisiBgDot
            float x = 0;
            float y = 0;
            int Age;
            for (Age = 0; Age < 360; Age++) {
                //LogUtils.e("angle:"+Age+"--x"+x+"--y"+y);
                for (int R = big; R > small+ret; R -=ret) {
                    x = mBallCenterX - (float) (R * Math.cos((Age / 180.0) * 3.1415926));
                    y = mBallCenterY - (float) (R * Math.sin((Age / 180.0) * 3.1415926));
                    mPaint3.setColor(Color.GRAY);
                    canvas.drawCircle(x, y, 1, mPaint3);
//                    if ((Age+1)%120==0&&((big-small)/ret/2)==R) {
//                        canvas.drawCircle(x, y, sR*3/5, mPaint3);//中心小圆
//                    }
                }
            }
        }
        //drawText(canvas,big, (float) bbb);
        drawImage(canvas,big,small,ret);
    }

    private void initDraw(Canvas canvas,int sR,float bbb) {
        mPaint3.setColor(Color.BLUE);
        mPaint3.setAntiAlias(true);
//        canvas.drawCircle(mCenterX, (mCenterY), sR/3, mPaint3);//中心小圆
//        canvas.drawCircle(mCenterX, (float) (mCenterY-bbb),sR*3/5, mPaint3);//上小圆
//        canvas.drawCircle(mCenterX-sR, mCenterY+sR, sR*3/5, mPaint3);//左下小圆
//        canvas.drawCircle(mCenterX+sR, mCenterY+sR, sR*3/5, mPaint3);//右下小圆
        //画背景
        drawBgBall(canvas);
        //画球
        drawBall(canvas);
    }

    private void drawText(Canvas canvas,int small,float bbb) {
        if (!isVisiText)
            return;
        if (!TextUtils.isEmpty(text)) {
            isVisiImage=false;
            isVisiText=true;
            mPaint2.setColor(lastColor==0?Color.WHITE:lastColor);
            //去锯齿的 不多说
            mPaint2.setAntiAlias(true);
            mPaint2.setTextSize(mRadius - small);
            mPaint2.setStyle(Paint.Style.STROKE);
            //mPaint2.setStyle(Paint.Style.FILL);
            Path path = new Path();
            path.addCircle(mCenterX, mCenterY, bbb, Path.Direction.CW);
            //canvas.drawPath(path, mPaint2);
            canvas.drawTextOnPath(text, path, 0, small / 2, mPaint2);
        }

        // 下面就是绘制文本了
        //char[] chars= text.toCharArray();
        //canvas.rotate(180f);
        //canvas.save();
//        for(int i=0;i<chars.length;i++){
//            //这里就是让文字由小到大了，里面的参数嘛 自己看着整 发挥创造性了
//            mPaint2.setTextSize(60);
//            char[] chars1=new char[1];
//            chars1[0]=chars[i];
//            //下面就是一个字符一个字符的往后画了 根据自己的需求调整宽度 尺寸
//            //这里说明一下参数，免得大家自己又要去看去猜去摸索 如果单纯的不需要文字逐渐变大或者啥的 只需要扭曲
//            //那就直接用canvas.drawTextOnPath（）；把string path 往里面传就好了
//            //然后下面的0 的位置是X的偏移 1是写Y轴的偏移 这个就可以让那个path是在文字下面 或者穿过中间 或者在下面了
//            //差不多了 我很懒 只给个思路 毕竟大家拿过去都要自己改改的
//            canvas.drawTextOnPath(chars1,0,1, path, i, 20+i, mPaint2);
//            canvas.drawTextOnPath();
//
//        }
    }

    private void drawImage(Canvas canvas,int big,int small,int ret) {
        if (!isVisiImage)
            return;
        isVisiImage=true;
        isVisiText=false;
        float x = 0;
        float y = 0;
        int Age;
        mPaint3.setColor(lastColor==0?Color.CYAN:lastColor);
        for(Age=0;Age<360;Age++)
        {
            for (int R = big; R > small; R = R - ret)
            {
                x = mBallCenterX- (float)(R * Math.cos( (Age/180.0)*3.1415926));
                y = mBallCenterY- (float)(R * Math.sin((Age / 180.0) * 3.1415926));
//                if (isVisiBgDot)
//                    canvas.drawCircle(x,y,3,mPaint3);
                int color = 0;
                color =( (0x0001 << ((R - small) /ret)) & (Picture1_ByteT[(Age / 3) * 2] * 256 + Picture1_ByteT[(Age / 3) * 2 + 1]) );
                if (color > 0){//少画几个点总是好的
                    if (prerent==1)
                        canvas.drawCircle(x,y,3,mPaint3);
                    else if (Age%prerent==0)
                        canvas.drawCircle(x,y,5,mPaint3);
                }
            }
        }
    }

    private void drawBall(Canvas canvas) {
        //point to the right drawable instance
        Drawable drawable = mBallDrawable;
        drawable.setBounds((int) (mBallCenterX - drawable.getIntrinsicWidth()/1.5f),
                (int) (mBallCenterY - drawable.getIntrinsicWidth()/1.5f),
                (int) (mBallCenterX + drawable.getIntrinsicWidth()/1.5f),
                (int) (mBallCenterY + drawable.getIntrinsicWidth()/1.5f));
        drawable.draw(canvas);
    }
    private void drawBgBall(Canvas canvas) {
        //Drawable drawable=getResources().getDrawable(R.drawable.rocker_base);
        //point to the right drawable instance
        if (!isVisiBg)
            return;
        if (mBgDrawable==null)
            return;
        mBgDrawable.setBounds((int) (mCenterX - mRadius),
                (int) (mCenterY - mRadius),
                (int) (mCenterX + mRadius),
                (int) (mCenterY + mRadius));
        mBgDrawable.draw(canvas);
    }

    public void setmBgDrawable(IconDrawable mBgDrawable,boolean relayout) {
        this.mBgDrawable = mBgDrawable.sizePx((int) mBallRadius);
        if (relayout)
            invalidate();
    }
}
