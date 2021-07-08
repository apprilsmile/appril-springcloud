package com.appril.helios.config;

import com.appril.helios.filter.CheckTokenFilter;
import com.appril.helios.filter.LogSaveFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulConfig {

    @Bean
    public CheckTokenFilter getCheckParameter() {
        return new CheckTokenFilter();
    }

    @Bean
    public LogSaveFilter getLogSaveFilter() {
        return new LogSaveFilter();
    }

}
