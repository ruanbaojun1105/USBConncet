package com.hwx.usbconnect.usbconncet.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwx.usbconnect.usbconncet.Application;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;

import net.youmi.android.AdManager;
import net.youmi.android.nm.cm.ErrorCode;
import net.youmi.android.nm.sp.SplashViewSettings;
import net.youmi.android.nm.sp.SpotListener;
import net.youmi.android.nm.sp.SpotManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        UsbMainActivity.changeAppLanguage(this);
        UsbMainActivity.changeAppLanguage(Application.getContext());
        setContentView(R.layout.activity_welcome);
        AdManager.getInstance(this).init("c76ea937d89eaa7d", "93645cbf2bb5c272", true);
        SplashViewSettings splashViewSettings = new SplashViewSettings();
        splashViewSettings.setAutoJumpToTargetWhenShowFailed(false);//默认自动跳转
        splashViewSettings.setTargetClass(UsbMainActivity.class);
        // 使用默认布局参数
        final FrameLayout splashLayout = (FrameLayout) findViewById(R.id.container);
//        FrameLayout.LayoutParams params =
//                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        splashViewSettings.setSplashViewContainer(splashLayout);
// 使   //用自定义布局参数
        //splashViewSettings.setSplashViewContainer();
        SpotManager.getInstance(this).showSplash(this, splashViewSettings,new SpotListener() {

            @Override
            public void onShowSuccess() {
                LogUtils.e("开屏展示成功");
            }

            @Override
            public void onShowFailed(int errorCode) {
                LogUtils.e("开屏展示失败");
                switch (errorCode) {
                    case ErrorCode.NON_NETWORK:
                        LogUtils.e("网络异常");
                        break;
                    case ErrorCode.NON_AD:
                        LogUtils.e("暂无开屏广告");
                        break;
                    case ErrorCode.RESOURCE_NOT_READY:
                        LogUtils.e("开屏资源还没准备好");
                        break;
                    case ErrorCode.SHOW_INTERVAL_LIMITED:
                        LogUtils.e("开屏展示间隔限制");
                        break;
                    case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                        LogUtils.e("开屏控件处在不可见状态");
                        break;
                    default:
                        LogUtils.e("errorCode: %d", errorCode+"");
                        break;
                }
            }

            @Override
            public void onSpotClosed() {
                LogUtils.e("开屏被关闭");
            }

            @Override
            public void onSpotClicked(boolean isWebPage) {
                LogUtils.e("开屏被点击");
                LogUtils.e("是否是网页广告？%s", isWebPage ? "是" : "不是");
            }
        });
        ImageView iv_welcome_te= (ImageView) findViewById(R.id.iv_welcome_te);
        iv_welcome_te.animate().rotation(1500f).setDuration(2300).setStartDelay(100).start();
        iv_welcome_te.animate().alpha(1f).setDuration(1000).setStartDelay(100).start();
        ImageView ivWelcomeBg= (ImageView) findViewById(R.id.iv_welcome_bg);
        List<String> list=getImgData();
        int page = getRandomNumber(0, list.size() - 1);
        Glide.with(this).load(list.get(page)).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivWelcomeBg);
        ivWelcomeBg.animate().scaleX(1.12f).scaleY(1.12f).setDuration(2300).setStartDelay(100).start();
        ivWelcomeBg.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, UsbMainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        },2500);
    }
    public static int getRandomNumber(int min, int max) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }

    private List<String> getImgData() {
        List<String> imgs = new ArrayList<>();
        imgs.add("file:///android_asset/a.jpg");
        imgs.add("file:///android_asset/b.jpg");
        imgs.add("file:///android_asset/c.jpg");
        imgs.add("file:///android_asset/d.jpg");
        imgs.add("file:///android_asset/e.jpg");
        imgs.add("file:///android_asset/f.jpg");
        imgs.add("file:///android_asset/g.jpg");
        return imgs;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(this).onDestroy();
    }
}
