package com.hwx.usbconnect.usbconncet.ui.brocast;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hwx.usbconnect.usbconncet.AppConfig;
import com.hwx.usbconnect.usbconncet.IBackService;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.hwx.usbconnect.usbconncet.MsgInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class TcpService extends Service {
	private static final String TAG = "TcpAIDLService";
	private static final long HEART_BEAT_RATE = 5 * 1000;

	public static final String HOST = "39.108.147.206";// "192.168.0.108";//116.62.11.36
	public static final int PORT = 2018;//80  7200

	public static final String MESSAGE_ACTION="message_ACTION";
	public static final String HEART_BEAT_ACTION="heart_beat_ACTION";

	private ReadThread mReadThread;

	private LocalBroadcastManager mLocalBroadcastManager;

	private WeakReference<Socket> mSocket;
	private static String[] colors = new String[]{"colormain1","colormain2","colormain3","colormain4","colormain5","colormain6","colormain7","blue_t","white_yellow","gold"};
	private static String[] icons = new String[]{"fa-street-view","fa-github-alt","fa-modx","fa-simplybuilt","fa-slideshare","fa-info-circle","fa-yelp","fa-meh-o","fa-apple","fa-android"};

	private boolean isConnect;

	// For heart Beat
	private Handler mHandler = new Handler();
	private Runnable heartBeatRunnable = new Runnable() {

		@Override
		public void run() {
			if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						//boolean isSuccess = sendMsg(BluetoothService.reData((byte) 0x08,new byte[]{(byte) 0x08,(byte) 0x08},true));//就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
						isConnect = sendMsg("^",true);
						LogUtils.e(isConnect?"tcp ok":"error tcp");
						if (!isConnect) {
							mHandler.removeCallbacks(heartBeatRunnable);
							mReadThread.release();
							releaseLastSocket(mSocket);
							new InitSocketThread().start();
						}
					}
				}).start();
			}
			mHandler.postDelayed(this, HEART_BEAT_RATE);
		}
	};


	private long sendTime = 0L;

	@Override
	public IBinder onBind(Intent arg0) {
		return new MsgBinder();
	}

	public class MsgBinder extends Binder{
		/**
		 * 获取当前Service的实例
		 * @return
		 */
		public TcpService getService(){
			return TcpService.this;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		releaseLastSocket(mSocket);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		new InitSocketThread().start();
		mLocalBroadcastManager= LocalBroadcastManager.getInstance(this);

	}
	public boolean sendMsg(byte[]  msg,boolean isTcp) {
		if (null == mSocket || null == mSocket.get()) {
			return false;
		}
		Socket soc = mSocket.get();
		if (soc==null)
			return false;
		try {
			if (!soc.isClosed() && !soc.isOutputShutdown()) {
				OutputStream os = soc.getOutputStream();
				os.write(msg);
				os.flush();
				sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (!isTcp)
			LogUtils.e("send byte[] ok:");
		return true;
	}
	public synchronized boolean sendMsg(String msg) {
		return sendMsg(msg.getBytes(),false);
	}
	public synchronized boolean sendMsg(String msg,boolean istcp) {
		return sendMsg(msg.getBytes(),istcp);
	}

	private void initSocket() {//初始化Socket
		try {
			Socket so = new Socket(HOST, PORT);
			mSocket = new WeakReference<Socket>(so);
			mReadThread = new ReadThread(so);
			mReadThread.start();
			mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			mHandler.postDelayed(heartBeatRunnable, 300);//初始化成功后，就准备发送心跳包
		}
	}

	private void releaseLastSocket(WeakReference<Socket> mSocket) {
		try {
			if (null != mSocket) {
				Socket sk = mSocket.get();
				if (!sk.isClosed()) {
					sk.close();
				}
				sk = null;
				mSocket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	class InitSocketThread extends Thread {
		@Override
		public void run() {
			super.run();
			initSocket();
		}
	}

	// Thread to read content from Socket
	class ReadThread extends Thread {
		private WeakReference<Socket> mWeakSocket;
		private boolean isStart = true;

		public ReadThread(Socket socket) {
			mWeakSocket = new WeakReference<Socket>(socket);
		}

		public void release() {
			isStart = false;
			releaseLastSocket(mWeakSocket);
		}

		@Override
		public void run() {
			super.run();
			Socket socket = mWeakSocket.get();
			if (null != socket) {
				try {
					InputStream is = socket.getInputStream();
					byte[] buffer = new byte[1024 * 1];
					int length = 0;
					while (!socket.isClosed() && !socket.isInputShutdown()
							&& isStart && ((length = is.read(buffer)) != -1)) {
						if (length > 0) {
							byte[] bytes= Arrays.copyOf(buffer,length);
							String message = new String(bytes).trim();
							Log.e(TAG, message);
							//收到服务器过来的消息，就通过Broadcast发送出去
							if(message.equals("ok")){//处理心跳回复
								Intent intent=new Intent(HEART_BEAT_ACTION);
								mLocalBroadcastManager.sendBroadcast(intent);
							}else{
								//其他消息回复
								int a = (int) (Math.random() * 10);
								if (message.contains(AppConfig.getInstance().getString("talk_name","^"))){
									message="{fa-street-view @color/"+colors[7]+" 18sp}\t\t"+message;
								}else if (message.trim().startsWith("服务器")||message.trim().startsWith("管理员")){
									message="{fa-reddit-alien @color/"+colors[a]+" 25sp}\t\t"+message;
								}else {
									message="{"+icons[a]+" @color/"+colors[a]+" 18sp}\t\t"+message;
								}
									Intent intent = new Intent(MESSAGE_ACTION);
									intent.putExtra("message", message);
									mLocalBroadcastManager.sendBroadcast(intent);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
