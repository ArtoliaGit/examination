package com.bsoft.examination.domain.reservation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 预约信息
 * @author artolia
 */
@Data
@TableName("reserve")
public class Reserve {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 预约日期
     */
    private String reserveDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 预约时段
     */
    private String timeSlot;

    /**
     * 患者分类
     */
    private String type;

    /**
     * 患者id
     */
    private String brid;

    /**
     * 申请单号
     */
    private String applyNo;

    /**
     * 门诊号、住院号
     */
    private String mzhm;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 预约项目编号
     */
    private String checkItem;

    /**
     * 项目名称
     */
    private String checkItemName;

    /**
     * 归属执行科室
     */
    private String dept;

    /**
     * 部位基数
     */
    private Integer partNum;

    /**
     * 检查部位
     */
    private String checkPart;

    /**
     * 检查状态
     */
    private String checkStatus;

    /**
     * 加急状态
     */
    private String urgentStatus;

    /**
     * 作废状态
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
