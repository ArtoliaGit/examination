package com.bsoft.examination.service.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.common.auth.UserInfo;
import com.bsoft.examination.domain.auth.Role;
import com.bsoft.examination.mapper.auth.RoleMapper;
import com.bsoft.examination.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 角色业务类
 * @author Artolia Pendragon
 * @version 1.0.0
 */
@Service
public class RoleService {

    private final RoleMapper roleMapper;

    private final UserInfo userInfo;

    public RoleService(UserInfo userInfo, RoleMapper roleMapper) {
        this.userInfo = userInfo;
        this.roleMapper = roleMapper;
    }

    /**
     * 获取角色列表
     * @param params 参数
     * @return Result
     */
    public Result getRoleList(Map<String, Object> params) {
        Result<List<Role>> result = new Result<>();
        try {
            String pageNum = (String) params.get("page");
            String pageSize = (String) params.get("pageSize");
            Page<Role> page = new Page<>(1, 10);

            if (StringUtils.isNoneBlank(pageNum, pageNum)) {
                page.setCurrent(Long.parseLong(pageNum));
                page.setSize(Long.parseLong(pageSize));
            }
            IPage<Role> roleIPage = roleMapper.getRoleList(page);
            long total = roleIPage.getTotal();
            List<Role> roleList = roleIPage.getRecords();
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
            result.setData(roleList);
            result.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 保存或更新
     * @param role 角色实体
     * @param op 操作
     * @return Result
     */
    public Result save(Role role, String op) {
        Result<Role> result = new Result<>();

        try {
            if ("create".equals(op)) {
                QueryWrapper<Role> query = new QueryWrapper<>();
                query.eq("role_name", role.getRoleName());
                int num = roleMapper.selectCount(query);
                if (num > 0) {
                    result.setCode(HttpStatus.NO_CONTENT);
                    result.setMessage("角色名已存在");
                    return result;
                }
                role.setCreateUser(userInfo.getUsername());
                role.setRoleId(UUIDUtil.generateTimeUUID());
                roleMapper.save(role);
                result.setCode(HttpStatus.OK);
                result.setMessage("保存成功");
                result.setData(role);
            } else {
                roleMapper.updateById(role);
                result.setCode(HttpStatus.OK);
                result.setMessage("保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 根据用户id获取角色
     * @param userId 用户id
     * @return Result
     */
    public Result getRolesByUser(String userId) {
        Result<List<Role>> result = new Result<>();

        try {
            List<Role> roleList = roleMapper.getRolesByUser(userId);
            result.setData(roleList);
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 删除角色
     * @param roleId 角色id
     * @return Result
     */
    public Result delete(String roleId) {
        Result result = new Result();

        try {
            roleMapper.deleteById(roleId);
            result.setCode(HttpStatus.OK);
            result.setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 保存角色菜单配置
     * @param role 角色实体
     * @return Result
     */
    public Result saveResource(Role role) {
        Result result = new Result();

        try {
            if (role.getRoleId() != null) {
                roleMapper.deleteResource(role.getRoleId());
                if (role.getResource() != null && role.getResource().size() > 0) {
                    roleMapper.saveResource(role);
                }
                result.setCode(HttpStatus.OK);
                result.setMessage("保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }
}
