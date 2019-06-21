package com.bsoft.examination.web.auth;

import com.bsoft.examination.domain.auth.Role;
import com.bsoft.examination.service.auth.RoleService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 角色
 * @author Artolia Pendragon
 * @version 1.0.0
 */
@RestController
@RequestMapping("role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 获取角色列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getRoleList")
    public String getRoleList(HttpServletRequest request) {
        Map<String, Object> params = RequestParamPaser.getParameters(request);
        return roleService.getRoleList(params).toJson();
    }

    /**
     * 保存或更新角色
     * @param role 角色实体
     * @param request 请求
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody Role role, HttpServletRequest request) {
        String op = request.getParameter("op");
        return roleService.save(role, op).toJson();
    }

    /**
     * 删除角色
     * @param roleId 角色id
     * @return String
     */
    @GetMapping("/delete")
    public String delete(String roleId) {
        return roleService.delete(roleId).toJson();
    }

    /**
     * 保存角色菜单
     * @param role 角色实体
     * @return String
     */
    @PostMapping("/saveResource")
    public String saveResource(@RequestBody Role role) {
        return roleService.saveResource(role).toJson();
    }
}
