package org.xiaoli.xiaolicommonmessage.service;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeResponse;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeResponseBody;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponseBody;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolicommondomain.constants.MessageConstants;

/**
 * 阿里云号码认证服务 - 短信验证码发送服务
 * 使用 SendSmsVerifyCode 接口，验证码由阿里云自动生成
 */
@Component
@Slf4j
public class AliPnsService {

    /**
     * 号码认证服务客户端
     */
    @Autowired
    Client pnsClient;

    /**
     * 签名
     */
    @Value("${pns.sign-name:}")
    private String signName;

    /**
     * 短信模版代码
     */
    @Value("${pns.aliyun.templateCode:}")
    private String templateCode;

    /**
     * 验证码长度
     */
    @Value("${pns.aliyun.codeLength:6}")
    private Long codeLength;

    /**
     * 验证码有效时间（分钟）
     */
    @Value("${pns.aliyun.validTime:5}")
    private Long validTime;

    /**
     * 是否发送短信
     */
    @Value("${pns.send-message:false}")
    private boolean sendMessage;

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号
     * @return 发送结果，包含验证码
     */
    public boolean sendPnsVerifyCode(String phoneNumber) {
        return sendPnsVerifyCode(phoneNumber, templateCode);
    }

    /**
     * 发送短信验证码（指定模版）
     *
     * @param phoneNumber  手机号
     * @param templateCode 模版代码
     * @return 发送结果，包含验证码
     */
    public boolean sendPnsVerifyCode(String phoneNumber, String templateCode) {
        // 先判断是否可以发送
        if (!sendMessage) {
            log.error("短信发送通道关闭, {}", phoneNumber);
            return false;
        }

        try {
            // 构建请求参数
            // 不设置 SchemeName，使用阿里云默认的"默认方案"
            SendSmsVerifyCodeRequest request = new SendSmsVerifyCodeRequest()
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setPhoneNumber(phoneNumber)
                    .setTemplateParam("{\"code\":\"##code##\",\"min\":\"" + validTime + "\"}");
//                    .setCodeLength(codeLength)
//                    .setValidTime(validTime);

            log.info("===== 开始发送验证码 =====");
            log.info("发送请求参数: phoneNumber={}, signName={}, schemeName=默认方案, templateCode={}, codeLength={}, validTime={}", 
                    phoneNumber, signName, templateCode, codeLength, validTime);

            // 发送请求
            SendSmsVerifyCodeResponse response = pnsClient.sendSmsVerifyCode(request);


            log.info("短信验证码发送响应: {}", new Gson().toJson(response));

            // 获取响应体
            SendSmsVerifyCodeResponseBody body = response.getBody();


            // 判断是否发送成功
            if (body != null && MessageConstants.PNS_MSG_OK.equals(body.getCode())) {

                return true;
            }

            String errorMsg = body != null ? body.getMessage() : "未知错误";
            log.error("短信验证码发送失败, 手机号: {}, 错误信息: {}", phoneNumber, errorMsg);
            return false;

        } catch (Exception e) {
            log.error("短信验证码发送异常, 手机号: {}, 异常信息: {}", phoneNumber, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 校验短信验证码
     * @param phoneNumber 手机号
     * @param verifyCode  用户输入的验证码
     * @return 校验结果，true表示验证通过，false表示验证失败
     */
    public boolean checkPnsVerifyCode(String phoneNumber, String verifyCode) {
        // 打印请求参数，方便调试
        log.info("===== 开始校验验证码 =====");
        log.info("手机号: {}, 验证码: {}", phoneNumber, verifyCode);
        
        try {
            // 构建校验请求
            // 不设置 SchemeName，使用阿里云默认方案（与发送时保持一致）
            CheckSmsVerifyCodeRequest request = new CheckSmsVerifyCodeRequest()
                    .setPhoneNumber(phoneNumber)
                    .setVerifyCode(verifyCode);

            log.info("请求参数: {}", new Gson().toJson(request));
            
            // 发送校验请求
            CheckSmsVerifyCodeResponse response = pnsClient.checkSmsVerifyCode(request);
            
            log.info("短信验证码校验响应: {}", new Gson().toJson(response));

            // 获取响应体
            CheckSmsVerifyCodeResponseBody body = response.getBody();

            // 判断是否校验成功
            if (body != null) {
                // 从 Model 对象中获取校验结果，校验通过的标识是 "PASS"
                CheckSmsVerifyCodeResponseBody.CheckSmsVerifyCodeResponseBodyModel model = body.getModel();
                if (model != null) {
                    String verifyResult = model.getVerifyResult();
                    if ("PASS".equals(verifyResult)) {
                        log.info("验证码校验通过, 手机号: {}", phoneNumber);
                        return true;
                    }
                    log.warn("验证码校验未通过, 手机号: {}, 结果: {}", phoneNumber, verifyResult);
                }
                return false;
            }

            String errorMsg = body != null ? body.getMessage() : "未知错误";
            log.error("验证码校验失败, 手机号: {}, 错误信息: {}", phoneNumber, errorMsg);
            return false;

        } catch (Exception e) {
            // 打印完整的异常堆栈信息
            log.error("验证码校验异常, 手机号: {}, 验证码: {}", phoneNumber, verifyCode);
            log.error("异常类型: {}", e.getClass().getName());
            log.error("异常信息: {}", e.getMessage());
            log.error("完整堆栈: ", e);
            return false;
        }
    }

}

//    /**
//     * 发送短信验证码（带自定义参数）
//     *
//     * @param phoneNumber   手机号
//     * @param templateCode  模版代码
//     * @param templateParam 模版参数（JSON格式）
//     * @param codeLength    验证码长度
//     * @param validTime     有效时间（分钟）
//     * @return 发送结果
//     */
//    public SendSmsVerifyCodeResult sendSmsVerifyCode(String phoneNumber, String templateCode,
//                                                     String templateParam, Long codeLength, Long validTime) {
//        // 先判断是否可以发送
//        if (!sendMessage) {
//            log.error("短信发送通道关闭, {}", phoneNumber);
//            return SendSmsVerifyCodeResult.fail("短信发送通道关闭");
//        }
//
//        try {
//            // 构建请求参数
//            SendSmsVerifyCodeRequest request = new SendSmsVerifyCodeRequest()
//                    .setSignName(signName)
//                    .setTemplateCode(templateCode)
//                    .setPhoneNumber(phoneNumber)
//                    .setTemplateParam(templateParam)
//                    .setCodeLength(codeLength)
//                    .setValidTime(validTime);
//
//            // 发送请求
//            SendSmsVerifyCodeResponse response = pnsClient.sendSmsVerifyCode(request);
//
//            log.info("短信验证码发送响应: {}", new Gson().toJson(response));
//
//            // 获取响应体
//            SendSmsVerifyCodeResponseBody body = response.getBody();
//
//            // 判断是否发送成功
//            if (body != null && MessageConstants.SMS_MSG_OK.equals(body.getCode())) {
//                String verifyCode = body.getModel() != null ? body.getModel().getVerifyCode() : null;
//                return SendSmsVerifyCodeResult.success(verifyCode);
//            }
//
//            String errorMsg = body != null ? body.getMessage() : "未知错误";
//            log.error("短信验证码发送失败, 手机号: {}, 错误信息: {}", phoneNumber, errorMsg);
//            return SendSmsVerifyCodeResult.fail(errorMsg);
//
//        } catch (Exception e) {
//            log.error("短信验证码发送异常, 手机号: {}, 异常信息: {}", phoneNumber, e.getMessage(), e);
//            return SendSmsVerifyCodeResult.fail(e.getMessage());
//        }
//    }
