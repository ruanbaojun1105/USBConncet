package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hwx.usbconnect.usbconncet.AppConfig;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.hwx.usbconnect.usbconncet.ui.activity.UsbMainActivity;
import com.hwx.usbconnect.usbconncet.ui.adapter.SpinnerItemAdapter;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OnLineImageFragment extends SimpleFragment {


    @BindView(R.id.toedit_t)
    TextView toeditT;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.send)
    Button send;
    SpinnerItemAdapter itemClickAdapter;

    public OnLineImageFragment() {
        // Required empty public constructor
    }

    public static OnLineImageFragment newInstance() {
        OnLineImageFragment fragment = new OnLineImageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_online_mode_image;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (itemClickAdapter!=null){
            itemClickAdapter.setColor(((OnLineFragment)getParentFragment()).getLastColor());
            itemClickAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    private void initView() {
//        int a = (int) (Math.random() * 5);
//        int[] ass = new int[]{R.color.colormain1, R.color.colormain2, R.color.colormain3, R.color.colormain4, R.color.colormain5};
        //Glide.with(getActivity()).load(new ColorDrawable()).into(imageView);
        String itemPath = MainFragment.getInnerSDCardPath() + "/HWX-SPINNER/";
        String[] fileArr = MainFragment.getFileAll(mContext, new File(itemPath), false, false);
        String[] fileArrname = MainFragment.getFileAll(mContext, new File(itemPath), true, false);
        List<ImageFontTag> modList = new ArrayList<>();
        for (int i = 0; i < fileArr.length; i++) {
            ImageFontTag imageFontMod = new ImageFontTag(fileArr[i], fileArrname[i]);
            modList.add(imageFontMod);
        }
        itemClickAdapter = new SpinnerItemAdapter(modList,((OnLineFragment)getParentFragment()).getLastColor());
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        rvList.setLayoutManager(manager);
        rvList.setAdapter(itemClickAdapter);
        rvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                OnLineFragment fragment = (OnLineFragment) getParentFragment();
                String path = itemClickAdapter.getItem(position).getPath();
                fragment.changeToOne(MainFragment.readFile(mContext, path));
            }
        });
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LogUtils.e("onScrollStateChanged---"+newState);
                OnLineFragment fragment = (OnLineFragment) getParentFragment();
                fragment.setThisCleanAnim(newState!=0);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        send.setVisibility(modList.size() == 0 ? View.VISIBLE : View.GONE);
        send.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                OnLineFragment fragment = (OnLineFragment) getParentFragment();
                fragment.changeToOne(new byte[0]);
            }
        });
        if (modList.size() <6&& !AppConfig.getInstance().getBoolean("istip",false)) {
            AppConfig.getInstance().putBoolean("istip",true);
            new AlertDialog.Builder(mContext).setMessage(R.string.vkfajkddkt)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton(R.string.dtaddssd, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ((UsbMainActivity)mContext).toStarDownImage();
                        }
                    })
                    .setNegativeButton(R.string.gdadtt, null)
                    .show();
        }
    }

    public class  ImageFontTag{
        String path;
        String name;

        public ImageFontTag(String path, String name) {
            this.path = path;
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }
    }

}
