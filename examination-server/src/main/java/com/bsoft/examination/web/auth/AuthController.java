package com.bsoft.examination.web.auth;

import com.bsoft.examination.domain.auth.User;
import com.bsoft.examination.service.auth.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录验证
 * @author Artolia Pendragon
 */
@RestController
@RequestMapping("authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 登录
     * @param user 用户实体类
     * @return String
     */
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return authService.login(user.getUsername(), user.getPassword()).toJson();
    }

}
