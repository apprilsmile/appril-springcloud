package com.appril.helios.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.appril.helios.domain.ApiRequest;
import com.appril.helios.domain.ApiResult;
import com.appril.helios.util.JWTUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 验证token的拦截器
 */
@Slf4j
public class CheckTokenFilter extends ZuulFilter {

    @Value("${helios.releaseMethods}")
    private List<String> releaseMethods;

    @Value("${loginToken.whiteList}")
    private String whiteList;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = getCurrentContext();
        String method = context.getRequest().getRequestURI();
        return !releaseMethods.contains(method);
    }


    @Override
    public Object run() throws ZuulException {
        long start = System.currentTimeMillis();
        try {
            /* String igCreateTime=  DateChangeUtil.timeStamp2Date(start);*/
            RequestContext context = getCurrentContext();
            context.set("start", start);
            HttpServletRequest httpServletRequest = context.getRequest();
            context.set("ip", getIpAddr(httpServletRequest));
            //兼容ie8，后台接口不做改动，请求头添加content-type。
            String contentType = context.getZuulRequestHeaders().get(HttpHeaders.CONTENT_TYPE);
            if (StringUtils.isEmpty(contentType) && StringUtils.isEmpty(httpServletRequest.getContentType())) {
                context.addZuulRequestHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            }

            String method = httpServletRequest.getRequestURI();
            /* context.set("code",200);*/
            String token = httpServletRequest.getHeader("token");
            String userId = "";
            if (StrUtil.isEmpty(token)) {
                if (!StringUtils.isEmpty(httpServletRequest.getContentType()) && httpServletRequest.getContentType().contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                    log.debug("time " + (System.currentTimeMillis() - start));
                    return null;
                }
                InputStream in = httpServletRequest.getInputStream();
                String requestBody = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
                if (StringUtils.isEmpty(requestBody)) {
                    log.debug("time " + (System.currentTimeMillis() - start));
                    return null;

                }
                ApiRequest apiRequest = JSONObject.parseObject(requestBody, ApiRequest.class);
                token = apiRequest.getToken();
                userId = apiRequest.getUserId();
                context.set("apiRequest", apiRequest);
            }
            /**
             * 判断下请求的接口，如果是登录接口，在登录成功后要返回一个token，
             * 注册，或者获取验证码的接口不需要验证token
             * 其他接口都需要验证token
             */
            //验证token
            if (!whiteList.contains(method)) {
                boolean tokenIsValue = JWTUtils.checkStrToken(token);
                if (tokenIsValue) {
                    log.debug("user " + userId + " token ok ,time " + (System.currentTimeMillis() - start));
                    return null;
                }
                context.set("code", 500);
                //失败返回参数
                ApiResult apiResult = ApiResult.isErrNoToken("Token验证失败，请登录",null);
                context.setResponseBody(JSONObject.toJSONString(apiResult));
                //中断转发
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(401);
                context.getResponse().setContentType("application/json;charset=UTF-8");
                log.debug("user " + userId + " token err ,time " + (System.currentTimeMillis() - start));
            }
            log.debug("user " + userId + " other ,time " + (System.currentTimeMillis() - start));
            return null;
        } catch (Exception e) {
            /*e.printStackTrace();*/
            RequestContext context = getCurrentContext();
            context.set("code", 500);
            log.debug("error ,time " + (System.currentTimeMillis() - start));
            return null;
        }

    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        System.out.println("第一次查询" + ip);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (inet!=null){
                    ip = inet.getHostAddress();
                }
            }
        }
        return ip;

    }
}
