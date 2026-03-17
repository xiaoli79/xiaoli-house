package org.xiaoli.xiaolichatservice.config;


import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;


/**
 * 编码器
 */
public class ServerEncoder implements Encoder.Text<Object> {


    @Override
    public String encode(Object obj) throws EncodeException {
        return JsonUtil.obj2String(obj);
    }



    @Override
    public void init(EndpointConfig endpointConfig) {
        Text.super.init(endpointConfig);
    }



    @Override
    public void destroy() {
        Text.super.destroy();
    }
}
