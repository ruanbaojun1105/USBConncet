package com.hwx.usbconnect.usbconncet.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.hwx.usbconnect.usbconncet.App;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.fragment.MainFragment;
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
    private Paint mPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
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
    private IconDrawable mBallDrawable;
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
    private boolean isFixed=false;//混色
    private AnimatorSet animSet=null;
    private ObjectAnimator rotate=null;
    private ObjectAnimator fadeInOut=null;
    private byte[] Picture1_ByteT=null;
    private int lastColor;
    private int prerent=1;//显示百分比精细度，越大越不清晰

    public void setPrerent(int prerent) {
        this.prerent = prerent;
    }

    public SpinnerTopView setVisiBg(boolean visiBg) {
        isVisiBg = visiBg;
        return this;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
        invalidate();
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
        mBgDrawable=new IconDrawable(getContext(), FontAwesomeIcons.fa_life_saver).colorRes(R.color.blue_t_alph);//.sizeDp(25);//fa-life-saver,fa_futbol_o
        //mBgDrawable=getResources().getDrawable(R.drawable.fan_bg,getContext().getTheme());
        mBallDrawable=new IconDrawable(getContext(), FontAwesomeIcons.fa_dot_circle_o).colorRes(R.color.blue_light).sizeDp(35);
        a.recycle();
        mBallRadius = mBallDrawable.getIntrinsicWidth() >> 1;
        mDefaultWidth = App.dip2px(mDefaultWidthDp);
        mDefaultHeight = App.dip2px(mDefaultHeightDp);

        setPadding(10,10,10,10);
        setmBgDrawable(mBgDrawable,false);
        animSet = new AnimatorSet();
        animSet.setInterpolator(new LinearInterpolator());
        rotate = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        fadeInOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0.2f, 1f);
        setAnim1();
        //animSet.setDuration(5000);
        //ObjectAnimator animator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animators) {
