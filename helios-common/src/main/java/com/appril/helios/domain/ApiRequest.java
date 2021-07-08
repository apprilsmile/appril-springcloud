package com.appril.helios.domain;

import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ApiRequest {

    public static final TypeReference<Map<String, Object>> MapType = new TypeReference<Map<String, Object>>() {
    };
    /**
     * 终端类型 ios/android/pc/terminal
     */
    private String platform;
    /**
     * 请求版本软件版本号，默认1.0
     */
    private String appVersion;
    /**
     * 请求版本接口版本号，默认1.0
     */
    private String apiVersion;
    /**
     * 终端设备
     */
    private String mac;
    /**
     * 设备当前IP地址
     */
    private String ip;
    /**
     * 令牌
     */
    private String token;
    /**
     * 具体的请求参数
     */
    private Map<String, Object> data;

    private String page;

    private String limit;

    private String userId;

    private String userName;


    public static ApiRequest New(Map<String, Object> data) {
        return new ApiRequest().setData(data);
    }

    public static ApiRequest New() {
        return New(new HashMap<>());
    }

    public <T> T getData(String key) {
        if (null != data) {
            if (data.containsKey(key)) {
                return (T) data.get(key);
            }
        }
        return null;
    }

}
