package com.project.interceptor;

import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class LoginInterceptor implements HandlerInterceptor {
    private final static String[] whiteUriList = {"/boards/*"};
    private final static String[] whiteMethodList = {};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       String requestMethod = request.getMethod();
       String requestUri = request.getRequestURI();
       boolean ret = isInWhiteList(whiteUriList, requestUri);
       if(!requestMethod.equals("GET")) ret = false;

       HttpSession session = request.getSession();
       if(!ret & (session == null || session.getAttribute("admin") == null)){
            response.sendRedirect("/login");
            return false;
       }
        return true;
    }

    // boards* 패턴의 uri 중에서 유일하게 get request는 로그인이 필요 없음.
    private boolean isInWhiteList(String[] whiteUriList, String requestURI){
        return PatternMatchUtils.simpleMatch(whiteUriList, requestURI);
    }
}
