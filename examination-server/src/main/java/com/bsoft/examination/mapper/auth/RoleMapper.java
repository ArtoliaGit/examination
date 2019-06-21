package com.bsoft.examination.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.domain.auth.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色mapper
 * @author artolia
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 保存角色
     * @param role 角色实体
     * @return int
     */
    int save(Role role);

    /**
     * 获取资源列表
     * @param roleId 角色id
     * @return List
     */
    List<String> getResource(@Param("role_id") String roleId);

    /**
     * 根据用户获取角色
     * @param userId 用户id
     * @return List
     */
    List<Role> getRolesByUser(@Param("user_id") String userId);

    /**
     * 获取用户角色列表
     * @return IPage
     */
    IPage<Role> getRoleList(Page page);

    /**
     * 删除菜单
     */
    int deleteResource(String roleId);

    /**
     * 保存菜单
     * @param role 角色实体
     * @return int
     */
    int saveResource(Role role);
}
