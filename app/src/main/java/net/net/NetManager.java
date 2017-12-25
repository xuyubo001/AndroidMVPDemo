package net.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.net.http.HttpManager;
import net.tools.Logs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class NetManager {
	public static Context context;
	private IHttpCallback httpCallback = null;
	private static String TAG = NetManager.class.getSimpleName();
	private static SessionFailCallback sessionFailCallback = null;

	public void httpGet(String uri, IHttpCallback cb) {
		// httpGet
		if (true == NetThreadsManager.hasURI(uri)) {
			return;
		}
		NetThreadsManager.addURI(uri);
		httpCallback = cb;
		HttpGetThread httpGet = new HttpGetThread();
		httpGet.uri = uri;
		httpGet.start();

	}

	class HttpGetThread extends Thread {
		public String uri = null;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			Message msgStart = new Message();
			msgStart.what = MsgType.MSG_TYPE_HTTP_START;
			mHandler.sendMessage(msgStart);

			HttpManager httpManager = new HttpManager();
			Message msg = new Message();
			try {

				String result = httpManager.httpGet(uri);
				if (null == result) {
					msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
				} else {

					if (!TextUtils.isEmpty(result)
							&& result.contains("HOMEWORKAPP_ERROR")) {
						Logs.e(TAG, "HOMEWORKAPP_ERROR");
						msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
					} else {
						msg.what = MsgType.MSG_TYPE_HTTP_SUCCESS;
						Bundle data = new Bundle();
						data.putString("data", result);
						data.putString("source", uri);
						msg.setData(data);
					}

				}
				NetThreadsManager.delURI(uri);
				mHandler.sendMessage(msg);
			} catch (Exception e) {
				if (null != e) {
					Logs.e(TAG, e.toString());
				}
				msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
				mHandler.sendMessage(msg);
			}

		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MsgType.MSG_TYPE_HTTP_START:
				httpCallback.onStart();
				break;
			case MsgType.MSG_TYPE_HTTP_SUCCESS:
				String result = msg.getData().getString("data");
				httpCallback.onSuccess(result);
				break;
			case MsgType.MSG_TYPE_HTTP_FAILURE:
				httpCallback.onFailure(null);
				break;
			case MsgType.MSG_TYPE_HTTP_PROGRESS:
				httpCallback.onProgress(msg.getData().getLong("progress"), msg
						.getData().getLong("maxvalue"));
				break;
			default:
				break;
			}

		}
	};

	public void httpPost(String uri, String data, List<NameValuePair> params,
			boolean openIdenticalUrlRequest, IHttpCallback cb) {
		// httpPost
		if (!openIdenticalUrlRequest) {
			if (true == NetThreadsManager.hasURI(uri)) {
				return;
			}
			NetThreadsManager.addURI(uri);
		}
		httpCallback = cb;
		HttpPostThread httpPost = new HttpPostThread();
		httpPost.uri = uri;
		httpPost.params = params;
		httpPost.data = data;
		httpPost.start();
	}

	// httpPost
	public void httpPost(String uri, String data, List<NameValuePair> params,
			IHttpCallback cb) {
		httpPost(uri, data, params, false, cb);
	}

	public void httpPost(String uri, String data, List<NameValuePair> params,
			List<String> files, IHttpCallback cb) {

		if (true == NetThreadsManager.hasURI(uri)) {
			return;
		}
		NetThreadsManager.addURI(uri);

		httpCallback = cb;
		HttpPostThread httpPost = new HttpPostThread();
		httpPost.uri = uri;
		httpPost.params = params;
		httpPost.data = data;
		httpPost.files = files;
		httpPost.start();
	}

	class HttpPostThread extends Thread {
		public String uri = null;
		List<NameValuePair> params = null;
		List<String> files = null;
		public String data = null;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			Message msgStart = new Message();
			msgStart.what = MsgType.MSG_TYPE_HTTP_START;
			mHandler.sendMessage(msgStart);

			HttpManager httpManager = new HttpManager();
			Message msg = new Message();
			NetResult nr = httpManager.httpPost(uri, data, params, files);

			if (null == nr) {
				msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
			} else {
				if (nr.getStatus() == 200) {
					if (!TextUtils.isEmpty(nr.getResult())
							&& nr.getResult().contains("HOMEWORKAPP_ERROR")) {
						Logs.e(TAG, "HOMEWORKAPP_ERROR");
						msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
					} else {
						msg.what = MsgType.MSG_TYPE_HTTP_SUCCESS;
						Logs.d(TAG, "status: " + nr.getStatus() + "Result:"
								+ nr.getResult());
					}
				} else {
					msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
					Logs.e(TAG, "URL:" + uri);
					Logs.e(TAG,
							"status: " + nr.getStatus() + "Result:"
									+ nr.getResult());
				}
				Bundle data = new Bundle();
				data.putString("data", nr.getResult());
				data.putString("source", uri);
				msg.setData(data);
			}
			NetThreadsManager.delURI(uri);
			mHandler.sendMessage(msg);
		}
	}

	// download
	public void download(String uri, String filename, IHttpCallback cb) {
		// httpGet
		if (true == NetThreadsManager.hasURI(uri)) {
			return;
		}
		NetThreadsManager.addURI(uri);
		httpCallback = cb;
		DownloadThread download = new DownloadThread();
		download.uri = uri;
		download.filename = filename;
		download.start();

	}

	class DownloadThread extends Thread {
		public String uri = null;
		public String filename = null;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			Message msgStart = new Message();
			msgStart.what = MsgType.MSG_TYPE_HTTP_START;
			mHandler.sendMessage(msgStart);

			HttpManager httpManager = new HttpManager();
			Message msg = new Message();
			boolean isSuccess = false;
			try {
				isSuccess = httpManager.download(uri, filename, mHandler);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logs.d(TAG, "下载文件IO错误: " + e.toString());
			}
			if (isSuccess == false) {
				msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
			} else {
				msg.what = MsgType.MSG_TYPE_HTTP_SUCCESS;
				Bundle data = new Bundle();
				data.putString("data", "true");
				data.putString("source", uri);
				msg.setData(data);
			}
			NetThreadsManager.delURI(uri);
			mHandler.sendMessage(msg);
		}
	}

	// download
	public void upload(String uri, String filename, IHttpCallback cb) {

		if (true == NetThreadsManager.hasURI(uri)) {
			return;
		}
		NetThreadsManager.addURI(uri);
		httpCallback = cb;
		UploadThread upload = new UploadThread();
		upload.uri = uri;
		upload.filename = filename;
		upload.start();

	}

	class UploadThread extends Thread {
		public String uri = null;
		public String filename = null;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			Message msgStart = new Message();
			msgStart.what = MsgType.MSG_TYPE_HTTP_START;
			mHandler.sendMessage(msgStart);

			HttpManager httpManager = new HttpManager();
			Message msg = new Message();
			boolean isSuccess = httpManager.upload(uri, filename);
			if (isSuccess == false) {
				msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
			} else {
				msg.what = MsgType.MSG_TYPE_HTTP_SUCCESS;
				Bundle data = new Bundle();
				data.putString("data", "true");
				data.putString("source", uri);
				msg.setData(data);
			}
			NetThreadsManager.delURI(uri);
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 用于集中处理所有请求中 请求成功时的错误
	 * 
	 * @param callback
	 */
	public static void setSessionFailCallback(SessionFailCallback callback) {
		sessionFailCallback = callback;
	}

	/**
	 * 清空当前session
	 */
	public static void clearSession() {
		HttpManager.sessionID = null;
	}
}
