package com.es.phoneshop.web.filter;


import com.es.phoneshop.security.DosProtectionFilter;
import com.es.phoneshop.security.impl.DosProtectionFilterImpl;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DosFilter implements Filter {

    private static final int ERROR_TOO_MANY_REQUESTS = 429;

    private DosProtectionFilter dosProtectionFilter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dosProtectionFilter = DosProtectionFilterImpl.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        long time = ((HttpServletRequest)request).getSession().getLastAccessedTime();

        if (dosProtectionFilter.isAllowed(ip, time)) {
            filterChain.doFilter(request, response);
        }else{
            ((HttpServletResponse) response).setStatus(ERROR_TOO_MANY_REQUESTS);
        }
    }

    @Override
    public void destroy() {
    }
}
