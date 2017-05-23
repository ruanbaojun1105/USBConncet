package com.hwx.usbconnect.usbconncet.font;

import android.content.Context;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class FontUtilsTest {
    private Context context;
    /*
     * 字库名
     */
    private static String dotMatrixFont = "GBK16.DZK";
    /*
     * 编码 GB2312
     */
    private final static String ENCODE = "GBK";

    /*
     * 16X16的字库用16个点表示
     */
    private int dots = 12;

    /*
     * 一个字用点表示需要多少字节，16X16的字体需要32个字节
     */
    private int wordByteByDots = 32;//12*12/24----16*16/32
    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
    public FontUtilsTest(Context context) {
        this.context = context;  
    }
    //12*12
    public byte[] getWordsGen(String str) {
        byte[] res=new byte[0];
        char[] aa=str.toCharArray();
        int[] byteCode = new int[2];
        for (int i = 0; i <aa.length ; i++) {
            byte[] data = new byte[0];
            try {
                data = String.valueOf(aa[i]).getBytes(ENCODE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            res=byteMerger(res,data);
                if (data.length==2){
                    // 当成无符号对待
                    byteCode[0] = data[0] < 0 ? 256 + data[0] : data[0];
                    byteCode[1] = data[1] < 0 ? 256 + data[1] : data[1];
                    byte[] tag = read(byteCode[0], byteCode[1]);
                    res=byteMerger(res,tag);
                }
        }
        return res;
    }

    /** 
     * 获取字符串的点阵矩阵 
     * @param str 
     * @return 
     */  
    public boolean[][] getWordsInfo(String str) {  
        byte[] dataBytes = null;  
        try {  
            dataBytes = str.getBytes(ENCODE);  
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  
        }  
        // 汉字对应的byte数组  
        int[] byteCode = new int[dataBytes.length];  
        // 当成无符号对待  
        for (int i = 0; i < dataBytes.length; i++) {  
            // 根据每两个byte数组计算一个汉字的相对位置  
            byteCode[i] = dataBytes[i] < 0 ? 256 + dataBytes[i] : dataBytes[i];
        }  
        // 用来存放所有汉字对应的字模信息  
        // 一个汉字24 byte
        int wordNums = byteCode.length/2;  
        boolean[][] matrix = new boolean[dots][wordNums * dots];  
        byte [] dataResult = new byte[wordNums * wordByteByDots];  
        for (int i = 0, numIndex = 0; i < byteCode.length; i += 2, numIndex++) {  
            // 依次读取到这个汉字对应的24位的字模信息
            byte[] data = read(byteCode[i], byteCode[i+1]);
            System.arraycopy(data, 0, dataResult, numIndex * data.length, data.length);  
        }
        for (int num = 0; num < wordNums; num++) {
            for (int i = 0; i < 18; i++) {
                for (int j1 = 0; j1 < 8; j1++) {
                    // 对每个字节进行解析
                    byte tmp = dataResult[num * wordByteByDots + i * 2 + j1];
                    for (int j2 = 0; j2 < 8; j2++) {
                        int aa=j1 * 8 + j2;
                        if (aa>=24){
                            continue;
                        }
                        if (((tmp >> (7 - j2)) & 1) == 1) {
                            matrix[i][aa] = true;
                        } else {
                            matrix[i][aa] = false;
                        }
                    }
                }
            }
        }
        return matrix;  
    }  
  
    /** 
     * 获取一个汉字的点阵数组，开始测试代码时用。。 
     * @param str
     * @return 
     */  
    @SuppressWarnings("unused")  
    public boolean[][] getWordInfo(String str) {
        boolean[][] matrix = new boolean[12][12];
        int[] byteCode = new int[2];  
        try {  
            byte[] data = str.getBytes(ENCODE);  
            // 当成无符号对待  
            byteCode[0] = data[0] < 0 ? 256 + data[0] : data[0];
            byteCode[1] = data[1] < 0 ? 256 + data[1] : data[1];
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        // 读取到这个汉子对应的24位的字模信息
        byte[] data = read(byteCode[0], byteCode[1]);
        for (int i = 0; i < 12; i++) {//24::::0/1  15/3
            // 对每个字节进行解析
            byte tmp = data[i];
            byte[]tag=getBooleanArray(tmp);
            for (int j = 0; j < tag.length; j++) {
                if (((tmp >> (7 - j)) & 1) == 1) {
                    matrix[i][j] = true;
                } else {
                    matrix[i][j] = false;
                }
            }
        }
        return matrix;  
    }
    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte)(b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }
    /** 
     * 从字库中找到这个汉字的字模信息 
     * @param areaCode 区码，对应编码的第一个字节 
     * @param posCode 位码，对应编码的第二个字节 
     * @return 12*12
     */  
    protected byte[] read(int areaCode, int posCode) {
        byte[] data = null;  
        try {  
            int area = areaCode - 0x81;
            int pos = posCode - (posCode<0x7f?0x40:0x41);
            InputStream in = context.getResources().getAssets().open(dotMatrixFont);
            long offset = wordByteByDots * (area * 190 + pos);
            in.skip(offset);  
            data = new byte[wordByteByDots];  
            in.read(data, 0, wordByteByDots);  
            in.close();  
        } catch (Exception ex) {  
        }  
        return data;  
    }  
  
}  