package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;

import net.youmi.android.nm.bn.BannerManager;
import net.youmi.android.nm.bn.BannerViewListener;
import net.youmi.android.nm.cm.ErrorCode;
import net.youmi.android.nm.vdo.VideoAdListener;
import net.youmi.android.nm.vdo.VideoAdManager;
import net.youmi.android.nm.vdo.VideoAdSettings;

import me.weyye.hipermission.HiPermission;

import static com.hwx.usbconnect.usbconncet.Constants.isOpenVideo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private TextView name;
    private TextView this_info;


    public UseFragment() {
        // Required empty public constructor
    }

    public static UseFragment newInstance(String param1, String param2) {
        UseFragment fragment = new UseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_use, container, false);
        initView(rootView);

        if (!HiPermission.checkPermission(getContext(), Manifest.permission.READ_PHONE_STATE)){
            return rootView;
        }
        // 获取广告条
        View bannerView = BannerManager.getInstance(getContext())
                .getBannerView(getContext(), new BannerViewListener() {

                    @Override
                    public void onRequestSuccess() {
                        LogUtils.e("请求广告条成功");

                    }

                    @Override
                    public void onSwitchBanner() {
                        LogUtils.e("广告条切换");
                    }

                    @Override
                    public void onRequestFailed() {
                        LogUtils.e("请求广告条失败");
                    }
                });

// 获取要嵌入广告条的布局
        LinearLayout bannerLayout = (LinearLayout) rootView.findViewById(R.id.ll_banner);
// 将广告条加入到布局中
        bannerLayout.addView(bannerView);

        setupVideoAd(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        name = (TextView) rootView.findViewById(R.id.name);
        this_info = (TextView) rootView.findViewById(R.id.this_info);
        name.setText(mParam1);
        name.setTextColor(getResources().getColor(R.color.colorPrimary));
        this_info.setText(mParam2);
    }

    /**
     * 设置视频广告
     */
    private void setupVideoAd(View rootView) {
        Button btnShowVideoAd = (Button) rootView.findViewById(R.id.btn_show_video_ad);
        if (!isOpenVideo) {
            btnShowVideoAd.setVisibility(View.GONE);
            return;
        }
        // 设置视频广告
        final VideoAdSettings videoAdSettings = new VideoAdSettings();
        videoAdSettings.setInterruptTips("退出视频播放将无法获得奖励" + "\n确定要退出吗？");

        //		// 只需要调用一次，由于在主页窗口中已经调用了一次，所以此处无需调用
        //		VideoAdManager.getInstance().requestVideoAd(mContext);

        btnShowVideoAd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 展示视频广告
                VideoAdManager.getInstance(getContext())
                        .showVideoAd(getContext(), videoAdSettings, new VideoAdListener() {
                            @Override
                            public void onPlayStarted() {
                                LogUtils.e("开始播放视频");
                            }

                            @Override
                            public void onPlayInterrupted() {
                                LogUtils.e("播放视频被中断");
                            }

                            @Override
                            public void onPlayFailed(int errorCode) {
                                LogUtils.e("视频播放失败");
                                switch (errorCode) {
                                    case ErrorCode.NON_NETWORK:
                                        LogUtils.e("网络异常");
                                        break;
                                    case ErrorCode.NON_AD:
                                        LogUtils.e("视频暂无广告");
                                        break;
                                    case ErrorCode.RESOURCE_NOT_READY:
                                        LogUtils.e("视频资源还没准备好");
                                        break;
                                    case ErrorCode.SHOW_INTERVAL_LIMITED:
                                        LogUtils.e("视频展示间隔限制");
                                        break;
                                    case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                        LogUtils.e("视频控件处在不可见状态");
                                        break;
                                    default:
                                        LogUtils.e("请稍后再试");
                                        break;
                                }
                            }

                            @Override
                            public void onPlayCompleted() {
                                LogUtils.e("视频播放成功");
                            }
                        });
            }
        });
    }
}
