package com.hwx.usbconnect.usbconncet.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ftp.InstallUtils;
import com.hwx.usbconnect.usbconncet.ftp.MyAccessibilityService;
import com.hwx.usbconnect.usbconncet.ftp.ServiceUtils;
import com.hwx.usbconnect.usbconncet.ftp.UtilsFTP;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.InputStream;

public class FTPMainActivity extends Activity {

	UtilsFTP ftp = null;
	Button btn_connect;
	EditText editText_ip;
	EditText editText_loginname;
	EditText editText_pwd;

	TextView tv;

	Activity self;
	private File mLocalFile;
	private Thread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ftp_main);
		self = this;

		tv = (TextView) findViewById(R.id.tv);
		editText_ip = (EditText) findViewById(R.id.editText_ip);
		editText_loginname = (EditText) findViewById(R.id.editText_loginname);
		editText_pwd = (EditText) findViewById(R.id.editText_pwd);

		editText_ip.setText("39.108.147.206");
		editText_loginname.setText("");
		editText_pwd.setText("");

		btn_connect = (Button) findViewById(R.id.btn_connect);

		btn_connect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				connect();

			}
		});

//		findViewById(R.id.btn_testdownload).setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				new Thread() {
//
//					public void run() {
//
//						try {
//							UtilsFTP.IProgressListener listener = new UtilsFTP.IProgressListener() {
//								long BEG = 0;
//
//								@Override
//								public void onProgress(long len, long total) {
//									if (System.currentTimeMillis() - BEG > 200 || len == total) {
//										BEG = System.currentTimeMillis();
//										String result = String.format("%.2f", 100 * (double) len / (double) total);
//										showTV(result + "%");
//									}
//								}
//							};
//							String path = Environment.getExternalStorageDirectory().toString() + "/app-CIT.apk";
//							mLocalFile = new File(path);
//							ftp.downloadWithProgress("Latest/CIT/apk/app-CIT.apk", mLocalFile, listener);
//							toast("下载完毕");
////							Intent intent = new Intent(Intent.ACTION_VIEW);
////							intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
////							startActivity(intent);
//
//							if (!hasEnv()) {
//								Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//								startActivityForResult(intent, 0);
//							} else {
//								startInstall();
//							}
//						} catch (Exception e) {
//							// TODO: handle exception
//							toast(e.toString());
//						}
//					}
//
//
//				}.start();
//
//			}
//		});
		findViewById(R.id.btn_testupload).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread() {

					public void run() {

						try {

							UtilsFTP.IProgressListener listener = new UtilsFTP.IProgressListener() {
								long BEG = 0;

								@Override
								public void onProgress(long len, long total) {
									if (System.currentTimeMillis() - BEG > 200 || len == total) {
										BEG = System.currentTimeMillis();
										String result = String.format("%.2f", 100 * (double) len / (double) total);
										showTV(result + "%");
									}
								}
							};
							InputStream in2 = self.getResources().getAssets().open("a.mp3");
							ftp.uploadWithProgress("picture/test.mp3", in2, listener);
							toast("上传完毕");
						} catch (Exception e) {
							// TODO: handle exception
							toast(e.toString());
						}
					}

					;

				}.start();
			}
		});

		connect();
	}

	public void download(View view) {
		new Thread() {

			public void run() {

				try {
					UtilsFTP.IProgressListener listener = new UtilsFTP.IProgressListener() {
						long BEG = 0;

						@Override
						public void onProgress(long len, long total) {
							if (System.currentTimeMillis() - BEG > 200 || len == total) {
								BEG = System.currentTimeMillis();
								String result = String.format("%.2f", 100 * (double) len / (double) total);
								showTV(result + "%");
							}
						}
					};
					String path = Environment.getExternalStorageDirectory().toString() + "/test.bin";
					mLocalFile = new File(path);
					ftp.downloadWithProgress("/picture/test.bin", mLocalFile, listener);
                    FTPFile[] ftpFiles=ftp.getClient().listFiles("/picture");
					toast("下载完毕");
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
					startActivity(intent);

				} catch (Exception e) {
					// TODO: handle exception
					toast(e.toString());
				}
			}

			;

		}.start();
	}

	@Override
	protected void onDestroy() {
		if (mThread != null) {
			mThread.interrupt();
			mThread = null;
		}
		super.onDestroy();
	}

	private void connect() {
		final String IP = editText_ip.getText().toString();
		final String loginname = editText_loginname.getText().toString();
		final String pwd = editText_pwd.getText().toString();

		mThread = new Thread() {
			public void run() {
				ftp = new UtilsFTP(IP, 21, "anonymous", "anonymous", false);
				try {
					ftp.connect();
					toast("连接成功");
//					连接成功后发心跳包
//							int count = 0;
//							while (ftp.getClient().sendNoOp()) {
//								Thread.sleep(3000);
////								toast("心跳" + String.valueOf(count));
//								count++;
//							}
//					ftp.disconnect();
//					toast("断开成功");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					toast(e.toString());
					e.printStackTrace();
				}
			}

		};
		mThread.start();
	}

	public static final String TAG = FTPMainActivity.class.getSimpleName();

	private void startInstall() {
		new Thread() {
			@Override
			public void run() {
//				final String apk = FileUtils.getApk(FTPMainActivity.this);
				if (mLocalFile == null) {
					String path = Environment.getExternalStorageDirectory().toString() + "/app-CIT.apk";
					mLocalFile = new File(path);
				}
				final String apk = mLocalFile.getAbsolutePath();
				String appName = apk.substring(apk.lastIndexOf("/") + 1, apk.lastIndexOf("."));
				Log.i(TAG, "run: " + appName);
				MyAccessibilityService.addInstalledWhitelList(InstallUtils.getAppNameByReflection(FTPMainActivity.this, apk));
				InstallUtils.installNormal(FTPMainActivity.this, apk);
			}
		}.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && hasEnv()) {
			startInstall();
		}
	}

	private boolean hasEnv() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
				&& ServiceUtils.isAccessibilitySettingsOn(FTPMainActivity.this);
	}

	private final static int CMD_TOAST_INFO = 0;
	private final static int CMD_TEXTVIEW_INFO = 1;

	private void toast(String info) {
		Message msg = commonhandler.obtainMessage();
		msg.what = CMD_TOAST_INFO;
		msg.obj = info;
		commonhandler.sendMessage(msg);
	}

	private void showTV(String info) {
		Message msg = commonhandler.obtainMessage();
		msg.what = CMD_TEXTVIEW_INFO;
		msg.obj = info;
		commonhandler.sendMessage(msg);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			android.os.Process.killProcess(android.os.Process.myPid());
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler commonhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case CMD_TOAST_INFO:
					Toast.makeText(FTPMainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
					break;
				case CMD_TEXTVIEW_INFO:
					tv.setText(msg.obj.toString());
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};
}
