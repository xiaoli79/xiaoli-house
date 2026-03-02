package org.xiaoli.xiaolicommonsecurity.utils;


import jakarta.servlet.http.HttpServletRequest;
import org.xiaoli.xiaolicommoncore.utils.ServletUtil;
import org.xiaoli.xiaolicommondomain.constants.SecurityConstants;

/**
 *安全工具类
 */
public class SecurityUtil {


    /**
     * 获取请求的Token的信息
     * @return
     */
    public static String getToken(){
        return getToken(ServletUtil.getRequest());
    }

    /**
     * 根据request请求获取请求token
     *
     * @param request
     * @return token信息
     */
    public static String getToken(HttpServletRequest request){
        String token = request.getHeader(SecurityConstants.AUTHENTICATION);
        return token;
    }
}
