package com.hwx.usbconnect.usbconncet.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwx.usbconnect.usbconncet.App;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.utils.ScreenParamsUtil;
import com.jaeger.library.StatusBarUtil;
import com.yalantis.starwars.TilesFrameLayout;
import com.yalantis.starwars.interfaces.TilesFrameLayoutListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WelcomeActivity extends AppCompatActivity {

    ImageView ivWelcomeBg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ivWelcomeBg= (ImageView) findViewById(R.id.iv_welcome_bg);
        ImageView iv_welcome_te= (ImageView) findViewById(R.id.iv_welcome_te);
        iv_welcome_te.bringToFront();
        StatusBarUtil.setTranslucentForImageView(this,0,ivWelcomeBg);
        List<String> list=getImgData();
        int page = getRandomNumber(0, list.size() - 1);
        Glide.with(this).load(list.get(page)).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivWelcomeBg);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setStartDelay(300);
        animSet.setInterpolator(new BounceInterpolator());//OvershootInterpolator
        ObjectAnimator rotate = ObjectAnimator.ofFloat(iv_welcome_te, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(iv_welcome_te, "alpha", 0.0f, 0.9f);
        ObjectAnimator tray = ObjectAnimator.ofFloat(iv_welcome_te, "translationY", 0f, -ScreenParamsUtil.getInstance(this).screenHeight/2+App.dip2px(20));
        ObjectAnimator trax = ObjectAnimator.ofFloat(iv_welcome_te, "translationX", 0f, ScreenParamsUtil.getInstance(this).screenWidth/2-App.dip2px(20));
        ObjectAnimator scx1 = ObjectAnimator.ofFloat(iv_welcome_te, "scaleX", 1f, 3f);
        ObjectAnimator scy2 = ObjectAnimator.ofFloat(iv_welcome_te, "scaleY", 1f, 3f);
        ObjectAnimator scx = ObjectAnimator.ofFloat(ivWelcomeBg, "scaleX", 1f, 1.2f);
        ObjectAnimator scy = ObjectAnimator.ofFloat(ivWelcomeBg, "scaleY", 1f, 1.2f);
        ObjectAnimator out = ObjectAnimator.ofFloat(ivWelcomeBg, "alpha", 1f, 0.3f);
        animSet.setDuration(3300);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                Intent intent = new Intent(WelcomeActivity.this, UsbMainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animSet.play(rotate).with(fadeInOut).with(scx).with(scy).with(tray).with(trax).with(scx1).with(scy2).with(out);
        animSet.start();

    }
    public static int getRandomNumber(int min, int max) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }
    private List<String> getImgData() {
        List<String> imgs = new ArrayList<>();
        imgs.add("file:///android_asset/c.jpg");
        imgs.add("file:///android_asset/f.jpg");
        imgs.add("file:///android_asset/g.jpg");
        imgs.add("file:///android_asset/h.png");
        return imgs;
    }

}
