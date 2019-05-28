package io.cjf.lianxi0509.filter;

import io.cjf.lianxi0509.constant.Constant;
import io.cjf.lianxi0509.exception.ClientException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(2)
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String requestURI = request.getRequestURI();
        boolean containsExclude = Arrays.asList(Constant.passUrls).contains(requestURI);
        if (containsExclude) {
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        List<String> authUrls = (List<String>)request.getAttribute("authUrls");

        boolean containsAuthUrl = authUrls.contains(requestURI);
        if (!containsAuthUrl){
            return;
        }
        //todo throw excedption
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
