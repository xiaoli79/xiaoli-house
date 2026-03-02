package org.xiaoli.xiaolicommondomain.exception;


import lombok.Getter;
import lombok.Setter;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;


/**
 * 自定义异常模板~~
 */

@Getter
@Setter
public class ServiceException extends RuntimeException {


    /**
     * 状态码
     */
    private int code;

    /**
     * 信息~~
     */
    private String msg;

    /**
     * 响应构造异常
     * @param resultCode
     */
    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMsg());  // ← 传递给父类
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 消息构造异常
     * @param message
     */
    public ServiceException(String message) {
        super(message);  // ← 传递给父类
        this.code = ResultCode.ERROR.getCode();
        this.msg = message;
    }

    /**
     * 消息和响应异常
     * @param code
     * @param msg
     */
    public ServiceException(String msg, int code) {
        super(msg);  // ← 传递给父类
        this.code = code;
        this.msg = msg;
    }

}
