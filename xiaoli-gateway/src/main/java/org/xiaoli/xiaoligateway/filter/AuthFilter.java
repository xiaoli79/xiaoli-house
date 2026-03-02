package org.xiaoli.xiaoligateway.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.xiaoli.xiaolicommoncore.utils.ServletUtil;
import org.xiaoli.xiaolicommoncore.utils.StringUtil;
import org.xiaoli.xiaolicommondomain.constants.SecurityConstants;
import org.xiaoli.xiaolicommondomain.constants.TokenConstants;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommonredis.service.RedisService;
import org.xiaoli.xiaolicommonsecurity.utils.JwtUtil;
import org.xiaoli.xiaoligateway.config.IgnoreWhiteProperties;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

//  白名单
    @Autowired
    private IgnoreWhiteProperties  ignoreWhiteProperties;

//  redis微服务
    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

//      1.获取请求
        ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
//      2.获取请求路由
        String url = request.getURI().getPath();
//      如果处在白名单当中，直接放行
        if(StringUtil.matches(ignoreWhiteProperties.getWhites(), url)){
            return chain.filter(exchange);
        }

//      3.获取令牌
        String token = getToken(request);
        if(StringUtils.isEmpty(token)){
            return unauthorizedResponse(exchange, ResultCode.TOKEN_EMPTY);
        }
//      4.根据令牌获取有效信息
        Claims claims;
        claims = JwtUtil.parseToken(token);
        if(claims == null){
            return unauthorizedResponse(exchange, ResultCode.TOKEN_INVALID);
        }
//      5.获取缓存中的key
        String userKey = JwtUtil.getUserKey(claims);
        Boolean isLogin = redisService.hasKey(getTokenKey(userKey));
        if(!isLogin){
            return unauthorizedResponse(exchange, ResultCode.LOGIN_STATUS_OVERTIME);
        }

//      6.获取用户数据信息
        String userId = JwtUtil.getUserID(claims);
        String userName = JwtUtil.getUserName(claims);
        String userFrom = JwtUtil.getUserFrom(claims);
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(userName)){
            return unauthorizedResponse(exchange,ResultCode.TOKEN_CHECK_FAILED);
        }

//      7.设置用户信息到请求,构建了一个新请求~~
        ServerHttpRequest.Builder mutate = request.mutate();
        addHeader(mutate,SecurityConstants.USER_KEY,userKey);
        addHeader(mutate,SecurityConstants.USER_ID,userId);
        addHeader(mutate,SecurityConstants.USERNAME,userName);
        addHeader(mutate,SecurityConstants.USER_FROM,userFrom);

//      8.加上header信息之后继续放行，由网关放回到微服务中，由后端统一处理登录逻辑~~
        return chain.filter(exchange.mutate().request(mutate.build()).build());

    }


    /**
     * 根据http请求获取token
     * @param request
     * @return
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(SecurityConstants.AUTHENTICATION);

//      裁剪令牌前缀
        if(StringUtils.isNoneEmpty(token) && token.startsWith(TokenConstants.PREFIX)){
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * 未授权返回
     * @param exchange
     * @param resultCode 结果码
     * @return
     */
   private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, ResultCode resultCode) {
        log.error("[鉴权处理异常]请求路径是：{}",exchange.getRequest().getPath());
        int retCode =  Integer.parseInt(String.valueOf(resultCode.getCode()).substring(0,3));
//      unauthroized
        return ServletUtil.webFluxResponseWriter(exchange.getResponse(), HttpStatus.valueOf(retCode),resultCode.getMsg(),resultCode.getCode());

   }


    /**
     * 获取缓存key
     * @param token token信息
     * @return tokenKey
     */
   private String getTokenKey(String token){
       return TokenConstants.LOGIN_TOKEN_KEY+token;
   }


    /**
     * 添加header
     * @param mutate 请求
     * @param name key
     * @param value 值
     */
   private void addHeader(ServerHttpRequest.Builder mutate,String name,Object value){
       if(value == null){
           return;
       }
       String valueStr = value.toString();
       String valueEncode = ServletUtil.urlEncode(valueStr);
       mutate.header(name, valueEncode);
   }


    /**
     * 处理优先级
     * @return
     */
    @Override
    public int getOrder() {
        return -200;
    }
}
