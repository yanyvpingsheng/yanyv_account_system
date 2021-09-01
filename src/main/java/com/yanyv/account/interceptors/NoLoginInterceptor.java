package com.yanyv.account.interceptors;

import com.yanyv.account.entity.Account;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Account account = (Account) request.getSession().getAttribute("account");
        if(account != null) System.out.println(account.getId());
        if (null == account) {
            /**
             * 未登录或过期
             */
            response.sendRedirect("login");
            return false;
        }
        return true;
    }
}
