package com.hwx.usbconnect.usbconncet.font;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hwx.usbconnect.usbconncet.R;

public class MySurfaceView extends SurfaceView {
	private Context mContext;
	private SurfaceHolder holder;

	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		holder = this.getHolder();
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		holder = this.getHolder();
	}

	public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		holder = this.getHolder();
	}

	/**
	 * show font
	 * 
	 * @param font_kind
	 *            the font type, has 16,24,32
	 * @param font
	 *            draw font
	 * @param startx
	 *            the font coordinate start x value
	 * @param starty
	 *            the font coordinate start y value
	 * @param beishu
	 *            the font magnification multiple
	 * @param type
	 *            draw font use icon,1 is flower,2 is love
	 */

	public void show_font16(int font_kind, String font, int startx, int starty, int beishu, int type) {
		boolean[][] arr = null;
		int weith = 16;
		int height = 16;
		if (font_kind == 16) {
			weith = 16;
			height = 16;
			arr = new boolean[weith][height];
			Font16 font16 = new Font16(mContext);
			arr = font16.drawString(font);
		} else if (font_kind == 24) {
			weith = 24;
			height = 24;
			arr = new boolean[weith][height];
			Font24 font24 = new Font24(mContext);
			arr = font24.drawString(font);
		} else {
			weith = 32;
			height = 32;
			arr = new boolean[weith][height];
			Font32 font32 = new Font32(mContext);
			arr = font32.drawString(font);
		}

		for (int i = 0; i < weith; i++) {
			for (int j = 0; j < height; j++) {
				try {
					Thread.sleep(25);
				} catch (InterruptedException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				float x = (float) j;
				float y = (float) i;
				if (arr[i][j]) {
					Bitmap bitmap = null;
					if (type == 1) {
						bitmap = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.ic_launcher));
					} else if (type == 2) {
						bitmap = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.ic_launcher));
					}
					int bw = bitmap.getWidth();
					int bh = bitmap.getHeight();
					synchronized (holder) {
						Canvas c = null;
						try {
							c = holder.lockCanvas(new Rect(startx + (int) x * beishu, starty + (int) y * beishu, startx + (int) x * beishu
									+ bw, starty + (int) y * beishu + bh));

							Paint p = new Paint();
							p.setColor(Color.RED);
							c.drawBitmap(bitmap, startx + x * beishu, starty + y * beishu, p);

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								if (c != null) {
									holder.unlockCanvasAndPost(c);// 结束锁定画图，并提交改变。
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

		}

	}

}
