package org.xiaoli.xiaolicommonsecurity.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolicommoncore.utils.ServletUtil;
import org.xiaoli.xiaolicommondomain.constants.CacheConstants;
import org.xiaoli.xiaolicommondomain.constants.SecurityConstants;
import org.xiaoli.xiaolicommondomain.constants.TokenConstants;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import org.xiaoli.xiaolicommonsecurity.domain.dto.LoginUserDTO;
import org.xiaoli.xiaolicommonsecurity.domain.dto.TokenDTO;
import org.xiaoli.xiaolicommonsecurity.utils.JwtUtil;
import org.xiaoli.xiaolicommonsecurity.utils.SecurityUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * token服务类
 */

@Slf4j
@Component
public class TokenService {



    /**
     * 毫秒
     */
    private final static long MILLIS_SECOND = 1000;

    /**
     * 分钟
     */
    private final static long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    /**
     * 过期时间
     */
    private final static Long EXPIRE_TIME = CacheConstants.EXPIRATION;

    /**
     * token的KEY前缀
     */
    private final static String ACCESS_TOKEN = TokenConstants.LOGIN_TOKEN_KEY;

    /**
     * 120分钟
     */
    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 引用redis服务
     */
    @Autowired
    private RedisService redisService;


    /**
     * 创建Token
     * @param loginUserDTO 登录信息
     * @return token信息
     */
    public TokenDTO createToken(LoginUserDTO loginUserDTO) {

//      1.随机产生用户标识 这里面的token是UUID的token，这是用来缓存key的
        String token = UUID.randomUUID().toString();
        loginUserDTO.setToken(token);
//      2.进行缓存
        refreshToken(loginUserDTO);

//      2.生成原始数据声明
        Map<String,Object> claimMap = new HashMap<>();
        claimMap.put(SecurityConstants.USER_KEY,token);
        claimMap.put(SecurityConstants.USER_ID,loginUserDTO.getUserId());
        claimMap.put(SecurityConstants.USERNAME,loginUserDTO.getUserName());
        claimMap.put(SecurityConstants.USER_FROM,loginUserDTO.getUserFrom());
//      3.生成TokenDTO
//      这是响应的accessToken~~
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(JwtUtil.createToken(claimMap));
        tokenDTO.setExpires(EXPIRE_TIME);
        return tokenDTO;
    }

    /**
     * 根据令牌获取用户信息
     * @param token 令牌
     * @return 用户信息
     */
    public LoginUserDTO getLoginUser(String token) {

        LoginUserDTO user = null;

        try{
            if(StringUtils.isNotBlank(token)){
                String userKey = JwtUtil.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userKey),LoginUserDTO.class);
                return user;
            }
        }catch (Exception e){
            log.warn("获取登录用户失败");
        }
        return user;
    }

    /**
     * 根据请求来获取用户信息
     * @param request
     * @return
     */
    public LoginUserDTO getLoginUser(HttpServletRequest request) {
        String token = SecurityUtil.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 不传参数来获取用户信息
     * @return 用户信息
     */
    public LoginUserDTO getLoginUser(){
        return getLoginUser(ServletUtil.getRequest());
    }



    /**
     * 根据令牌来删除用户登录状态
     * @param token
     */
    public void delLoginUser(String token) {
        if(StringUtils.isNotBlank(token)){
            String userKey = JwtUtil.getUserKey(token);
            redisService.deleteObject(getTokenKey(userKey));
        }
    }


    /**
     * 允许超管删除别人的登录状态
     * @param userId 用户ID
     * @param userFrom 用户来源
     */
    public void delLoginUser(Long userId,String userFrom) {
        if(userId == null) return;
        Collection<String> tokenkeys = redisService.keys(ACCESS_TOKEN+"*");
//      遍历redis里面的key删除
        for(String tokenkey:tokenkeys){
            LoginUserDTO user = redisService.getCacheObject(tokenkey,LoginUserDTO.class);
            if(user != null && user.getUserId().equals(userId) && user.getUserFrom().equals(userFrom)){
                redisService.deleteObject(tokenkey);
            }
        }
    }

    /**
     * 验证令牌有效期 不到120分钟，自动刷新下
     * @param loginUserDTO
     */
    public void verifyToken(LoginUserDTO loginUserDTO) {
        long expireTime = loginUserDTO.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if(expireTime - currentTime <= MILLIS_MINUTE_TEN){
            refreshToken(loginUserDTO);
        }
    }


    /**
     * 设置用户身份信息，允许登录
     * @param loginUserDTO 用户信息
     */
    public void setLoginUser(LoginUserDTO loginUserDTO) {
        if(loginUserDTO != null && StringUtils.isNotBlank(loginUserDTO.getToken())){
            refreshToken(loginUserDTO);
        }
    }


    /**
     * 缓存用户信息设置令牌有效期
     * @param loginUserDTO 用户信息
     */
    public void refreshToken(LoginUserDTO loginUserDTO) {

        loginUserDTO.setLoginTime(System.currentTimeMillis());


        loginUserDTO.setExpireTime(loginUserDTO.getLoginTime()+ EXPIRE_TIME * MILLIS_MINUTE);

//      根据随机产生用户标识生成key
        String userKey = getTokenKey(loginUserDTO.getToken());

//      生成loginUserDTO缓存
        redisService.setCacheObject(userKey,loginUserDTO,EXPIRE_TIME, TimeUnit.MINUTES);

    }

    /**
     * 获取token key的信息
     * @param token
     * @return tokenKey
     */
    private String getTokenKey(String token) {
        return ACCESS_TOKEN+token;
    }



}
