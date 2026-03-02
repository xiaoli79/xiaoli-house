package org.xiaoli.xiaoligateway.handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import reactor.core.publisher.Mono;


@Order(-1)
@Slf4j
@Configuration
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if(response.isCommitted()) {
            return Mono.error(ex);
        }

        int retCode = ResultCode.ERROR_CODE.getCode();
        String retMsg = ResultCode.ERROR.getMsg();
        if(ex instanceof NoResourceFoundException){
            retCode = ResultCode.SERVICE_NOT_FOUND.getCode();
            retMsg = ResultCode.SERVICE_NOT_FOUND.getMsg();
//      加入服务侧的异常？？
        }else if(ex instanceof ServiceException){
            retMsg = ex.getMessage();
            retCode = ((ServiceException) ex).getCode();
        }
        //截取http状态码
        int httpCode = Integer.parseInt(String.valueOf(retCode).substring(0, 3));

        log.error("网关处理异常请求路径:{},异常信息{}",exchange.getRequest().getPath(),ex.getMessage());

        return webFluxResponseWriter(response, HttpStatus.valueOf(httpCode),retMsg,retCode);
    }

    private Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus status,Object value,int code) {
        return webFluxResponseWriter(response,MediaType.APPLICATION_JSON_VALUE,status,value,code);
    }

    private Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus status, Object value, int code) {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE,contentType);
        R<?> result = R.fail(code,value.toString());
        DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtil.obj2String(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }
}
