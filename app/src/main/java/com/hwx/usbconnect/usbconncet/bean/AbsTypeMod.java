package com.hwx.usbconnect.usbconncet.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/11.
 */

public abstract class AbsTypeMod implements MultiItemEntity,Serializable {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int PRESET = 3;
    public static final int IMGFONT = 4;

    int color;//颜色选择：红黄蓝，混色
    int model;//上下滚，左右滚，固定
    boolean isCheck;//是否勾选
    int id;//当前ID编号

    public AbsTypeMod() {
    }

    public AbsTypeMod(int color, int model, boolean isCheck, int id) {
        this.color = color;
        this.model = model;
        this.isCheck = isCheck;
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
