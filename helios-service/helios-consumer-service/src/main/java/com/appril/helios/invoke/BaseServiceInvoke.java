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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Feign 调用
 */
@FeignClient(name = "HELIOS-BASE-SERVICE", configuration = BaseServiceInvoke.MultipartSupportConfig.class, fallback = BaseServiceFallback.class)
@Primary
public interface BaseServiceInvoke {

    @GetMapping("/base/test")
    ApiResult<Map<String,Object>> test(@RequestBody ApiRequest apiRequest);

    @Configuration
    class MultipartSupportConfig {
        @Bean
        public Encoder multipartFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(new ObjectFactory<HttpMessageConverters>() {
                @Override
                public HttpMessageConverters getObject() throws BeansException {
                    return new HttpMessageConverters(new RestTemplate().getMessageConverters());
                }
            }));
        }
    }
}
