package indi.zx.downpan.security;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.common.response.Response;
import indi.zx.downpan.security.model.JwtUser;
import indi.zx.downpan.security.model.LoginUser;
import indi.zx.downpan.support.util.JwtTokenUtils;
import indi.zx.downpan.support.util.ResponseUtil;
import indi.zx.downpan.support.util.SecurityUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 16:20
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private ThreadLocal<Integer> rememberMe = new ThreadLocal<>();
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // 从输入流中获取到登录的信息
        try {
            LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
            rememberMe.set(loginUser.getRememberMe() == null ? 0 : loginUser.getRememberMe());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            return null;
        }
    }

//     成功验证后调用的方法
//     如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                           Authentication authResult) throws IOException, ServletException {
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        SecurityUtil.setConcurrentUser(jwtUser);
        boolean isRemember = rememberMe.get() == 1;


        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), isRemember);
//        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), false);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        //response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);


        JSONObject json = new JSONObject();
        json.put(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + token);
        json.put("user",jwtUser);
        Response<?> success = ResponseUtil.success(json);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONObject.toJSONString(success));

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Response<?> failure = ResponseUtil.failure(GlobalConstants.Res.RESOURCE_NOT_FOUND);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONObject.toJSONString(failure));
    }
}
