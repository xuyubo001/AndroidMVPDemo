package com.nsu.edu.androidmvpdemo.net;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nsu.edu.androidmvpdemo.net.net.IHttpCallback;
import com.nsu.edu.androidmvpdemo.net.net.NetManager;
import com.nsu.edu.androidmvpdemo.net.tools.Logs;
import com.nsu.edu.androidmvpdemo.utils.UrlPath;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2015/10/19 0019.
 */
public class HttpRequest {
	public static final String TAG = "HttpRequest";
	public static final int DONE = 9993;
	public static final int UPDATE = 9994;
	public static final int UPLOAD_FAIL = 9996;
	public static final int UPLOAD_SUCCESS = 9995;
	public static final int UPLOAD_SUCCESS_QUESTION = 9994;
	public static final int INITDATA = 9997;
	public static final int SUCCESS = 9998;
	public static final int ERROR = 9999;
	public static final int UPDATE_RESULTID = 9992;
	private static List<String> waitDownLoadId;
	private static int nowDownLoadNo = 0;


	/**
	 * 传给服务器客户端的CID 用于推送
	 *
	 * @param teacherId
	 *            上下文
	 */
	public static void updateClientId(String teacherId) {
		Log.d(TAG,"updateClientId    "+teacherId);
		NetManager netManager = new NetManager();
		List<NameValuePair> params = new ArrayList<>();

		params.add(new BasicNameValuePair("teacherId",teacherId));

		netManager.httpPost(UrlPath.UPDATE_CLIENT_ID(), null, params, new IHttpCallback() {
			@Override
			public void onStart() {


			}

			@Override
			public void onSuccess(String msg) {
               Logs.d(TAG,"updateClientId onSuccess");

			}

			@Override
			public void onFailure(String err) {


			}

			@Override
			public void onProgress(long progress, long maxValue) {

			}
		});
	}




	






}
