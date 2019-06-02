package com.ms.wed.wechat.wedding.core;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**********************************
 * 对接微信服务请求
 * 鲁宏伟
 *******************************/
public interface BaseController {
    public void process(HttpServletRequest request , HttpServletResponse response) throws Throwable;
}
