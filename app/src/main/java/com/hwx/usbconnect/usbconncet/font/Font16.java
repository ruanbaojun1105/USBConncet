package com.hwx.usbconnect.usbconncet.font;

import android.content.Context;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Font16 {
	private Context context;

	public Font16(Context context) {
		this.context = context;
	}

	private final static String ENCODE = "GB2312";
	private final static String ZK16 = "HZK16";

	private boolean[][] arr;
	int all_16_32 = 16;
	int all_2_4 = 2;
	int all_32_128 = 32;

	public boolean[][] drawString(String str) {
		byte[] data = null;
		int[] code = null;
		int byteCount;
		int lCount;

		arr = new boolean[all_16_32][all_16_32];
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) < 0x80) {
				continue;
			}
			code = getByteCode(str.substring(i, i + 1));
			data = read(code[0], code[1]);
			byteCount = 0;
			for (int line = 0; line < all_16_32; line++) {
				lCount = 0;
				for (int k = 0; k < all_2_4; k++) {
					for (int j = 0; j < 8; j++) {
						if (((data[byteCount] >> (7 - j)) & 0x1) == 1) {
							arr[line][lCount] = true;
							System.out.print("@");
						} else {
							System.out.print(" ");
							arr[line][lCount] = false;
						}
						lCount++;
					}
					byteCount++;
				}
				System.out.println();
			}
		}
		return arr;
	}

	public byte[] getStringFontByte(String str) {
		int[] code = null;
		byte[] res=new byte[0];

		for (int i = 0; i < str.length(); i++) {
			try {
				byte[] data=str.substring(i, i + 1).getBytes(ENCODE);
				res=byteMerger(res,data);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (str.charAt(i) < 0x80) {
				continue;
			}
			code = getByteCode(str.substring(i, i + 1));
			byte[] tag = read16_DZK(code[0], code[1]);
			res=byteMerger(res,tag);
		}
		return res;
	}
	private static String dotMatrixFont = "GBK16.DZK";
	private int wordByteByDots = 32;//12*12/24----16*16/32
	protected byte[] read16_DZK(int areaCode, int posCode) {
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

	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
		byte[] byte_3 = new byte[byte_1.length+byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	/**
	 *
	 * @param areaCode
	 * @param posCode
	 * @return
	 */
	protected byte[] read(int areaCode, int posCode) {
		byte[] data = null;
		try {
			int area = areaCode - 0xa0;
			int pos = posCode - 0xa0;

			InputStream in = context.getResources().getAssets().open(ZK16);
			long offset = all_32_128 * ((area - 1) * 94 + pos - 1);
			in.skip(offset);
			data = new byte[all_32_128];
			in.read(data, 0, all_32_128);
			in.close();
		} catch (Exception ex) {
		}
		return data;
	}

	protected int[] getByteCode(String str) {
		int[] byteCode = new int[2];
		try {
			byte[] data = str.getBytes(ENCODE);
			byteCode[0] = data[0] < 0 ? 256 + data[0] : data[0];
			byteCode[1] = data[1] < 0 ? 256 + data[1] : data[1];
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return byteCode;
	}

}
