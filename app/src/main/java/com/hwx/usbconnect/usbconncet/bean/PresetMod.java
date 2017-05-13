package com.hwx.usbconnect.usbconncet.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/11.
 */

public class PresetMod extends AbsTypeMod implements Parcelable {
    int type;//速度显示，点亮显示，版本号

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PresetMod(int type) {
        this.type = type;
    }

    public PresetMod() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
    }

    protected PresetMod(Parcel in) {
        this.type = in.readInt();
    }

    public static final Creator<PresetMod> CREATOR = new Creator<PresetMod>() {
        @Override
        public PresetMod createFromParcel(Parcel source) {
            return new PresetMod(source);
        }

        @Override
        public PresetMod[] newArray(int size) {
            return new PresetMod[size];
        }
    };

    @Override
    public int getItemType() {
        return PRESET;
    }
}
