package com.bsoft.examination.web.auth;

import com.bsoft.examination.domain.auth.User;
import com.bsoft.examination.service.auth.UserService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户操作
 * @author Artolia Pendragon
 * @version 1.0.0
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 获取用户信息
     * @return String
     */
    @GetMapping("/getUserInfo")
    public String getUserInfo() {
        return userService.getUserInfo().toJson();
    }

    /**
     * 获取用户列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getUserList")
    public String getUserList(HttpServletRequest request) {
        Map<String, Object> params = RequestParamPaser.getParameters(request);

        return userService.getUserList(params).toJson();
    }

    /**
     * 保存或更新用户
     * @param user 用户实体
     * @param request 请求
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody User user, HttpServletRequest request) {
        String op = request.getParameter("op");
        return userService.save(user, op).toJson();
    }

    /**
     * 删除用户
     * @param userId 用户id
     * @return String
     */
    @GetMapping("/delete")
    public String delete(@RequestParam String userId) {
        return userService.delete(userId).toJson();
    }
}
