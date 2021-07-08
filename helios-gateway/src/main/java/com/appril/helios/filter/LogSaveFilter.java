package com.appril.helios.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 日志存储网关过滤器
 */
@Slf4j
public class LogSaveFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }


    @Override
    public boolean shouldFilter() {
        return true;
    }

    Logger logger = LoggerFactory.getLogger(LogSaveFilter.class);

    @Override
    public Object run() {
        RequestContext context = getCurrentContext();
        HttpServletRequest httpServletRequest = context.getRequest();
        HttpServletResponse httpServletResponse = context.getResponse();
        httpServletResponse.setContentType(MediaType.TEXT_PLAIN_VALUE);
        return null;
    }



}
