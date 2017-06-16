package com.hwx.usbconnect.usbconncet.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwx.usbconnect.usbconncet.App;
import com.hwx.usbconnect.usbconncet.R;

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
        UsbMainActivity.changeAppLanguage(App.getContext());
        setContentView(R.layout.activity_welcome);
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
}
