
package com.hdw.dubbo.common.scan;

import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;


/**
 * 
 * @description SpringBoot 实现json和jsonp格式数据,接口共用
 * @author TuMinglong
 * @date 2018年3月7日 上午9:26:27
 */
@ControllerAdvice
public class JsonpResponseAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpResponseAdvice() {
        super("callback");
    }

    @Override
    protected MediaType getContentType(MediaType contentType, ServerHttpRequest request, ServerHttpResponse response) {
        return new MediaType("application", "javascript", contentType.getCharset());
    }
}
