package com.nsu.edu.androidmvpdemo.net.tools;

public class TimeUtils {

	public static String TAG = TimeUtils.class.getSimpleName();
	
	public static void sleep(long ms){
		//TODO 添加出错处理
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Logs.e(TAG, "Sleep failed: "+e.toString());
		}		
	}
}
