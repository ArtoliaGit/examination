package com.bsoft.examination.common;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * 发送消息编码
 */
public class ServerEncoder implements Encoder.Text<Result> {

    @Override
    public String encode(Result result) {
        return result.toJson();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
