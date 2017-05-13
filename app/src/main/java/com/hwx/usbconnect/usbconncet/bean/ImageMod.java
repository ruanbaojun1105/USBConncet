package com.hwx.usbconnect.usbconncet.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/11.
 */

public class ImageMod extends AbsTypeMod implements Parcelable {
    String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageMod(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageMod() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imagePath);
    }

    protected ImageMod(Parcel in) {
        this.imagePath = in.readString();
    }

    public static final Parcelable.Creator<ImageMod> CREATOR = new Parcelable.Creator<ImageMod>() {
        @Override
        public ImageMod createFromParcel(Parcel source) {
            return new ImageMod(source);
        }

        @Override
        public ImageMod[] newArray(int size) {
            return new ImageMod[size];
        }
    };
    @Override
    public int getItemType() {
        return IMG;
    }
}
