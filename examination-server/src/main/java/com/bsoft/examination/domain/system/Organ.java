package com.bsoft.examination.domain.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 机构实体类
 */
@Data
@TableName("organ")
public class Organ {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 机构编码
     */
    private String organcode;

    /**
     * 机构
     */
    private String organname;

    /**
     * 数据库名
     */
    private String name;

    /**
     * 数据库驱动类型
     */
    private String driverclass;

    /**
     * 数据库ip地址
     */
    private String ip;

    /**
     * 数据库端口
     */
    private String port;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 类型 1:中心 2:下属站
     */
    private String type;

    /**
     * 维护人
     */
    private String createUser;

    /**
     * 维护时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 维护机构
     */
    private String createUnit;
}
