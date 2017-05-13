package com.hwx.usbconnect.usbconncet.font;

import android.content.Context;

import java.io.InputStream;

public class Font32 {
	private Context context;

	public Font32(Context context) {
		this.context = context;
	}

	private final static String ENCODE = "GB2312";
	private final static String ZK32 = "HZK32F";

	private boolean[][] arr;

	public boolean[][] drawString(String str) {
		byte[] data = null;
		int[] code = null;
		int byteCount;
		int lCount;

		arr = new boolean[32][32];
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) < 0x80) {
				continue;
			}
			code = getByteCode(str.substring(i, i + 1));
			data = read(code[0], code[1]);
			byteCount = 0;
			for (int line = 0; line < 32; line++) {
				lCount = 0;
				for (int k = 0; k < 4; k++) {
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

	protected byte[] read(int areaCode, int posCode) {
		byte[] data = null;
		try {
			int area = areaCode - 0xa0;
			int pos = posCode - 0xa0;

			InputStream in = context.getResources().getAssets().open(ZK32);
			long offset = 128 * ((area - 1) * 94 + pos - 1);
			in.skip(offset);
			data = new byte[128];
			in.read(data, 0, 128);
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
