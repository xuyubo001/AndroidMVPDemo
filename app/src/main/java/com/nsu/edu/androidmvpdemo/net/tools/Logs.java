package com.nsu.edu.androidmvpdemo.net.tools;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;
@SuppressWarnings("unused")
public class Logs {
	
	private static final int VERBOSE = 2;
	private static final int DEBUG = 3;
	private static final int INFO = 4;
	private static final int WARN = 5;
	private static final int ERROR = 6;
	private static final int ASSERT = 7;
	public static String logsFile;
	


	public static synchronized void v(String tag, String msg) {
		Log.v(tag, msg);
	}

	public static synchronized void v(String tag, String msg, Throwable tr) {
		Log.v(tag, msg, tr);
	}

	public static synchronized void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static synchronized void d(String tag, String msg, Throwable tr) {
		Log.d(tag, msg, tr);
	}

	public static synchronized void i(String tag, String msg) {
		Log.i(tag, msg);
	}

	public static synchronized void i(String tag, String msg, Throwable tr) {
		Log.i(tag, msg, tr);
	}

	public static synchronized void w(String tag, String msg) {
		Log.w(tag, msg);
	}

	public static synchronized void w(String tag, String msg, Throwable tr) {
		Log.w(tag, msg, tr);
	}

	public static synchronized void w(String tag, Throwable tr) {
		Log.w(tag, tr);
	}

	public static synchronized void e(String tag, String msg) {
		Log.e(tag, msg);
		
		logsFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "error/homework";

		File file = new File(logsFile);
		if (!file.exists()) {
			file.mkdirs();
		}
		FileOutputStream outputStream = null;
		try {
			// 判断有无SD卡——即SD卡是否处于挂起(MEDIA_MOUNTED),如果有就存储到SD卡中
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				
				File real_path = new File(file, getDate()+".txt");
				outputStream = new FileOutputStream(real_path.getAbsolutePath(), true);
				//byte[]data=(tag+":"+msg+"\n").getBytes();
				String newRow = new String(new byte[]{0x0d, 0x0a}, "utf-8");
				Date date=new Date();//如果不需要格式,可直接用date,date就是当前系统时间
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//设置显示格式
				//用DateFormat的format()方法在dt中获取并以yyyy/MM/dd HH:mm:ss格式显示
				String dateTime =df.format(date);
				tag = dateTime + newRow + tag;
				String printStr = tag + ":" + msg + "\n" + newRow;
				byte[] data = printStr.getBytes("utf-8");
				// 将数据写入到流中
				outputStream.write(data, 0, data.length);
				// 返回文件的绝度路径
			} else {
				// 存储到内部存储中
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	public static synchronized void e(String tag, String msg, Throwable tr) {
		Log.e(tag, msg, tr);
		logsFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "error/homework";

		File file = new File(logsFile);
		if (!file.exists()) {
			file.mkdirs();
		}
		FileOutputStream outputStream = null;
		try {
			// 判断有无SD卡——即SD卡是否处于挂起(MEDIA_MOUNTED),如果有就存储到SD卡中
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File real_path = new File(file, getDate()+".txt");
				outputStream = new FileOutputStream(real_path.getAbsolutePath(), true);
				//byte[]data=(tag+":"+msg+"\n").getBytes();
				String newRow = new String(new byte[]{0x0d, 0x0a}, "utf-8");
				Date date=new Date();//如果不需要格式,可直接用date,date就是当前系统时间
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//设置显示格式
				//用DateFormat的format()方法在dt中获取并以yyyy/MM/dd HH:mm:ss格式显示
				String dateTime =df.format(date);
				tag = dateTime + newRow + tag;
				String printStr = tag + ":" + msg + "\n" + newRow;
				// 将数据写入到流中
				byte[] data = (tag+":"+msg+getStackTraceString(tr)+"\n").getBytes("utf-8");
				// 将数据写入到流中
				outputStream.write(data, 0, data.length);
				outputStream.flush();
				outputStream.close();
				// 返回文件的绝度路径
			} else {
				// 存储到内部存储中
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}

		// This is to reduce the amount of log spew that apps do in the
		// non-error
		// condition of the network being unavailable.
		Throwable t = tr;
		while (t != null) {
			if (t instanceof UnknownHostException) {
				return "";
			}
			t = t.getCause();
		}

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}

	public static int println(int priority, String tag, String msg) {
		return println(LOG_ID_MAIN, priority, tag, msg);
	}

	/** @hide */
	public static final int LOG_ID_MAIN = 0;
	/** @hide */
	public static final int LOG_ID_RADIO = 1;
	/** @hide */
	public static final int LOG_ID_EVENTS = 2;
	/** @hide */
	public static final int LOG_ID_SYSTEM = 3;
	/** @hide */
	public static final int LOG_ID_CRASH = 4;

	public static int println(int bufID, int priority, String tag, String msg) {
		return 0;
	}
	
	/**
	 * 获取系统时间
	 */
	private static String getDate(){
		Date date=new Date();//如果不需要格式,可直接用date,date就是当前系统时间
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//设置显示格式
		String nowTime="";
		nowTime= df.format(date);//用DateFormat的format()方法在dt中获取并以yyyy/MM/dd HH:mm:ss格式显示
		return nowTime;
	}
}
