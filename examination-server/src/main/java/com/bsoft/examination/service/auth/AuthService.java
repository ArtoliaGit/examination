package com.bsoft.examination.service.auth;

import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录验证
 * @author artolia
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return Result
     */
    public Result login(String username, String password) {
        Result<String> result = new Result<>();
        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String sessionId = request.getSession().getId();

            result.setCode(HttpStatus.OK);
            result.setMessage("登录成功");
            result.setData(sessionId);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            result.setCode(403);
            result.setMessage("验证失败");
        }
        return result;
    }
}
