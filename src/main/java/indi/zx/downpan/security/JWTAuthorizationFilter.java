package indi.zx.downpan.security;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.exception.InternalServerExecption;
import indi.zx.downpan.security.model.JwtUser;
import indi.zx.downpan.support.util.JwtTokenUtils;
import indi.zx.downpan.support.util.ResponseUtil;
import indi.zx.downpan.support.util.SecurityUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:20
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        try {
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader,response));
        } catch (InternalServerExecption e) {
            //返回json形式的错误信息
            Response<?> failure = ResponseUtil.failure(e.getCode(),e.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSONObject.toJSONString(failure));
            return;
        }
        super.doFilterInternal(request, response, chain);
    }

    // 这里从token中获取用户信息并新建一个token
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader, HttpServletResponse response) throws InternalServerExecption {
        String token = tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        boolean expiration = JwtTokenUtils.isExpiration(token);
        if (expiration) {
            throw new InternalServerExecption(GlobalConstants.Res.ACCESS_DENIED.getCode(),"token过期请重新登录！");
        } else {
            String username = JwtTokenUtils.getUsername(token);
            JwtUser jwtUser = new JwtUser();
            jwtUser.setUsername(username);
            SecurityUtil.setConcurrentUser(jwtUser);
            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null, null);
            }
        }
        return null;
    }
}
