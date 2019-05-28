package io.cjf.lianxi0509.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.cjf.lianxi0509.dao.RoleMenuMapper;
import io.cjf.lianxi0509.dao.UserRoleMapper;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class TokenFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    private String[] urls = {
            "/user/getCaptcha",
            "/user/login",
            "/menu/getTree"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("token filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();

        boolean contains = Arrays.asList(urls).contains(requestURI);
        if (contains) {
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        String bearToken = request.getHeader("Authorization");
        String[] s = bearToken.split(" ");
        String token = s[1];

        Algorithm algorithm = Algorithm.HMAC256("cjf");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("tecent")
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);

        String username = jwt.getSubject();
        request.setAttribute("currentUsername",username);

        Claim userIdClaim = jwt.getClaim("userId");
        Integer userId = userIdClaim.asInt();
        request.setAttribute("currentUserId", userId);

        List<Integer> roleIds = userRoleMapper.selectRoleIds(userId);

        List<String> urls = roleMenuMapper.selectUrls(roleIds);

        request.setAttribute("authUrls",urls);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
