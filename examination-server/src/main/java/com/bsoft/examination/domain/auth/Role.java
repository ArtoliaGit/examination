package com.bsoft.examination.domain.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 角色实体类
 */
@Data
@TableName(value = "role")
public class Role {

    /**
     * 角色id
     */
    @TableId(value = "role_id", type = IdType.UUID)
    private String roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDescription;

    /**
     * 维护时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 维护人
     */
    private String createUser;

    /**
     * 维护机构
     */
    private String createUnit;

    /**
     * 菜单列表
     */
    private List<String> resource;
}
