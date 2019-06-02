package com.ms.wed.wechat.wedding.controller;

import com.ms.wed.wechat.wedding.core.BaseController;
import com.ms.wed.wechat.wedding.util.MessageUtil;
import com.ms.wed.wechat.wedding.util.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Controller
public class WeChatController implements BaseController {
    Logger logger = LoggerFactory.getLogger(WeChatController.class);
    @Override

    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public void process(HttpServletRequest request, HttpServletResponse response) throws Throwable{
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        PrintWriter out = response.getWriter();
        out.write(echostr);
    }
    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public void processpost(HttpServletRequest request, HttpServletResponse response) throws Throwable{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Map<String,String> message=MessageUtil.parseXml(request);

        String messageType=message.get("MsgType");

        if(messageType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)){
            //接收的是文本消息

            //打印接收所有参数
            System.out.println("ToUserName："+message.get("ToUserName"));
            System.out.println("FromUserName："+message.get("FromUserName"));
            System.out.println("CreateTime："+message.get("CreateTime"));
            System.out.println("MsgType："+message.get("MsgType"));
            System.out.println("Content："+message.get("Content"));
            System.out.println("MsgId："+message.get("MsgId"));

            String req_content=message.get("Content");

            String res_content="";

            //组装回复消息
            //我们做个小实验
            //接收内容：你好  回复：你好
            //接收内容：大家好  回复：大家好
            //接收内容：同志们好  回复：为人民服务
            if("你好".equals(req_content)){
                res_content="骚年，你好！";
            }else if ("大家好".equals(req_content)){
                res_content="大家好";
            }else if("同志们好".equals(req_content)){
                res_content="为人民服务";
            }else{
                //否则原样输出输入内容
                res_content="你猜猜我在干啥！";
            }

            TextMessage textMessage=new TextMessage();
            textMessage.setToUserName(message.get("FromUserName"));   //这里的ToUserName  是刚才接收xml中的FromUserName
            textMessage.setFromUserName(message.get("ToUserName"));   //这里的FromUserName 是刚才接收xml中的ToUserName  这里一定要注意，否则会出错
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setContent(res_content);
            textMessage.setMsgType(messageType);

            String xml=MessageUtil.textMessageToXml(textMessage);

            System.out.println("xml:"+xml);

            PrintWriter out = response.getWriter();
            out.print(xml);
            out.close();
        }
    }
}
