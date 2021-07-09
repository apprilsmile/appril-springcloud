package com.appril.helios.invoke.callback;

import com.appril.helios.domain.ApiRequest;
import com.appril.helios.domain.ApiResult;
import com.appril.helios.invoke.BaseServiceInvoke;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 熔断降级配置
 */
@Component
public class BaseServiceFallback implements BaseServiceInvoke {
    @Override
    public ApiResult<Map<String, Object>> test(ApiRequest apiRequest) {
        Map<String,Object> data = new HashMap<>();
        data.put("name","苏轼");
        data.put("phone","189564896");
        data.put("dynasty","Song");
        return ApiResult.isErrNoToken("查询失败！",data);
    }

    @Override
    public String testStr(String str) {
        return "good good study ,day day up !";
    }
}
