package com.appril.helios.invoke;

import com.appril.helios.domain.ApiRequest;
import com.appril.helios.domain.ApiResult;
import com.appril.helios.invoke.callback.BaseServiceFallback;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Feign 调用
 *
 *  spring cloud feign不支持@RequestBody+ RequestMethod.GET，报错
 */
@FeignClient(name = "HELIOS-BASE-SERVICE", configuration = BaseServiceInvoke.MultipartSupportConfig.class, fallback = BaseServiceFallback.class)
public interface BaseServiceInvoke {

    @PostMapping("/base/test")
    ApiResult<Map<String,Object>> test(@RequestBody ApiRequest apiRequest);

    @GetMapping("/base/testStr")
    String testStr(@RequestParam(name = "str") String str);


    @Configuration
    class MultipartSupportConfig {
        @Bean
        public Encoder multipartFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(() -> new HttpMessageConverters(new RestTemplate().getMessageConverters())));
        }
    }
}