package com.bsoft.examination.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数解析
 */
public class RequestParamPaser {

    /**
     * 获取请求参数
     * @param request
     * @return
     */
    public static Map<String, Object> getParameters(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, String[]> item : map.entrySet()) {
            if (item.getValue() != null && item.getValue().length >= 1) {
                result.put(item.getKey(), item.getValue()[0]);
            }
        }

        return result;
    }
}
