package com.appril.helios.controller;


import com.appril.helios.domain.ApiRequest;
import com.appril.helios.domain.ApiResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/base")
public class BaseTestController {

    @PostMapping("/test")
    ApiResult<Map<String,Object>> test(@RequestBody ApiRequest apiRequest){
        System.out.println("进入 base/test 请求");
        Map<String,Object> data = new HashMap<>();
        data.put("name","李白");
        data.put("phone","13674689562");
        data.put("dynasty","Tang");
        return ApiResult.isOk("111111","查询成功！",data);
    }

    @GetMapping("/testStr")
    String testStr(@RequestParam(name = "str") String str){
        System.out.println("进入 base/testStr 请求");
        return str;
    }
}
