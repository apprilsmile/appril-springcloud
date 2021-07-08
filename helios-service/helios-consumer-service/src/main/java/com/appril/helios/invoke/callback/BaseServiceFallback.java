package com.appril.helios.invoke.callback;

import com.appril.helios.domain.ApiRequest;
import com.appril.helios.domain.ApiResult;
import com.appril.helios.invoke.BaseServiceInvoke;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 熔断降级配置
 */
@Component
public class BaseServiceFallback implements BaseServiceInvoke {
    @Override
    public ApiResult<Map<String, Object>> test(ApiRequest apiRequest) {
        return null;
    }
}
