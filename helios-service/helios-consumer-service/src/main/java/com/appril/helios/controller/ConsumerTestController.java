package com.appril.helios.controller;


import com.alibaba.fastjson.JSON;
import com.appril.helios.domain.ApiRequest;
import com.appril.helios.domain.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/base")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET,
        RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
        RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins = "*")
public class ConsumerTestController {

    Logger logger = LoggerFactory.getLogger(ConsumerTestController.class);

    @GetMapping("/test")
    @ResponseBody
    public ApiResult<?> test(@RequestBody ApiRequest apiRequest) {
        logger.info("base/test 请求：" + JSON.toJSONString(apiRequest));
        Map<String,Object> data = new HashMap<>();
        data.put("name","李白");
        data.put("phone","13674689562");
        data.put("dynasty","Tang");
        return ApiResult.isOk("111111","查询成功！",data);
    }




}
