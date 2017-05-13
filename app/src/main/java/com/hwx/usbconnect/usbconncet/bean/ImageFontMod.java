package com.hwx.usbconnect.usbconncet.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/11.
 */

public class ImageFontMod extends AbsTypeMod implements Parcelable {
    int fontId;
    String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageFontMod(String imagePath) {
        this.imagePath = imagePath;
    }
    public ImageFontMod() {
    }
    public int getFontId() {
        return fontId;
    }

    public void setFontId(int fontId) {
        this.fontId = fontId;
    }

    @Override
    public int getItemType() {
        return IMGFONT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fontId);
    }

    protected ImageFontMod(Parcel in) {
        this.fontId = in.readInt();
    }

    public static final Parcelable.Creator<ImageFontMod> CREATOR = new Parcelable.Creator<ImageFontMod>() {
        @Override
        public ImageFontMod createFromParcel(Parcel source) {
            return new ImageFontMod(source);
        }

        @Override
        public ImageFontMod[] newArray(int size) {
            return new ImageFontMod[size];
        }
    };
}
