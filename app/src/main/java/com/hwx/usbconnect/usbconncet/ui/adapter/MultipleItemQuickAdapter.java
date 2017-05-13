package com.hwx.usbconnect.usbconncet.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hwx.usbconnect.usbconncet.Application;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bean.AbsTypeMod;
import com.hwx.usbconnect.usbconncet.bean.ImageFontMod;
import com.hwx.usbconnect.usbconncet.bean.PresetMod;
import com.hwx.usbconnect.usbconncet.bean.TextMod;
import com.hwx.usbconnect.usbconncet.utils.ScreenParamsUtil;
import com.piotrek.customspinner.CustomSpinner;
import com.xw.repo.XEditText;

import java.util.List;

import cn.refactor.library.SmoothCheckBox;

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<AbsTypeMod, BaseViewHolder> {

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(AbsTypeMod.TEXT, R.layout.type_main_text);
        addItemType(AbsTypeMod.IMG, R.layout.type_main_image);
        addItemType(AbsTypeMod.PRESET, R.layout.type_main_preset);
        addItemType(AbsTypeMod.IMGFONT, R.layout.type_main_preset);
    }

    @Override
    protected void convert(BaseViewHolder helper, AbsTypeMod item) {
        item.setId(helper.getAdapterPosition());
        RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        layoutParams.height= (ScreenParamsUtil.getInstance((Activity) mContext).screenHeight- Application.dip2px(150))/7;
        helper.itemView.requestLayout();

        SmoothCheckBox scb = helper.getView(R.id.checkBox);
        scb.setChecked(item.isCheck(),true);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
                item.setCheck(isChecked);
            }
        });

        CustomSpinner colorSpinner1 = helper.getView(R.id.spinner1);
        colorSpinner1.initializeStringValues(new String[]{"红","黄","蓝","混色"},"请选择颜色▼");
        colorSpinner1.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
            }

            @Override
            public void onSpinnerClosed() {
                item.setColor(colorSpinner1.getSelectedItemPosition());
            }
        });
        colorSpinner1.setSelection(item.getColor());

        CustomSpinner colorSpinner2 = helper.getView(R.id.spinner2);
        colorSpinner2.initializeStringValues(new String[]{"上下滚","左右滚","固定"},"请选择模式▼");
        colorSpinner2.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
            }

            @Override
            public void onSpinnerClosed() {
                item.setModel(colorSpinner2.getSelectedItemPosition());
            }
        });
        colorSpinner2.setSelection(item.getModel());

        switch (helper.getItemViewType()) {
            case AbsTypeMod.TEXT:
                TextMod itemOd= (TextMod) item;
                XEditText editText = helper.getView(R.id.biucontainer);
                String tag=itemOd.getText();
                if (!TextUtils.isEmpty(tag)) {
                    editText.post(new Runnable() {
                        @Override
                        public void run() {
                            editText.setText(tag);
                        }
                    });
                }
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        itemOd.setText(charSequence.toString());
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                break;
            case AbsTypeMod.IMG:
                break;
            case AbsTypeMod.PRESET:
                PresetMod presetMod= (PresetMod) item;
                CustomSpinner choce_spinner = helper.getView(R.id.choce_spinner);
                choce_spinner.initializeStringValues(new String[]{"速度显示","电量显示","版本号"},"基础显示选择▼");
                choce_spinner.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
                    @Override
                    public void onSpinnerOpened() {
                    }

                    @Override
                    public void onSpinnerClosed() {
                        presetMod.setType(choce_spinner.getSelectedItemPosition());
                    }
                });
                choce_spinner.setBackgroundResource(R.drawable.setting_btnbg3);
                choce_spinner.setSelection(presetMod.getType());
                break;
            case AbsTypeMod.IMGFONT:
                ImageFontMod imageFontMod= (ImageFontMod) item;
                CustomSpinner choce_spinner1 = helper.getView(R.id.choce_spinner);
                choce_spinner1.initializeStringValues(new String[]{"八卦.bin","花朵.bin","旋风.bin"},"图片样式选择▼");
                choce_spinner1.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
                    @Override
                    public void onSpinnerOpened() {
                    }

                    @Override
                    public void onSpinnerClosed() {
                        imageFontMod.setFontId(choce_spinner1.getSelectedItemPosition());
                    }
                });
                choce_spinner1.setBackgroundResource(R.drawable.setting_btnbg4);
                choce_spinner1.setSelection(imageFontMod.getFontId());
                break;
        }
    }

}