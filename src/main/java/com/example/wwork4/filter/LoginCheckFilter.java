package com.example.wwork4.filter;

import com.alibaba.fastjson.JSONObject;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.utils.JwtContext;
import com.example.wwork4.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.http.HttpRequest;

@Slf4j

@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req =(HttpServletRequest) servletRequest;
        HttpServletResponse resp =(HttpServletResponse) servletResponse;
        //获取请求url
        String url = req.getRequestURI();
        log.info("请求的url: {}",url);
        //判断请求中是否包含login,如果包含，则是登录操作，放行。
        if(url.contains("login")){
            log.info("登录操作，放行。。");
            filterChain.doFilter(servletRequest,servletResponse);
            return ;
        }
        if(url.contains("register")){
            log.info("注册操作，放行。。");
            filterChain.doFilter(servletRequest,servletResponse);
            return ;
        }
        //获取请求头中的令牌(token)
        String jwt=req.getHeader("token");
        //判断令牌是否存在，如果不存在，返回错误结果（未登录）
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头未空，返回登录的信息");
            Result error =Result.error("NO_LOGIN");
            String notLogin=JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return ;
        }
        //解析token




            try {
                Claims claims = JwtUtils.parseJWT(jwt);
                JwtContext.setClaims(claims);
                log.info("放行");
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("解析令牌失败，返回未登录错误信息");
                Result error = Result.error("NO_LOGIN");
                String notLogin = JSONObject.toJSONString(error);
                resp.getWriter().write(notLogin);
                return;
            }finally {
                JwtContext.clear();
            }
            //放行


    }
}
