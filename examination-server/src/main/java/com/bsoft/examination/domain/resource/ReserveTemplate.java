package com.bsoft.examination.domain.resource;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 预约资源模板
 * @author artolia
 */
@Data
@TableName("ReserveTemplate")
public class ReserveTemplate {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 预约项目
     */
    private String checkItem;

    /**
     * 星期
     */
    private String week;

    /**
     * 预约时段
     */
    private String timeSlot;

    /**
     * 总限额
     */
    private Integer totalLimit;

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
