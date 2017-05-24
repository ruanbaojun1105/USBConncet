package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.hwx.usbconnect.usbconncet.Application;
import com.hwx.usbconnect.usbconncet.BuildConfig;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bean.AbsTypeMod;
import com.hwx.usbconnect.usbconncet.bean.ImageFontMod;
import com.hwx.usbconnect.usbconncet.bean.ImageMod;
import com.hwx.usbconnect.usbconncet.bean.PresetMod;
import com.hwx.usbconnect.usbconncet.bean.TextMod;
import com.hwx.usbconnect.usbconncet.bluetooth.BluetoothService;
import com.hwx.usbconnect.usbconncet.font.Font16;
import com.hwx.usbconnect.usbconncet.font.FontUtils;
import com.hwx.usbconnect.usbconncet.ui.ScanHelper;
import com.hwx.usbconnect.usbconncet.ui.activity.UsbMainActivity;
import com.hwx.usbconnect.usbconncet.ui.adapter.MultipleItemQuickAdapter;
import com.hwx.usbconnect.usbconncet.utils.ACache;
import com.hwx.usbconnect.usbconncet.utils.AppConfig;
import com.hwx.usbconnect.usbconncet.utils.Constants;
import com.hwx.usbconnect.usbconncet.utils.FileUtil;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private RecyclerView mRecyclerView;
    private Button updateData;
    private MultipleItemQuickAdapter multipleItemAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(rootView);
        return rootView;
    }
    public static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }
    private void initView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
        List<AbsTypeMod> data = new ArrayList<>();
        ACache aCache = ACache.get(getContext());
        Object obj = aCache.getAsObject(Constants.SAVE_DATA_KEY);
        if (obj == null) {
            String itemPath=getInnerSDCardPath()+"/HWX-SPINNER/";
            String[] fileArr=getFileAll(new File(itemPath),false);
            String[] fileArrname=getFileAll(new File(itemPath),true);
            data.add(new ImageFontMod(fileArr,fileArrname));
            data.add(new ImageFontMod(fileArr,fileArrname));
            data.add(new ImageFontMod(fileArr,fileArrname));
            data.add(new TextMod("Hello word"));
            data.add(new TextMod("Hello word"));
            data.add(new TextMod("Hello word"));
            data.add(new PresetMod());
        } else {
            data = (List<AbsTypeMod>) obj;
        }
        multipleItemAdapter = new MultipleItemQuickAdapter(data);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
