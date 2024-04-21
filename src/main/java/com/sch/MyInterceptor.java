package com.sch;

import com.alibaba.fastjson.JSON;
import com.sch.common.BaseContext;
import com.sch.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    String[] urls={
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/common/**",
            "/user/sendMsg",
            "/user/login",
            "/backend/index.html"
    };
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        boolean check = check(urls, url);
        //无需处理的url,放行
        if (check) {
            log.info("无需处理的url");
            return true;
        }
        Object employee = request.getSession().getAttribute("employee");
        Object user = request.getSession().getAttribute("user");
        //用户已经登录过，无需验证,放行
        if (employee!=null){
            log.info("用户已经登录过，无需验证");
            Long id= (Long) employee;
            BaseContext.setId(id);
            return true;
        }
        if (user!=null){
            log.info("用户登录过了");
            Long id= (Long) user;
            BaseContext.setId(id);
            return true;
        }

        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));

        return false;
    }

    private boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }

}
