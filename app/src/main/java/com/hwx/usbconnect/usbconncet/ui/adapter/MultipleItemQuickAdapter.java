package com.hwx.usbconnect.usbconncet.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hwx.usbconnect.usbconncet.App;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bean.AbsTypeMod;
import com.hwx.usbconnect.usbconncet.bean.ImageFontMod;
import com.hwx.usbconnect.usbconncet.bean.PresetMod;
import com.hwx.usbconnect.usbconncet.bean.TextMod;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.hwx.usbconnect.usbconncet.utils.ScreenParamsUtil;
import com.hwx.usbconnect.usbconncet.utils.StateButton;
import com.piotrek.customspinner.CustomSpinner;
import com.xw.repo.XEditText;

import java.util.List;

import com.hwx.usbconnect.usbconncet.utils.SmoothCheckBox;

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<AbsTypeMod, BaseViewHolder> {

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(AbsTypeMod.TEXT, R.layout.type_main_text);//跟新
        addItemType(AbsTypeMod.IMG, R.layout.type_main_image);
        addItemType(AbsTypeMod.PRESET, R.layout.type_main_preset);
        addItemType(AbsTypeMod.IMGFONT, R.layout.type_main_preset);
    }

    @Override
    protected void convert(BaseViewHolder helper, final AbsTypeMod item) {
        item.setId(helper.getAdapterPosition());
        RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        layoutParams.height= (ScreenParamsUtil.getInstance((Activity) mContext).screenHeight- App.dip2px(150))/8;
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

        final CustomSpinner colorSpinner1 = helper.getView(R.id.spinner1);
        colorSpinner1.initializeStringValues(new String[]{
                mContext.getString(R.string.vaddf),
                mContext.getString(R.string.vaf),
                mContext.getString(R.string.vafg),
                mContext.getString(R.string.gaf)},mContext.getString(R.string.vfag));
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

        final CustomSpinner colorSpinner2 = helper.getView(R.id.spinner2);
        colorSpinner2.initializeStringValues(new String[]{
                mContext.getString(R.string.vvag),
                mContext.getString(R.string.vaga),
                mContext.getString(R.string.vaggagkjoo)},
                mContext.getString(R.string.ajkjkk));
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
                final TextMod itemOd= (TextMod) item;
                TextView editText = helper.getView(R.id.biucontainer);
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        showEditDialog(((Activity) mContext), mContext.getString(R.string.gdjiadstjj), itemOd.getText(),itemOd.getFontStyle(), new EditOnclick() {
                            @Override
                            public void onClick(final String str, final int fontS) {
                                itemOd.setText(str);
                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((TextView)view).setText(str);
                                        itemOd.setFontStyle(fontS);
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                    }
                });
                String tag=itemOd.getText();
                if (!TextUtils.isEmpty(tag)) {
                    editText.setText(tag);
                }
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        itemOd.setText(charSequence.toString());
//                    }
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                    }
//                });
                final CustomSpinner fontSpinner = helper.getView(R.id.font_spinner);
                fontSpinner.setVisibility(View.GONE);
                String a=mContext.getString(R.string.gtadtrtet);
                String b=mContext.getString(R.string.dtastdtrwqg);
                fontSpinner.initializeStringValues(new String[]{a,b},mContext.getString(R.string.drrtr));
                fontSpinner.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
                    @Override
                    public void onSpinnerOpened() {
                    }

                    @Override
                    public void onSpinnerClosed() {
                        LogUtils.e("----选择了"+fontSpinner.getSelectedItemPosition());
                        itemOd.setFontStyle(fontSpinner.getSelectedItemPosition());
                    }
                });
                fontSpinner.setSelection(itemOd.getFontStyle());
                break;
            case AbsTypeMod.IMG:
                break;
            case AbsTypeMod.PRESET:
                final PresetMod presetMod= (PresetMod) item;
                final CustomSpinner choce_spinner = helper.getView(R.id.choce_spinner);
                choce_spinner.initializeStringValues(new String[]{
                        mContext.getString(R.string.vfagbfgf),
                        mContext.getString(R.string.fdggggg),
                        mContext.getString(R.string.fjigjj)},mContext.getString(R.string.vagttew));
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
                final ImageFontMod imageFontMod= (ImageFontMod) item;
                final CustomSpinner choce_spinner1 = helper.getView(R.id.choce_spinner);
                //fileName = fName.substring(fName.lastIndexOf("//")+1);
                choce_spinner1.initializeStringValues(imageFontMod.getFileName()!=null?imageFontMod.getFileName():new String[]{},
                        mContext.getString(R.string.bfdfhdhdhf));
                choce_spinner1.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
                    @Override
                    public void onSpinnerOpened() {
                    }

                    @Override
                    public void onSpinnerClosed() {
//                        data.add(new ImageFontMod("file:///android_asset/八卦.bin"));
//                        data.add(new ImageFontMod("file:///android_asset/花朵.bin"));
//                        data.add(new ImageFontMod("file:///android_asset/旋风.bin"));
                        LogUtils.e("----选择了"+choce_spinner1.getSelectedItemPosition());
                        imageFontMod.setFontId(choce_spinner1.getSelectedItemPosition());
                        if (choce_spinner1.getSelectedItemPosition()==0)
                            return;
                        if (imageFontMod.getFileArr()==null)
                            return;
                        else
                            imageFontMod.setImagePath(imageFontMod.getFileArr()[choce_spinner1.getSelectedItemPosition()-1]);
                        /*switch (choce_spinner1.getSelectedItemPosition()){
                            case 0:
                                imageFontMod.setImagePath("file:///android_asset/八卦.bin");
                                break;
                            case 1:
                                imageFontMod.setImagePath("file:///android_asset/花朵.bin");
                                break;
                            case 2:
                                imageFontMod.setImagePath("file:///android_asset/旋风.bin");
                                break;
                        }*/
                    }
                });
                choce_spinner1.setBackgroundResource(R.drawable.setting_btnbg4);
                choce_spinner1.setSelection(imageFontMod.getFontId());
                break;
        }
    }

    interface  EditOnclick{
        void onClick(String str,int fontS);
    }

    public void showEditDialog(final Activity activity, String title,String content,int fontS, final EditOnclick onclickInterFace){
        final View view=View.inflate(activity,R.layout.type_main_text_dialog, null);
        final XEditText et= (XEditText) view.findViewById(R.id.biucontainer);
       /* et.setDisableEmoji(true);
        //final EditText et = new EditText(activity);
        et.setMaxLines(5);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});*/
        if (!TextUtils.isEmpty(content)) {
            et.setText(content);
            et.setSelection(content.length());
        }
        //et.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        final StateButton font_default= (StateButton) view.findViewById(R.id.font_default);
        final StateButton font_bold= (StateButton) view.findViewById(R.id.font_bold);
        font_default.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                font_default.setEnabled(false);
                font_bold.setEnabled(true);
                view.setTag(1);
            }
        });
        font_bold.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                font_default.setEnabled(true);
                font_bold.setEnabled(false);
                view.setTag(2);
            }
        });
        if (fontS==0||fontS==1){
            font_default.callOnClick();
        }else if(fontS==2){
            font_bold.callOnClick();
        }
        new AlertDialog.Builder(activity)
                //.setTitle(title)
                //.setIcon(android.R.drawable.ic_menu_send)
                .setView(view)
                .setPositiveButton(R.string.dtaddssd, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (activity==null)
                            return;
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                        final String input = et.getText().toString();
//                    if (TextUtils.isEmpty(input))
//                        return;
                        if (onclickInterFace!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onclickInterFace.onClick(input, (Integer) view.getTag());
                                }
                            });
                        }
                    }
                })
                .setNegativeButton(R.string.dadttsadts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    }
                })
                .show();
    }
}