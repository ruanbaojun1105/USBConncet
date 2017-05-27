package com.hwx.usbconnect.usbconncet.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/11.
 */

public class TextMod extends AbsTypeMod implements Parcelable {
    String text;
    int fontStyle;

    public TextMod() {
    }

    public TextMod(String text, int fontStyle) {
        this.text = text;
        this.fontStyle = fontStyle;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public TextMod(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getItemType() {
        return TEXT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeInt(this.fontStyle);
    }

    protected TextMod(Parcel in) {
        this.text = in.readString();
        this.fontStyle = in.readInt();
    }

    public static final Parcelable.Creator<TextMod> CREATOR = new Parcelable.Creator<TextMod>() {
        @Override
        public TextMod createFromParcel(Parcel source) {
            return new TextMod(source);
        }

        @Override
        public TextMod[] newArray(int size) {
            return new TextMod[size];
        }
    };
}
