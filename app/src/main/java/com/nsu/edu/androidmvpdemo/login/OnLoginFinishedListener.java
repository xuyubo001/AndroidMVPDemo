package com.nsu.edu.androidmvpdemo.login;

import cn.lamppa.homework.model.vo.user.TeacherInfoVo;

/**
 * Created by Anthony on 2016/2/15.
 * Class Note:登陆事件监听
 */
public interface OnLoginFinishedListener {

    void onUsernameError();

    void onPasswordError();

    void onSuccess(TeacherInfoVo teacherInfoVo);
    void onFail();
}
