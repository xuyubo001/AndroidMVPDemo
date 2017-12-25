package net.net.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;//in httpcore
import org.json.JSONException;
import org.json.JSONObject;

import net.io.ProgressInputStream;
import net.io.ProgressListener;
import net.net.MsgType;
import net.net.NetResult;
import net.tools.Logs;
import net.tools.TimeUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpManager {

	public int MAXRETRY = 1;
	public int retry = 0;
	public String TAG = this.getClass().getSimpleName();
	public int SO_TIMEOUT = 60000;
	public int CONNECTION_TIMEOUT = 60000;
	public static String sessionID = null;

	@SuppressLint("NewApi")
	public String httpGet(String uri) {
		int i = 0;
		Logs.i(TAG, "已经进入httpGet函数");
		if ((null == uri) || (uri.isEmpty())) {
			Logs.w(TAG, "URI is null");
			return null;
		}
		Logs.i(TAG, "http get uri: " + uri);

		StringBuffer buffer = new StringBuffer();
		for (i = 0; i < MAXRETRY; i++) {
			buffer.setLength(0);
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpclient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							CONNECTION_TIMEOUT);
			HttpGet httpget = new HttpGet(uri);
			httpget.addHeader("clientType", "1");
			if ((null != sessionID) && (false == sessionID.isEmpty())) {
				httpget.setHeader("Cookie", sessionID);
				Logs.w(TAG, "sessionId：" + sessionID);
			}

			try {
				HttpResponse response = httpclient.execute(httpget);
				int status = response.getStatusLine().getStatusCode();
				Logs.d(TAG, "第" + i + "次尝试从服务器获取数据。");
				if (200 != status) {
					// TODO 添加出错处理
					Logs.e(TAG, "GET返回错误码: " + status);
					Logs.e(TAG, "错误信息: " + response.getEntity().toString());
					TimeUtils.sleep(3000);
					continue;
				}
				if ((null == sessionID) || (true == sessionID.isEmpty())) {
					if (true == response.containsHeader("Set-Cookie")) {
						sessionID = response.getFirstHeader("Set-Cookie")
								.getValue().split(";")[0];
						Logs.w(TAG, "sessionId：" + sessionID);
					}
				}
				//
				HttpEntity httpEntity = response.getEntity();
				if (null != httpEntity) {
					BufferedReader in = new BufferedReader(
							new InputStreamReader(httpEntity.getContent(),
									"UTF-8"), 8 * 1024);

					String line = "";
					while ((line = in.readLine()) != null) {
						buffer.append(line);
					}
				}
				Logs.d(TAG, buffer.toString());
				// 此处处理接收到的数据
				break;

			} catch (ClientProtocolException e) {

				Logs.e(TAG, "客户端协议异常: " + e.toString());
				TimeUtils.sleep(1000);
			} catch (IOException e) {

				Logs.e(TAG, "IO异常: " + e.toString());
				TimeUtils.sleep(1000);
			} finally {
				// 关闭连接
				httpget.abort();
				httpclient.getConnectionManager().shutdown();
			}
		}
		if (i >= 1) {
			Logs.w(TAG, "尝试1次从服务器获取数据，均失败，请检查网络。");
			return null;
		} else {
			return buffer.toString();
		}
	}

	@SuppressLint("NewApi")
	public NetResult httpPost(String uri, String data,
			List<NameValuePair> params) {
		int i = 0;
		String result = "";
		if ((null == uri) || (uri.isEmpty())) {
			Logs.w(TAG, "URI is null");
			return null;
		}
		NetResult nr = new NetResult();
		for (i = 0; i < MAXRETRY; i++) {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpclient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							CONNECTION_TIMEOUT);

			HttpPost httppost = new HttpPost(uri);
			httppost.addHeader("clientType", "1");
			httppost.getParams().setParameter("charset", "utf-8");
			if ((null != sessionID) && (false == sessionID.isEmpty())) {
				httppost.setHeader("Cookie", sessionID);
				Logs.e("sessionID", sessionID);
			}
			try {
				// 如果第二个参数不为null或""，将第二个参数设置为entity
				if (null != data && data.trim().length() > 0) {
					// 向服务器写json
					StringEntity se = new StringEntity(data + "\r\n\r\n",
							"utf-8");
					httppost.setHeader(HTTP.CONTENT_TYPE,
							"application/json;charset=utf-8");
					httppost.setHeader("Accept", "*/*");
					httppost.setEntity(se);
				} else {// 如果第二个参数为空，判断第三个参数
					if ((null == params) || (params.isEmpty())) {
						Logs.w(TAG, "params is null");
					} else {// 如果第三个参数不为空，将第三个参数设置为entity
						httppost.setEntity(new UrlEncodedFormEntity(params,
								HTTP.UTF_8));
					}

				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				Logs.e(TAG, "编码异常: " + e1.toString());
				return null;
			}
			try {

				HttpResponse response = httpclient.execute(httppost);
				int status = response.getStatusLine().getStatusCode();
				Logs.d(TAG, "第" + i + "次尝试发送数据到服务器。");
				if ((null == sessionID) || (true == sessionID.isEmpty())) {
					if (true == response.containsHeader("Set-Cookie")) {
						sessionID = response.getFirstHeader("Set-Cookie")
								.getValue().split(";")[0];
					}
				}
				//
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity);// 取出应答字符串
				nr.setStatus(status);
				nr.setResult(result);
				break;

			} catch (ClientProtocolException e) {
				Logs.e(TAG, "客户端协议异常: " + e.toString());
				TimeUtils.sleep(1000);
			} catch (IOException e) {
				Logs.e(TAG, "IO异常: " + e.toString());
				TimeUtils.sleep(1000);

			} catch (Exception ex) {
				Logs.e(TAG, "异常: " + ex.toString());
				TimeUtils.sleep(1000);
			} finally {
				// 关闭连接
				httppost.abort();
				httpclient.getConnectionManager().shutdown();
			}
		}
		if (i >= 3) {
			Logs.w(TAG, "尝试3次从服务器获取数据，均失败，请检查网络。");
			return null;
		}
		return nr;

	}

	/**
	 * 需要参数的上传文件接口
	 * 
	 * @param uri
	 * @param data
	 * @param params
	 * @param files
	 * @return
	 */
	@SuppressLint("NewApi")
	public NetResult httpPost(String uri, String data,
			List<NameValuePair> params, List<String> files) {
		int i = 0;
		String result = "";
		if ((null == uri) || (uri.isEmpty())) {
			Logs.w(TAG, "URI is null");
			return null;
		}
		NetResult nr = new NetResult();
		for (i = 0; i < MAXRETRY; i++) {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpclient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							CONNECTION_TIMEOUT);
			HttpPost httppost = new HttpPost(uri);
			httppost.addHeader("clientType", "1");
			if ((null != sessionID) && (false == sessionID.isEmpty())) {
				httppost.setHeader("Cookie", sessionID);
				Logs.e("sessionID", sessionID);
			}
			MultipartEntityBuilder mEntityBuilder = null;
			boolean isMulti = false;
			if (((data != null) && (params != null))
					|| ((data != null) && (files != null))
					|| ((params != null) && (files != null))
					|| ((files != null) && (files.size() > 1))) {
				// 多个文件
				mEntityBuilder = MultipartEntityBuilder.create();
				mEntityBuilder.setCharset(Charset.forName("UTF-8"));
				isMulti = true;
			}

			try {
				// 如果第二个参数不为null或""，将第二个参数设置为entity
				if (null != data && data.trim().length() > 0) {
					// 向服务器写json
					if (false == isMulti) {
						StringEntity se = new StringEntity(data + "\r\n\r\n",
								"UTF-8");
						Logs.d(TAG, "JSON:" + data + "\r\n\r\n");
						httppost.setHeader(HTTP.CONTENT_TYPE,
								"application/json;charset=utf-8");
						httppost.setHeader("Accept", "*/*");
						httppost.setEntity(se);
					} else {
						StringBody strBody = new StringBody(data + "\r\n",
								ContentType.create("application/json", "UTF-8"));
						mEntityBuilder.addPart("JSON", strBody);
					}
				}
				// 如果第二个参数为空，判断第三个参数
				if ((null == params) || (params.isEmpty())) {
					Logs.w(TAG, "params is null");
				} else {// 如果第三个参数不为空，将第三个参数设置为entity
					StringEntity se = new UrlEncodedFormEntity(params,
							HTTP.UTF_8);

					Logs.d(TAG, uri + "?" + EntityUtils.toString(se));
					if (false == isMulti) {
						httppost.setHeader(HTTP.CONTENT_TYPE,
								"application/x-www-form-urlencoded;charset=utf-8");
						httppost.setEntity(se);
					} else {
						for (int k = 0; k < params.size(); k++) {
							Logs.d(TAG, params.get(k).getName() + ":"
									+ params.get(k).getValue());
							mEntityBuilder.addTextBody(params.get(k).getName(),
									params.get(k).getValue());
						}
						// mEntityBuilder.addTextBody("NameValue",
						// EntityUtils.toString(se));
					}
				}
				// 文件上传
				if ((null != files) && (!files.isEmpty())) {
					if (false == isMulti) {
						Logs.d(TAG, "file" + ": " + files.get(0));
						FileEntity entity = new FileEntity(new File(
								files.get(0)), ContentType.create("text/plain",
								"UTF-8").toString());
						httppost.setEntity(entity);
					} else {
						for (int j = 0; j < files.size(); j++) {
							Logs.d(TAG, "file" + j + ": " + files.get(j));
							File file = new File(files.get(j));
							if ((null != file) && (file.isFile())) {
								mEntityBuilder.addBinaryBody("file", file);
							}
						}
					}
				}
				if (true == isMulti) {
					httppost.setEntity(mEntityBuilder.build());
				}

			} catch (UnsupportedEncodingException e1) {
				Logs.e(TAG, "编码异常: " + e1.toString());
				return null;
			} catch (IOException e) {
				Logs.e(TAG, "IO异常: " + e.toString());
				return null;
			}
			try {

				HttpResponse response = httpclient.execute(httppost);
				int status = response.getStatusLine().getStatusCode();
				Logs.d(TAG, "第" + i + "次尝试发送数据到服务器。");
				if ((null == sessionID) || (true == sessionID.isEmpty())) {
					if (true == response.containsHeader("Set-Cookie")) {
						sessionID = response.getFirstHeader("Set-Cookie")
								.getValue().split(";")[0];
					}
				}
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity);// 取出应答字符串
				nr.setStatus(status);
				nr.setResult(result);
				break;

			} catch (ClientProtocolException e) {
				Logs.e(TAG, "客户端协议异常: " + e.toString());
				TimeUtils.sleep(1000);
			} catch (IOException e) {
				Logs.e(TAG, "IO异常: " + e.toString());
				TimeUtils.sleep(1000);
			} catch (Exception e) {
				Logs.e(TAG, "Post异常: " + e.toString());
				TimeUtils.sleep(1000);
			} finally {
				// 关闭连接
				httppost.abort();
				httpclient.getConnectionManager().shutdown();
			}
		}
		if (i >= 3) {
			Logs.w(TAG, "尝试3次从服务器获取数据，均失败，请检查网络。");
		}
		return nr;
	}

	public boolean delete(File file) {
		if (file.isFile()) {
			return file.delete();
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				return file.delete();
			}

			for (int i = 0; i < childFiles.length; i++) {
				if (false == delete(childFiles[i])) {
					Logs.w(TAG, "删除文件" + childFiles[i].getName() + "失败！");
				}
			}
			return file.delete();
		}
		return true;
	}

	//
	@SuppressLint("NewApi")
	public boolean download(String uri, String filename, Handler handler)
			throws IOException {
		int i = 0;
		int BUFFER_LEN = 8192;
		if ((null == uri) || (uri.isEmpty())) {
			Logs.d(TAG, "URI is null");
			return false;
		}
		if ((null == filename) || (filename.isEmpty())) {
			Logs.d(TAG, "filename is null");
			return false;
		}
		File file = new File(filename);
		delete(file);

		OutputStream outputStream = null;
		int offset = 0;
		int filelen = 0;
		boolean isResumeBrokenTransfer = true;
		outputStream = new FileOutputStream(filename);
		for (i = 0; i < MAXRETRY; i++) {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpclient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							CONNECTION_TIMEOUT);
			HttpGet httpget = new HttpGet(uri);
			httpget.addHeader("clientType", "1");
			try {
				if ((null != sessionID) && (false == sessionID.isEmpty())) {
					httpget.setHeader("Cookie", sessionID);
				}
				if (true == isResumeBrokenTransfer) {
					httpget.addHeader("Range", "bytes=" + offset + "-");
				} else {
					if (httpget.containsHeader("Range")) {
						httpget.removeHeaders("Range");
					}
					isResumeBrokenTransfer = false;
				}
				HttpResponse response = httpclient.execute(httpget);
				int status = response.getStatusLine().getStatusCode();

				Logs.d(TAG, "第" + i + "次尝试从服务器获取数据。");
				if ((200 != status) && (206 != status)) {
					Logs.w(TAG, "HttpClient返回错误码: " + status);
					TimeUtils.sleep(3000);
					continue;
				}
				String contentRange = null;
				if (response.containsHeader("Content-Range")) {
					Logs.d(TAG, "=======================ddd=================");
					contentRange = response.getFirstHeader("Content-Range")
							.getValue();
					Logs.d(TAG, "Content-Range : " + contentRange);
					filelen = Integer.parseInt(contentRange
							.substring(contentRange.lastIndexOf("/") + 1));
					Logs.d(TAG, "下载文件大小: " + filelen + " Bytes");
					if (filelen <= offset) {
						Logs.e(TAG, "文件长度异常: " + filelen);
						break;
					}
				} else {
					if (response.containsHeader("Content-Length")) {
						Logs.d(TAG,
								"=======================ccc=================");
						String contentLength = response.getFirstHeader(
								"Content-Length").getValue();
						Logs.d(TAG, "Content-Length:" + contentLength);
						filelen = Integer.parseInt(contentLength);
						Logs.d(TAG, "下载文件大小: " + filelen + "Bytes");
						if (filelen <= offset) {
							Logs.e(TAG, "文件长度异常: " + filelen);
							break;
						}
					}
				}
				if (response.containsHeader("Accept-Ranges")) {
					if (response.getFirstHeader("Accept-Ranges").getValue()
							.equals("bytes")) {
						isResumeBrokenTransfer = true;
					} else {
						isResumeBrokenTransfer = false;
					}
				} else {
					isResumeBrokenTransfer = false;
				}
				if ((null == sessionID) || (true == sessionID.isEmpty())) {
					if (true == response.containsHeader("Set-Cookie")) {
						sessionID = response.getFirstHeader("Set-Cookie")
								.getValue().split(";")[0];
					}
				}
				// 此处处理接收到的数据
				HttpEntity httpEntity = response.getEntity();
				if (null != httpEntity) {

					final int fileSize = filelen;
					final Handler mHandler = handler;
					ProgressInputStream pis = new ProgressInputStream(
							httpEntity.getContent(), new ProgressListener() {

								@Override
								public void transferred(long transferedBytes) {
									Message msg = new Message();
									msg.what = MsgType.MSG_TYPE_HTTP_PROGRESS;
									Bundle data = new Bundle();
									data.putLong("progress", transferedBytes);
									data.putLong("maxvalue", fileSize);
									msg.setData(data);
									mHandler.sendMessage(msg);
								}
							});

					byte[] bytes = new byte[BUFFER_LEN];
					int size = 0;
					while ((size = pis.read(bytes)) != -1) {
						outputStream.write(bytes, 0, size);
						offset += size;
					}
					pis.close();
				}
				if (offset < filelen) {
					continue;
				}

				break;

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Logs.e(TAG, "客户端协议异常: " + e.toString());
				TimeUtils.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logs.e(TAG, "IO异常: " + e.toString());
				TimeUtils.sleep(1000);
			} finally {
				// 关闭连接
				httpget.abort();
				httpclient.getConnectionManager().shutdown();
			}
		}
		if (null != outputStream) {
			outputStream.flush();
			outputStream.close();
			outputStream = null;
		}
		if (i >= 3) {
			Logs.w(TAG, "尝试3次从服务器获取数据失败，请检查网络。");
			return false;
		} else {
			return true;
		}
	}

	@SuppressLint("NewApi")
	public boolean upload(String uri, String filename) {
		int i = 0;
		if ((null == filename) || (filename.isEmpty())) {
			Logs.w(TAG, "文件名不能为空！");
			return false;
		}
		File file = new File(filename);
		if (false == file.exists()) {
			Logs.w(TAG, "文件" + filename + "不存在！");
			return false;
		}
		FileEntity entity = new FileEntity(file, ContentType.create(
				"text/plain", "UTF-8").toString());

		for (i = 0; i < MAXRETRY; i++) {

			HttpPost httppost = new HttpPost(uri);
			if ((null != sessionID) && (false == sessionID.isEmpty())) {
				httppost.setHeader("Cookie", sessionID);
			}
			httppost.setEntity(entity);
			httppost.addHeader("clientType", "1");
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpclient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							CONNECTION_TIMEOUT);
			try {
				Logs.d(TAG, "第" + i + "次尝试发送文件" + filename + "到服务器。");
				HttpResponse response = httpclient.execute(httppost);
				int status = response.getStatusLine().getStatusCode();

				if (200 != status) {
					// TODO 添加出错处理
					Logs.w(TAG, "上传文件" + filename + "失败错误码:" + status);
					TimeUtils.sleep(3000);
					continue;
				}
				if ((null == sessionID) || (true == sessionID.isEmpty())) {
					if (true == response.containsHeader("Set-Cookie")) {
						sessionID = response.getFirstHeader("Set-Cookie")
								.getValue().split(";")[0];
					}
				}
				HttpEntity httpEntity = response.getEntity();
				String result = EntityUtils.toString(httpEntity);
				Logs.d(TAG, result);
				break;

			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				Logs.e(TAG, "上传文件: 客户端协议错误(" + e1.toString() + ")");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Logs.e(TAG, "上传文件: 客户端IO错误(" + e1.toString() + ")");
			} finally {
				httppost.abort();
				httpclient.getConnectionManager().shutdown();
			}

		}

		if (i >= 3) {
			Logs.w(TAG, "尝试3次从服务器获取数据，均失败，请检查网络。");
			return false;
		} else {
			return true;
		}
	}
	//
}