//        multipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
//                return data.get(position).getSpanSize();
//            }
//        });
        mRecyclerView.setAdapter(multipleItemAdapter);
        updateData = (Button) rootView.findViewById(R.id.updateData);
        updateData.setOnClickListener(this);
        /*mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mRecyclerView.getWindowToken(), 0);
            }
        });*/
    }

    /**
     * 读取单个文件夹的所有文件
     * @return
     */
    private String[] getFileAll(File file,boolean isName) {
        if (file==null)
            return null;
        if (!file.exists()) {
            file.mkdir();
        }
        if (!AppConfig.getInstance().getBoolean("isCopy",false)){
            try {
                FileUtil.copyFile(getResources().getAssets().open("xuanfeng.bin"),file.getPath()+"/xuanfeng.bin");
                FileUtil.copyFile(getResources().getAssets().open("shu.bin"),file.getPath()+"/shu.bin");
                AppConfig.getInstance().putBoolean("isCopy",true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] filelist = file.list();
        if (filelist==null)
            return null;
        List<String> alist=new ArrayList<>();
        for (int i = 0; i < filelist.length; i++) {
            File readfile = new File(file.getPath() + "/" + filelist[i]);
            if (!readfile.isDirectory()) {
                if (readfile.getPath().endsWith(".bin"))
                    alist.add(isName?readfile.getName():readfile.getPath());
            }
        }
        String[] files = new String[alist.size()];
        for (int i = 0; i <alist.size() ; i++) {
            files[i]=alist.get(i);
        }

        return files;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateData:
                if (com.hwx.usbconnect.usbconncet.Constants.isOpenLim) {
                    int a=AppConfig.getInstance().getInt("success",1);
                    if (a > 20) {
                        new AlertDialog.Builder(getContext()).setMessage(R.string.ftdttt)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setPositiveButton(R.string.dttadfdc, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton(R.string.gdadtt, null)
                                .show();
                        return;
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<AbsTypeMod> listAbs=multipleItemAdapter.getData();
                        ACache aCache = ACache.get(getContext());
                        //aCache.remove(Constants.SAVE_DATA_KEY);
                        aCache.put(Constants.SAVE_DATA_KEY, (Serializable) listAbs);
                        dataMain=new byte[0];
                        TextMain=new byte[0];
                        if (!UsbMainActivity.mScanHelper.isScanConn()){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), R.string.dsttaat,Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        UsbMainActivity.mScanHelper.star();
                        for (int i = 0; i < listAbs.size(); i++) {
                            detailData(listAbs.get(i),v,i);
                            LogUtils.e("log comment","abs"+listAbs.get(i).getId());
                        }
                        UsbMainActivity.mScanHelper.sendData(Font16.byteMerger(dataMain,ScanHelper.sendDataSSS((byte) 0x09,TextMain,false)));
                    }
                }).start();

                break;
        }
    }

    byte[] dataMain=new byte[0];
    byte[] TextMain=new byte[0];

    private void detailData(AbsTypeMod absTypeMod,View v,int i) {
        //url="file:///android_asset/video.mp4";
        AbsTypeMod abs=absTypeMod;
        //图片
        if (abs instanceof ImageMod) {
            ImageMod item1= (ImageMod) abs;
            byte[] fileB=readFile(v.getContext(),item1.getImagePath());
            byte [] data=Font16.byteMerger(new byte[]{item1.isCheck()?(byte)0x01:(byte)0x00,(byte)item1.getColor(),(byte)item1.getModel()},fileB);
            dataMain=Font16.byteMerger(dataMain,ScanHelper.sendDataSSS((byte)item1.getId(),data,false));
        }
        //预定模式
        if (abs instanceof PresetMod){
            PresetMod item2= (PresetMod) abs;
            dataMain=Font16.byteMerger(dataMain,ScanHelper.sendDataSSS((byte)item2.getId(),new byte[]{item2.isCheck()?(byte)0x01:(byte)0x00,(byte)item2.getColor(),(byte)item2.getModel(),(byte)item2.getType()},false));
        }
        //文本
        if (abs instanceof TextMod){
            TextMod item3= (TextMod) abs;
            if (!TextUtils.isEmpty(item3.getText())) {
                try {
                    byte [] data1=Font16.byteMerger(new byte[]{item3.isCheck()?(byte)0x01:(byte)0x00,(byte)item3.getColor(),(byte)item3.getModel()},item3.getText().getBytes("GB2312"));
                    byte [] data12=new Font16(v.getContext()).getStringFontByte(item3.getText());
                    dataMain=Font16.byteMerger(dataMain,ScanHelper.sendDataSSS((byte) item3.getId(),data1,false));
                    TextMain=Font16.byteMerger(TextMain,data12);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else {
                dataMain=Font16.byteMerger(dataMain,ScanHelper.sendDataSSS((byte) item3.getId(),new byte[]{item3.isCheck()?(byte)0x01:(byte)0x00,(byte)item3.getColor(),(byte)item3.getModel()},false));
            }
        }
        //预定图
        if (abs instanceof ImageFontMod){
            ImageFontMod item4= (ImageFontMod) abs;
            byte[] fileB=readFile(v.getContext(),item4.getImagePath());
            byte [] data=Font16.byteMerger(new byte[]{item4.isCheck()?(byte)0x01:(byte)0x00,(byte)item4.getColor(),(byte)item4.getModel()},fileB);
            dataMain=Font16.byteMerger(dataMain,ScanHelper.sendDataSSS((byte)item4.getId(),data,false));
        }
    }

    public static byte[] readFile(Context context, String filePath) {
        byte[] data = new byte[240];
        try {
            LogUtils.e("file---"+filePath);
            //InputStream in = context.getResources().getAssets().open(filePath);
            InputStream in =new FileInputStream(filePath);
            in.read(data, 0, 240);
            in.close();
            if (BuildConfig.DEBUG) {
                StringBuilder buil = new StringBuilder("");
                for (int i = 0; i < 240; i++) {
                    //data[i]=(byte) i;
                    buil.append((i % 15 == 0 ? "\n" : " ")/*+i+"- "+data[i]+"-"*/ + Integer.toHexString(data[i]));
                }
                LogUtils.e("------" + buil.toString());
            }
        } catch (Exception ex) {
        }
        return data;
    }
}
