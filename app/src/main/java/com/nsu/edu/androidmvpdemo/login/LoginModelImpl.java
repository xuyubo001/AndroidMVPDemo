package com.nsu.edu.androidmvpdemo.login;

import com.alibaba.fastjson.JSON;
import com.nsu.edu.androidmvpdemo.net.HttpRequest;
import com.nsu.edu.androidmvpdemo.net.net.IHttpCallback;
import com.nsu.edu.androidmvpdemo.net.net.NetManager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;
import com.nsu.edu.androidmvpdemo.utils.UrlPath;

import cn.lamppa.homework.model.baseinfo.result.TeacherLoginResultInfoVo;


/**
 * Created by Anthony on 2016/2/15.
 * Class Note:延时模拟登陆（2s），如果名字或者密码为空则登陆失败，否则登陆成功
 */
public class LoginModelImpl implements LoginModel {

    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {

        login_schoolServer(username,password,listener);
    }


    private void  login_schoolServer(String username,  String password, final OnLoginFinishedListener listener){
      final NetManager login_schoolsercer = new NetManager();
      login_schoolsercer.clearSession();
          // 封装list中
          List<NameValuePair> params = new ArrayList<NameValuePair>();
          // 账户名密码
          params.add(new BasicNameValuePair("userCode", username));
          params.add(new BasicNameValuePair("password", password));


          login_schoolsercer.httpPost(UrlPath.PATHLOGIN(), null, params,
                  new IHttpCallback() {
                      @Override
                      public void onSuccess(String msg) {

                          try {

                              if (msg.equals("ERROR_NOT_LOGGED_IN")) {


                              } else {

                                  TeacherLoginResultInfoVo info = JSON.parseObject(msg,
                                         TeacherLoginResultInfoVo.class);
                                  if(info.getResult().equals("SUCCEED")){
                                      listener.onSuccess(info.getTeacherInfoVo());
                                      HttpRequest.updateClientId(info.getTeacherInfoVo().getId());
                                  }

                              }
                          } catch (Exception e) {
                              e.printStackTrace();

                          } finally {

                          }
                      }

                      @Override
                      public void onStart() {

                      }

                      @Override
                      public void onProgress(long progress, long maxValue) {

                      }

                      @Override
                      public void onFailure(String err) {
                          listener.onFail();
                      }
                  });

      }

}
