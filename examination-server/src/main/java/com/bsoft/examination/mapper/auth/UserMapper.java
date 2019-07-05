package com.bsoft.examination.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.domain.auth.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 用户mapper
 * @author Artolia Pendragon
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return User
     */
    User findByUsername(String username);

    /**
     * 保存用户
     * @param user 用户实体
     * @return Integer
     */
    Integer save(User user);

    /**
     * 获取用户列表
     * @param page 分页对象
     * @return Ipage
     */
    IPage<User> getUserList(Page page, @Param("params") Map<String, Object> map);

    /**
     * 保存用户角色
     * @param user 用户
     * @return int
     */
    int saveRoles(User user);

    /**
     * 删除用户角色
     */
    int deleteRoles(User user);

}
