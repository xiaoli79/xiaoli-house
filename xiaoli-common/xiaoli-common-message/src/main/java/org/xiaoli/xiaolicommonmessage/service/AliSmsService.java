//package org.xiaoli.xiaolicommonmessage.service;
//
//
//import com.aliyun.dysmsapi20170525.Client;
//import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
//import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
//import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
//import com.google.gson.Gson;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.xiaoli.xiaolicommoncore.utils.JsonUtil;
//import org.xiaoli.xiaolicommondomain.constants.MessageConstants;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class AliSmsService {
//
//
//    /**
//     * 客户端
//     */
//    @Autowired
//    private Client client;
//
//
//    /**
//     * 短信模版代码
//     */
//    @Value("${sms.aliyun.templateCode:}")
//    private String templateCode;
//
//
//    /**
//     * 签名
//     */
//    @Value("${sms.sign-name:}")
//    private String singName;
//
//
//    /**
//     * 是否发上短信
//     */
//    @Value("${sms.send-message:}")
//    private boolean sendMessage;
//
//
//    /**
//     * 发送短信验证码
//     * @param phone 手机号
//     * @param code 验证码
//     * @return 发送成功
//     */
//    public boolean sendMobileCode(String phone,String code){
//        Map<String,String> params = new HashMap<>();
//        params.put("code",code);
//        return sendTemMessage(phone,templateCode,params);
//    }
//
//
//    /**
//     * 发送短信
//     */
//    private boolean sendTemMessage(String phone, String templateCode, Map<String,String> params) {
////      先判断是否可以发送
//        if(!sendMessage){
//            log.error("短信发送通道关闭,{}",phone);
//            return false;
//        }
//
////      设置发送信息
//        SendSmsRequest  request = new SendSmsRequest();
//        request.setPhoneNumbers(phone);
//        request.setSignName(singName);
//        request.setTemplateCode(templateCode);
//        request.setTemplateParam(JsonUtil.obj2String(params));
//
//
//        try {
////          发送短信
//            SendSmsResponse response =  client.sendSms(request);
////          得到响应体
//            SendSmsResponseBody responseBody = response.getBody();
//            if(responseBody.getCode().equals(MessageConstants.SMS_MSG_OK)){
//                return true;
//            }
//            log.error("短信{} 发送失败，失败原因{}...",new Gson().toJson(request),responseBody.getMessage());
//            return false;
//        } catch (Exception e) {
//            log.error("短信{} 发送失败，失败原因{}...",new Gson().toJson(request),e.getMessage());
//            return false;
//        }
//    }
//
//}
