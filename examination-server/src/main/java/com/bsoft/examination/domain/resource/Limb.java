package com.bsoft.examination.domain.resource;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 部位信息
 * @author artolia
 */
@Data
@TableName("limb")
public class Limb {

    /**
     * 部位编号
     */
    @TableId(value = "lid", type = IdType.UUID)
    private String lid;

    /**
     * 项目编号
     */
    private String cid;

    /**
     * 部位名称
     */
    private String name;

    /**
     * 预约占用部位基数
     */
    private Integer num;

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
