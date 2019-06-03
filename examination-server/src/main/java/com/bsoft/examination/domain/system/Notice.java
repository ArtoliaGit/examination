package com.bsoft.examination.domain.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 提示信息维护
 */
@Data
@TableName("notice")
public class Notice {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 类型
     */
    private String type;

    /**
     * 检查项目
     */
    private String checkItem;

    /**
     * 提示信息
     */
    private String info;

    /**
     * 排序
     */
    private Integer ord;

    /**
     * 状态
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
