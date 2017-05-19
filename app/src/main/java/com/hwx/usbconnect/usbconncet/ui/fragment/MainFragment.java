package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bean.AbsTypeMod;
import com.hwx.usbconnect.usbconncet.bean.ImageFontMod;
import com.hwx.usbconnect.usbconncet.bean.ImageMod;
import com.hwx.usbconnect.usbconncet.bean.PresetMod;
import com.hwx.usbconnect.usbconncet.bean.TextMod;
import com.hwx.usbconnect.usbconncet.bluetooth.BluetoothService;
import com.hwx.usbconnect.usbconncet.font.Font16;
import com.hwx.usbconnect.usbconncet.font.FontUtils;
import com.hwx.usbconnect.usbconncet.ui.activity.UsbMainActivity;
import com.hwx.usbconnect.usbconncet.ui.adapter.MultipleItemQuickAdapter;
import com.hwx.usbconnect.usbconncet.utils.ACache;
import com.hwx.usbconnect.usbconncet.utils.Constants;

import java.io.File;
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
            String itemPath=getInnerSDCardPath()+"/居一格/";
            String[] fileArr=getFileAll(new File(itemPath));
            data.add(new ImageFontMod(fileArr));
            data.add(new ImageFontMod(fileArr));
            data.add(new ImageFontMod(fileArr));
            data.add(new TextMod());
            data.add(new TextMod());
            data.add(new TextMod());
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
    }

    /**
     * 读取单个文件夹的所有文件
     * @return
     */
    private String[] getFileAll(File file) {
        if (file==null)
            return null;
        if (!file.exists())
            return null;
        String[] filelist = file.list();
        List<String> alist=new ArrayList<>();
        for (int i = 0; i < filelist.length; i++) {
            File readfile = new File(file.getPath() + "/" + filelist[i]);
            if (!readfile.isDirectory()) {
                if (readfile.getPath().endsWith(".mp4"))
                    alist.add(readfile.getPath());
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
                List<AbsTypeMod> listAbs=multipleItemAdapter.getData();
                ACache aCache = ACache.get(getContext());
                //aCache.remove(Constants.SAVE_DATA_KEY);
                aCache.put(Constants.SAVE_DATA_KEY, (Serializable) listAbs);
                for (int i = 0; i < listAbs.size(); i++) {
                    //url="file:///android_asset/video.mp4";
                    AbsTypeMod abs=listAbs.get(i);
                    //图片
                    if (abs instanceof ImageMod) {
                        ImageMod item1= (ImageMod) abs;
                        byte[] fileB=FontUtils.readFile(v.getContext(),item1.getImagePath());
                        byte [] data=Font16.byteMerger(new byte[]{item1.isCheck()?(byte)0x01:(byte)0x00,(byte)item1.getColor(),(byte)item1.getModel()},fileB);
                        UsbMainActivity.mScanHelper.sendData((byte)i,data,false);
                    }
                    //预定模式
                    if (abs instanceof PresetMod){
                        PresetMod item2= (PresetMod) abs;
                        UsbMainActivity.mScanHelper.sendData((byte)i,new byte[]{item2.isCheck()?(byte)0x01:(byte)0x00,(byte)item2.getColor(),(byte)item2.getModel(),(byte)item2.getType()},false);
                    }
                    //文本
                    if (abs instanceof TextMod){
                        TextMod item3= (TextMod) abs;
                        if (!TextUtils.isEmpty(item3.getText())) {
                            try {
                                byte [] data1=Font16.byteMerger(new byte[]{item3.isCheck()?(byte)0x01:(byte)0x00,(byte)item3.getColor(),(byte)item3.getModel()},item3.getText().getBytes("GB2312"));
                                byte [] data12=new Font16(v.getContext()).getStringFontByte(item3.getText());
                                UsbMainActivity.mScanHelper.sendData((byte) i,Font16.byteMerger(data1,data12),false);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }else {
                            UsbMainActivity.mScanHelper.sendData((byte) i,new byte[]{item3.isCheck()?(byte)0x01:(byte)0x00,(byte)item3.getColor(),(byte)item3.getModel()},false);
                        }
                    }
                    //预定图
                    if (abs instanceof ImageFontMod){
                        ImageFontMod item4= (ImageFontMod) abs;
                        byte[] fileB=FontUtils.readFile(v.getContext(),item4.getImagePath());
                        byte [] data=Font16.byteMerger(new byte[]{item4.isCheck()?(byte)0x01:(byte)0x00,(byte)item4.getColor(),(byte)item4.getModel()},fileB);
                        UsbMainActivity.mScanHelper.sendData((byte)i,data,false);
                    }
                }
                if (UsbMainActivity.mScanHelper.isScanConn())
                     Toast.makeText(v.getContext(), R.string.jagkjk, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(v.getContext(), R.string.fava, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
