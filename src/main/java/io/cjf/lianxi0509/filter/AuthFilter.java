package io.cjf.lianxi0509.filter;

import io.cjf.lianxi0509.exception.ClientException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
@Order(2)
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        List<String> authUrls = (List<String>)request.getAttribute("authUrls");
        String requestURI = request.getRequestURI();
        boolean contains = authUrls.contains(requestURI);
        if (!contains){
            return;
        }
        //todo throw excedption
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
