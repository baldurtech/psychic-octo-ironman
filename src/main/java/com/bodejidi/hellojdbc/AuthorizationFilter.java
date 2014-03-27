package com.bodejidi.hellojdbc;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // pass
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if(!req.getRequestURI().startsWith(req.getContextPath() + "/auth/") && isNotLogin(req)) {
            ((HttpServletResponse)response).sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // pass
    }

    public boolean isNotLogin(HttpServletRequest req)
        throws IOException, ServletException {

        Long memberId = (Long)req.getSession().getAttribute("memberId");
        return null == memberId;
    }
}
