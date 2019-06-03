package com.bsoft.examination.domain.resource;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 预约时段
 * @author artolia
 */
@Data
@TableName("ReserveTime")
public class ReserveTime {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 预约时段
     */
    private String timeSlot;

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
