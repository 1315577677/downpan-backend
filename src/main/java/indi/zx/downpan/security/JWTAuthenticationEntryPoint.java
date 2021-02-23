package indi.zx.downpan.security;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.exception.InternalServerExecption;
import indi.zx.downpan.support.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:20
 */
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    @Autowired
    public JWTAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        resolver.resolveException(request,response,null
                , new InternalServerExecption(GlobalConstants.Res.ACCESS_DENIED.getCode(),authException.getMessage()));
    }
}
