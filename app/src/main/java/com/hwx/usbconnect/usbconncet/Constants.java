package com.hwx.usbconnect.usbconncet;

/**
 * Created by Administrator on 2016/8/23.
 */
public class Constants {
    public static final boolean isOpenVideo=true;//是否开启视频广告
    public static final boolean isOpenEN=false;//是否开启显示英文


    public static final boolean isOpenCutInfo=true;//是否开启去掉公司介绍
    public static final boolean isOpenLim=false;//是否开启使用次数限制


    //#define USBD_VID  0x28e9
    //#define USBD_PID  0x028A
    public static final int[] DEVICE_PIDS = {650};//650//393
    public static final int[] DEVICE_VIDS = {10473};//104773
    public static final String SAVE_DATA_KEY="SAVE_DATA_KEY";


    public static final String server_url="120.76.118.136:8080";
    public static String Deviceid="1";
    public static String keyPwd="11111111";
    public static final String SD_KEY_PATH= "http://oa8iajf2m.bkt.clouddn.com/KEY_DATA.xls";

    public static final String MILK_DO_OK="MILK_DO_OK";
    public static final String MILK_DO_FAIL="MILK_DO_FAIL";

    public static final String SERIAL_PORT_COMMAND="SERIAL_PORT_COMMAND";//接收
    public static final String SERIAL_PORT_SEND="SERIAL_PORT_SEND";//发送的数据
    public static final String SERIAL_PORT_CONNECT_FAIL="SERIAL_PORT_CONNECT_FAIL";//蓝牙连接失败
    public static final String SERIAL_PORT_CONNECT_LOST="SERIAL_PORT_CONNECT_LOST";//蓝牙连接丢失
    public static final String SERIAL_PORT_CONNECT_STATE="SERIAL_PORT_CONNECT_STATE";//蓝牙连接状态
    public static final String SERIAL_PORT_CONNECT_NAME="SERIAL_PORT_CONNECT_NAME";//蓝牙连接设备的名字
    public static final String arg_isManagerEdti="arg_isManagerEdti";

    public static final int PAGE_IMAGE_COUNT=17;//固定一页总产品数量
    public static final int PAGE_COUNT=4;//固定总共支持的页数
    public static final int SCREEN_TIME_COUNT=300;//屏幕不动现实视频的时间

    public static int isLoadData=0;//1本地加载或2加载服务器 0不同步

    public static final String REFRESH_IAMGE="REFRESH_IAMGE";

    public static String VIDEO_PATH="";

