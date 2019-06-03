package com.bsoft.examination.domain.resource;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 检查项目实体类
 * @author artolia
 */
@Data
@TableName("CheckItem")
public class CheckItem {

    /**
     * 项目编号
     */
    @TableId(value = "code", type = IdType.UUID)
    private String code;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 可预约天数
     */
    private Integer days;

    /**
     * 性别
     */
    private String gender;

    /**
     * 预约方式
     */
    private String way;

    /**
     * 归属执行科室
     */
    private String dept;

    /**
     * 执行地点
     */
    private String address;

    /**
     * 有效标志
     */
    private String status;

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
