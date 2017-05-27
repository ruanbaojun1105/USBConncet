package com.hwx.usbconnect.usbconncet.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/11.
 */

public class ImageFontMod extends AbsTypeMod implements Parcelable {
    int fontId;
    String imagePath;
    String[] fileArr;
    String[] fileName;

    public String[] getFileName() {
        return fileName;
    }

    public ImageFontMod setFileName(String[] fileName) {
        this.fileName = fileName;
        return this;
    }

    public String[] getFileArr() {
        return fileArr;
    }

    public ImageFontMod setFileArr(String[] fileArr) {
        this.fileArr = fileArr;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageFontMod(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageFontMod(String[] fileArr, String[] fileName) {
        this.fileArr = fileArr;
        this.fileName = fileName;
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
        dest.writeString(this.imagePath);
        dest.writeStringArray(this.fileArr);
    }

    protected ImageFontMod(Parcel in) {
        this.fontId = in.readInt();
        this.imagePath = in.readString();
        this.fileArr = in.createStringArray();
    }

    public static final Creator<ImageFontMod> CREATOR = new Creator<ImageFontMod>() {
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