    public static char nAsciiDot[] =              // ASCII
            {
                    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,  // - -
                    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x1C,0x00,0x3F,0xB0,  // -!-
                    0x3F,0xB0,0x1C,0x00,0x00,0x00,0x00,0x00,

                    0x00,0x00,0x70,0x00,0x78,0x00,0x00,0x00,  // -"-
                    0x00,0x00,0x78,0x00,0x70,0x00,0x00,0x00,

                    0x04,0x40,0x1F,0xF0,0x1F,0xF0,0x04,0x40,  // -#-
                    0x1F,0xF0,0x1F,0xF0,0x04,0x40,0x00,0x00,

                    0x1C,0xC0,0x3E,0x60,0x22,0x20,0xE2,0x38,  // -$-
                    0xE2,0x38,0x33,0xE0,0x19,0xC0,0x00,0x00,

                    0x0C,0x30,0x0C,0x60,0x00,0xC0,0x01,0x80,  // -%-
                    0x03,0x00,0x06,0x30,0x0C,0x30,0x00,0x00,

                    0x01,0xE0,0x1B,0xF0,0x3E,0x10,0x27,0x10,  // -&-
                    0x3D,0xE0,0x1B,0xF0,0x02,0x10,0x00,0x00,

                    0x00,0x00,0x08,0x00,0x78,0x00,0x70,0x00,  // -'-
                    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x0F,0xC0,0x1F,0xE0,  // -(-
                    0x30,0x30,0x20,0x10,0x00,0x00,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x20,0x10,0x30,0x30,  // -)-
                    0x1F,0xE0,0x0F,0xC0,0x00,0x00,0x00,0x00,

                    0x01,0x00,0x05,0x40,0x07,0xC0,0x03,0x80,  // -*-
                    0x03,0x80,0x07,0xC0,0x05,0x40,0x01,0x00,

                    0x00,0x00,0x01,0x00,0x01,0x00,0x07,0xC0,  // -+-
                    0x07,0xC0,0x01,0x00,0x01,0x00,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x00,0x08,0x00,0x78,  // -,-
                    0x00,0x70,0x00,0x00,0x00,0x00,0x00,0x00,

                    0x01,0x00,0x01,0x00,0x01,0x00,0x01,0x00,  // ---
                    0x01,0x00,0x01,0x00,0x01,0x00,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x30,  // -.-
                    0x00,0x30,0x00,0x00,0x00,0x00,0x00,0x00,

                    0x00,0x30,0x00,0x60,0x00,0xC0,0x01,0x80,  // -/-
                    0x03,0x00,0x06,0x00,0x0C,0x00,0x00,0x00,

                    0x1F,0xE0,0x3F,0xF0,0x20,0x90,0x23,0x10,  // -0-
                    0x24,0x10,0x3F,0xF0,0x1F,0xE0,0x00,0x00,

                    0x00,0x00,0x08,0x10,0x18,0x10,0x3F,0xF0,  // -1-
                    0x3F,0xF0,0x00,0x10,0x00,0x10,0x00,0x00,

                    0x10,0x70,0x30,0xF0,0x21,0x90,0x23,0x10,  // -2-
                    0x26,0x10,0x3C,0x30,0x18,0x30,0x00,0x00,

                    0x10,0x20,0x30,0x30,0x22,0x10,0x22,0x10,  // -3-
                    0x22,0x10,0x3F,0xF0,0x1D,0xE0,0x00,0x00,

                    0x03,0x00,0x07,0x00,0x0D,0x00,0x19,0x10,  // -4-
                    0x3F,0xF0,0x3F,0xF0,0x01,0x10,0x00,0x00,

                    0x3E,0x20,0x3E,0x30,0x22,0x10,0x22,0x10,  // -5-
                    0x23,0x10,0x23,0xF0,0x21,0xE0,0x00,0x00,

                    0x0F,0xE0,0x1F,0xF0,0x32,0x10,0x22,0x10,  // -6-
                    0x22,0x10,0x03,0xF0,0x01,0xE0,0x00,0x00,

                    0x30,0x00,0x30,0x00,0x20,0xF0,0x21,0xF0,  // -7-
                    0x23,0x00,0x3E,0x00,0x3C,0x00,0x00,0x00,

                    0x1D,0xE0,0x3F,0xF0,0x22,0x10,0x22,0x10,  // -8-
                    0x22,0x10,0x3F,0xF0,0x1D,0xE0,0x00,0x00,

                    0x1C,0x00,0x3E,0x10,0x22,0x10,0x22,0x10,  // -9-
                    0x22,0x30,0x3F,0xE0,0x1F,0xC0,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x00,0x00,0x0C,0x60,  // -:-
                    0x0C,0x60,0x00,0x00,0x00,0x00,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x00,0x10,0x0C,0x70,  // -;-
                    0x0C,0x60,0x00,0x00,0x00,0x00,0x00,0x00,

                    0x00,0x00,0x01,0x00,0x03,0x80,0x06,0xC0,  // -<-
                    0x0C,0x60,0x18,0x30,0x10,0x10,0x00,0x00,

                    0x02,0x40,0x02,0x40,0x02,0x40,0x02,0x40,  // -=-
                    0x02,0x40,0x02,0x40,0x02,0x40,0x00,0x00,

                    0x00,0x00,0x10,0x10,0x18,0x30,0x0C,0x60,  // ->-
                    0x06,0xC0,0x03,0x80,0x01,0x00,0x00,0x00,

                    0x18,0x00,0x38,0x00,0x20,0x00,0x23,0xB0,  // -?-
                    0x27,0xB0,0x3C,0x00,0x18,0x00,0x00,0x00,

                    0x0F,0xE0,0x1F,0xF0,0x10,0x10,0x13,0xD0,  // -@-
                    0x13,0xD0,0x1F,0xD0,0x0F,0x80,0x00,0x00,

                    0x07,0xF0,0x0F,0xF0,0x19,0x00,0x31,0x00,  // -A-
                    0x19,0x00,0x0F,0xF0,0x07,0xF0,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x22,0x10,  // -B-
                    0x22,0x10,0x3F,0xF0,0x1D,0xE0,0x00,0x00,

                    0x0F,0xC0,0x1F,0xE0,0x30,0x30,0x20,0x10,  // -C-
                    0x20,0x10,0x30,0x30,0x18,0x60,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x20,0x10,  // -D-
                    0x30,0x30,0x1F,0xE0,0x0F,0xC0,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x22,0x10,  // -E-
                    0x27,0x10,0x30,0x30,0x38,0x70,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x22,0x10,  // -F-
                    0x27,0x00,0x30,0x00,0x38,0x00,0x00,0x00,

                    0x0F,0xC0,0x1F,0xE0,0x30,0x30,0x21,0x10,  // -G-
                    0x21,0x10,0x31,0xE0,0x19,0xF0,0x00,0x00,

                    0x3F,0xF0,0x3F,0xF0,0x02,0x00,0x02,0x00,  // -H-
                    0x02,0x00,0x3F,0xF0,0x3F,0xF0,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x20,0x10,0x3F,0xF0,  // -I-
                    0x3F,0xF0,0x20,0x10,0x00,0x00,0x00,0x00,

                    0x00,0xE0,0x00,0xF0,0x00,0x10,0x20,0x10,  // -J-
                    0x3F,0xF0,0x3F,0xE0,0x20,0x00,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x03,0x00,  // -K-
                    0x0F,0x80,0x3C,0xF0,0x30,0x70,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x20,0x10,  // -L-
                    0x00,0x10,0x00,0x30,0x00,0x70,0x00,0x00,

                    0x3F,0xF0,0x3F,0xF0,0x1C,0x00,0x0E,0x00,  // -M-
                    0x1C,0x00,0x3F,0xF0,0x3F,0xF0,0x00,0x00,

                    0x3F,0xF0,0x3F,0xF0,0x1C,0x00,0x0E,0x00,  // -N-
                    0x07,0x00,0x3F,0xF0,0x3F,0xF0,0x00,0x00,

                    0x0F,0xC0,0x1F,0xE0,0x30,0x30,0x20,0x10,  // -O-
                    0x30,0x30,0x1F,0xE0,0x0F,0xC0,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x22,0x10,  // -P-
                    0x22,0x00,0x3E,0x00,0x1C,0x00,0x00,0x00,

                    0x1F,0xE0,0x3F,0xF0,0x20,0x10,0x20,0x70,  // -Q-
                    0x20,0x3C,0x3F,0xFC,0x1F,0xE4,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x22,0x00,  // -R-
                    0x23,0x00,0x3F,0xF0,0x1C,0xF0,0x00,0x00,

                    0x18,0x60,0x3C,0x70,0x26,0x10,0x22,0x10,  // -S-
                    0x23,0x10,0x39,0xF0,0x18,0xE0,0x00,0x00,

                    0x00,0x00,0x38,0x00,0x30,0x10,0x3F,0xF0,  // -T-
                    0x3F,0xF0,0x30,0x10,0x38,0x00,0x00,0x00,

                    0x3F,0xE0,0x3F,0xF0,0x00,0x10,0x00,0x10,  // -U-
                    0x00,0x10,0x3F,0xF0,0x3F,0xE0,0x00,0x00,

                    0x3F,0x80,0x3F,0xC0,0x00,0x60,0x00,0x30,  // -V-
                    0x00,0x60,0x3F,0xC0,0x3F,0x80,0x00,0x00,

                    0x3F,0xC0,0x3F,0xF0,0x00,0x70,0x01,0xC0,  // -W-
                    0x00,0x70,0x3F,0xF0,0x3F,0xC0,0x00,0x00,

                    0x30,0x30,0x3C,0xF0,0x0F,0xC0,0x03,0x00,  // -X-
                    0x0F,0xC0,0x3C,0xF0,0x30,0x30,0x00,0x00,

                    0x00,0x00,0x3C,0x00,0x3E,0x10,0x03,0xF0,  // -Y-
                    0x03,0xF0,0x3E,0x10,0x3C,0x00,0x00,0x00,

                    0x38,0x70,0x30,0xF0,0x21,0x90,0x23,0x10,  // -Z-
                    0x26,0x10,0x3C,0x30,0x38,0x70,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x3F,0xF0,0x3F,0xF0,  // -[-
                    0x20,0x10,0x20,0x10,0x00,0x00,0x00,0x00,

                    0x1C,0x00,0x0E,0x00,0x07,0x00,0x03,0x80,  // -\-
                    0x01,0xC0,0x00,0xE0,0x00,0x70,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x20,0x10,0x20,0x10,  // -]-
                    0x3F,0xF0,0x3F,0xF0,0x00,0x00,0x00,0x00,

                    0x10,0x00,0x30,0x00,0x60,0x00,0xC0,0x00,  // -^-
                    0x60,0x00,0x30,0x00,0x10,0x00,0x00,0x00,

                    0x00,0x04,0x00,0x04,0x00,0x04,0x00,0x04,  // -_-
                    0x00,0x04,0x00,0x04,0x00,0x04,0x00,0x04,

                    0x00,0x00,0x00,0x00,0xC0,0x00,0xE0,0x00,  // -`-
                    0x20,0x00,0x00,0x00,0x00,0x00,0x00,0x00,

                    0x00,0xE0,0x05,0xF0,0x05,0x10,0x05,0x10,  // -a-
                    0x07,0xE0,0x03,0xF0,0x00,0x10,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xE0,0x04,0x10,  // -b-
                    0x06,0x10,0x03,0xF0,0x01,0xE0,0x00,0x00,

                    0x03,0xE0,0x07,0xF0,0x04,0x10,0x04,0x10,  // -c-
                    0x04,0x10,0x06,0x30,0x02,0x20,0x00,0x00,

                    0x01,0xE0,0x03,0xF0,0x06,0x10,0x24,0x10,  // -d-
                    0x3F,0xE0,0x3F,0xF0,0x00,0x10,0x00,0x00,

                    0x03,0xE0,0x07,0xF0,0x05,0x10,0x05,0x10,  // -e-
                    0x05,0x10,0x07,0x30,0x03,0x20,0x00,0x00,

                    0x02,0x10,0x1F,0xF0,0x3F,0xF0,0x22,0x10,  // -f-
                    0x30,0x00,0x18,0x00,0x00,0x00,0x00,0x00,

                    0x03,0xE4,0x07,0xF6,0x04,0x12,0x04,0x12,  // -g-
                    0x03,0xFE,0x07,0xFC,0x04,0x00,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x02,0x00,  // -h-
                    0x04,0x00,0x07,0xF0,0x03,0xF0,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x04,0x10,0x37,0xF0,  // -i-
                    0x37,0xF0,0x00,0x10,0x00,0x00,0x00,0x00,

                    0x00,0x00,0x00,0x0C,0x00,0x0E,0x00,0x02,  // -j-
                    0x04,0x02,0x37,0xFE,0x37,0xFC,0x00,0x00,

                    0x20,0x10,0x3F,0xF0,0x3F,0xF0,0x01,0x80,  // -k-
                    0x03,0xC0,0x06,0x70,0x04,0x30,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x20,0x10,0x3F,0xF0,  // -l-
                    0x3F,0xF0,0x00,0x10,0x00,0x00,0x00,0x00,

                    0x07,0xF0,0x07,0xF0,0x06,0x00,0x03,0xF0,  // -m-
                    0x06,0x00,0x07,0xF0,0x03,0xF0,0x00,0x00,

                    0x04,0x00,0x07,0xF0,0x03,0xF0,0x04,0x00,  // -n-
                    0x04,0x00,0x07,0xF0,0x03,0xF0,0x00,0x00,

                    0x03,0xE0,0x07,0xF0,0x04,0x10,0x04,0x10,  // -o-
                    0x04,0x10,0x07,0xF0,0x03,0xE0,0x00,0x00,

                    0x04,0x02,0x07,0xFE,0x03,0xFE,0x04,0x12,  // -p-
                    0x04,0x10,0x07,0xF0,0x03,0xE0,0x00,0x00,

                    0x03,0xE0,0x07,0xF0,0x04,0x10,0x04,0x12,  // -q-
                    0x03,0xFE,0x07,0xFE,0x04,0x02,0x00,0x00,

                    0x04,0x10,0x07,0xF0,0x03,0xF0,0x06,0x10,  // -r-
                    0x04,0x00,0x06,0x00,0x03,0x00,0x00,0x00,

                    0x02,0x20,0x07,0x30,0x05,0x90,0x04,0x90,  // -s-
                    0x04,0xD0,0x06,0x70,0x02,0x20,0x00,0x00,

                    0x04,0x00,0x04,0x00,0x1F,0xE0,0x3F,0xF0,  // -t-
                    0x04,0x10,0x04,0x30,0x00,0x20,0x00,0x00,

                    0x07,0xE0,0x07,0xF0,0x00,0x10,0x00,0x10,  // -u-
                    0x07,0xE0,0x07,0xF0,0x00,0x10,0x00,0x00,

                    0x00,0x00,0x07,0xC0,0x07,0xE0,0x00,0x30,  // -v-
                    0x00,0x30,0x07,0xE0,0x07,0xC0,0x00,0x00,

                    0x07,0xE0,0x07,0xF0,0x00,0x30,0x00,0xE0,  // -w-
                    0x00,0x30,0x07,0xF0,0x07,0xE0,0x00,0x00,

                    0x04,0x10,0x06,0x30,0x03,0xE0,0x01,0xC0,  // -x-
                    0x03,0xE0,0x06,0x30,0x04,0x10,0x00,0x00,

                    0x07,0xE2,0x07,0xF2,0x00,0x12,0x00,0x12,  // -y-
                    0x00,0x16,0x07,0xFC,0x07,0xF8,0x00,0x00,

                    0x06,0x30,0x06,0x70,0x04,0xD0,0x05,0x90,  // -z-
                    0x07,0x10,0x06,0x30,0x04,0x30,0x00,0x00,

                    0x00,0x00,0x02,0x00,0x02,0x00,0x1F,0xE0,  // -{-
                    0x3D,0xF0,0x20,0x10,0x20,0x10,0x00,0x00,

                    0x00,0x00,0x00,0x00,0x00,0x00,0x3D,0xF0,  // -|-
                    0x3D,0xF0,0x00,0x00,0x00,0x00,0x00,0x00,

                    0x00,0x00,0x20,0x10,0x20,0x10,0x3D,0xF0,  // -}-
                    0x1F,0xE0,0x02,0x00,0x02,0x00,0x00,0x00,

                    0x10,0x00,0x30,0x00,0x20,0x00,0x30,0x00,  // -~-
                    0x10,0x00,0x30,0x00,0x20,0x00,0x00,0x00,

                    0x01,0xE0,0x03,0xE0,0x06,0x20,0x0C,0x20,  // --
                    0x06,0x20,0x03,0xE0,0x01,0xE0,0x00,0x00,
            };
}
