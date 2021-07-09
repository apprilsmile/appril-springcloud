package com.appril.helios.controller;


import com.appril.helios.domain.ApiRequest;
import com.appril.helios.domain.ApiResult;
import com.appril.helios.invoke.BaseServiceInvoke;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


@RestController
@RequestMapping("/consumer")
public class ConsumerTestController {

    @Resource
    private BaseServiceInvoke baseServiceInvoke;

    @GetMapping("/test")
    @ResponseBody
    public ApiResult<Map<String,Object>> test(@RequestBody ApiRequest apiRequest) {
        System.out.println("进入 consumer/test 方法");
        return baseServiceInvoke.test(apiRequest);
    }

    @GetMapping("/testStr")
    String testStr(@RequestParam(name = "str") String str){
        System.out.println("进入 consumer/testStr 请求");
        return baseServiceInvoke.testStr(str);
    }
}