//                isVisiBgDot=true;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//                isVisiBgDot=true;
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//            }
//        });
        //starZeroAnim();
        /*ValueAnimator anim = ValueAnimator.ofInt(0, 239);
        anim.setDuration(5000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int a= (int) valueAnimator.getAnimatedValue();
            }
        });
        anim.start();*/
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
        if (animSet==null)
            return;
        else {
            if (!animSet.isPaused())
                animSet.pause();
        }
    }

    public void starZeroAnim() {
        if (animSet==null)
            return;
        starAnim();
    }

    private void starAnim() {
        if (Picture1_ByteT==null)
            return;
        if (!animSet.isStarted()) {
            if (animSet.isPaused())
                animSet.resume();
            else animSet.start();
        }else animSet.start();
    }

    public SpinnerTopView setAnim1() {
        animSet.play(rotate);
        rotate.setDuration(13000);
        rotate.setRepeatCount(-1);
        rotate.setRepeatMode(ValueAnimator.RESTART);
//        fadeInOut.setDuration(13000);
//        fadeInOut.setRepeatCount(-1);
//        fadeInOut.setRepeatMode(ValueAnimator.RESTART);
        return this;
    }
    public SpinnerTopView setAnim2() {
        animSet.play(rotate).with(fadeInOut);
        rotate.setDuration(7000);
        rotate.setRepeatCount(-1);
        rotate.setRepeatMode(ValueAnimator.REVERSE);
        fadeInOut.setDuration(7000);
        fadeInOut.setRepeatCount(-1);
        fadeInOut.setRepeatMode(ValueAnimator.REVERSE);
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
    static int[] Colors = new int[]{
            R.color.colormain6,R.color.colormain1, R.color.colormain2, R.color.colormain3,
            R.color.colormain4, R.color.colormain5, R.color.colormain7, R.color.colorAccent
            , R.color.cyan, R.color.red, R.color.btn_green, R.color.blue_light
            , R.color.glod_t, R.color.colorPrimary, R.color.little_gray, R.color.light_orange
    };
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
        //int small= (int) ((int) bbb-(sR*3/5));
        int big=(int)mRadius;

        initDraw(canvas, (int) sR, (float) bbb);
        //drawImage(canvas,big-50,small-50,ret);
        int small=big/24*8;
        int ret= ((big-small)/16);
        if (isVisiImage) {
            isVisiImage = true;
            isVisiText = false;
            float x = 0;
            float y = 0;
            int Age;
            if (!isFixed)
                mPaint3.setColor(lastColor == 0 ? Color.WHITE : lastColor);
            for (Age = 0; Age < 360; Age++) {
                if (isFixed&&Age%30==0){
                    int a = (int) (Math.random() * 16);
                    mPaint3.setColor(getResources().getColor(Colors[a]));
                }
                for (int R = big; R > small; R = R - ret) {
                    x = mBallCenterX - (float) (R * Math.cos((Age / 180.0) * 3.1415926));
                    y = mBallCenterY - (float) (R * Math.sin((Age / 180.0) * 3.1415926));
                    int color ;
                    color = ( (Picture1_ByteT[(Age / 3) * 2] * 256 + Picture1_ByteT[(Age / 3) * 2 + 1])>>(  (  (R - small) / ret)-1 ) )&0x0001;
//                (R-small)/(big-small)
                    if (color > 0) {//少画几个点总是好的
//                        mPaint3.setColor(Color.WHITE );
                        if (prerent == 1)
                            canvas.drawCircle(x, y, 3, mPaint3);
                        else if (Age % prerent == 0)
                            canvas.drawCircle(x, y, 5, mPaint3);
                    } else {
//                        mPaint3.setColor(Color.RED );
//                        canvas.drawCircle(x, y, 3, mPaint3);
                        if ((R % 3 == 0) && (Age % 5 == 0))
                            canvas.drawLine(x, y, x + 1, y + 1, mPaint3);//画背景
                    }
                }
            }
        }

        if (isVisiText) {//奇怪
            if (!TextUtils.isEmpty(text)) {
                isVisiImage = false;
                isVisiText = true;
                mPaint2.setColor(lastColor==0?Color.WHITE:lastColor);
                //去锯齿的 不多说
                mPaint2.setAntiAlias(true);
                mPaint2.setTextSize(mRadius - small-30);
                mPaint2.setStyle(Paint.Style.STROKE);
                Path path = new Path();
                path.addCircle(mCenterX, mCenterY, (float) bbb, Path.Direction.CW);
                //canvas.drawPath(path, mPaint2);
                canvas.drawTextOnPath(text, path, 0, small/3, mPaint2);
            }
        }
//        if (false) {//isVisiBgDot
//            float x = 0;
//            float y = 0;
//            int Age;
//            for (Age = 0; Age < 360; Age++) {
//                //LogUtils.e("angle:"+Age+"--x"+x+"--y"+y);
//                for (int R = big; R > small+ret; R -=ret) {
//                    x = mBallCenterX - (float) (R * Math.cos((Age / 180.0) * 3.1415926));
//                    y = mBallCenterY - (float) (R * Math.sin((Age / 180.0) * 3.1415926));
//                    canvas.drawLine(x,y,x+1,y+1,mPaint3);
//                }
//            }
//        }
        //drawText(canvas,big, (float) bbb);

    }

    private void initDraw(Canvas canvas,int sR,float bbb) {
        mPaint3.setColor(Color.GRAY);
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

    @Deprecated
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
        for(Age=0;Age<360;Age++){
            for (int R = big; R > small; R = R - ret){
                x = mBallCenterX- (float)(R * Math.cos( (Age/180.0)*3.1415926));
                y = mBallCenterY- (float)(R * Math.sin((Age / 180.0) * 3.1415926));
                int color = 0;
                color =( (0x0001 << ((R - small) /ret)) & (Picture1_ByteT[(Age / 3) * 2] * 256 + Picture1_ByteT[(Age / 3) * 2 + 1]) );
//                (R-small)/(big-small)
                if (color > 0){//少画几个点总是好的
                    if (prerent==1)
                        canvas.drawCircle(x,y,3,mPaint3);
                    else if (Age%prerent==0)
                        canvas.drawCircle(x,y,5,mPaint3);
                }else {
                    if ((R%3==0)&&(Age%5==0))
                        canvas.drawLine(x,y,x+1,y+1,mPaint3);//画背景
                }
            }
        }
    }

    private void drawBall(Canvas canvas) {
        //point to the right drawable instance
        Drawable drawable = mBallDrawable.color(lastColor);
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
    private byte[] drawableToBitamp(Drawable drawable)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return MainFragment.getBitmapByte(bitmap);
    }

    public SpinnerTopView setmBgDrawable(IconDrawable mBgDrawable,boolean relayout) {
        //new IconDrawable(getContext(), FontAwesomeIcons.fa_futbol_o).colorRes(R.color.blue_t_alph);
        this.mBgDrawable = mBgDrawable.sizePx((int) mBallRadius).colorRes(R.color.grey_spinner);
        //setPicture1_ByteT(drawableToBitamp(mBgDrawable));
        if (relayout)
            invalidate();
        return this;
    }


}
