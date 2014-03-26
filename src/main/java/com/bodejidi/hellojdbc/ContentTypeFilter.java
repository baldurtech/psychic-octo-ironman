package com.bodejidi.hellojdbc;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

public class ContentTypeFilter implements Filter {

    static final String contentType = "text/html";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // pass
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        response.setContentType(contentType);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // pass
    }
}
