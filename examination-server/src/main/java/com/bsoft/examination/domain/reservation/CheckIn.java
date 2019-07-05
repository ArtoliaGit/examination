package com.bsoft.examination.domain.reservation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 报到表
 */
@TableName("yy_bdb")
@Data
public class CheckIn {

    /**
     * 报到id
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 报到时间
     */
    private Date bdsj;

    /**
     * 报到序列
     */
    private String bdxl;

    /**
     * 报到时段
     */
    private String bdsd;

    /**
     * 时段序号
     */
    private String sdxh;

    /**
     * 预约号码
     */
    private String yyhm;

    /**
     * 申请单号
     */
    private String applyNo;

    /**
     * 病人id
     */
    private String brid;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private String age;

    /**
     * 患者分类 1、2、3，分别代表门诊、住院、体检
     */
    private String type;

    /**
     * 检查项目编号
     */
    private String code;

    /**
     * 检查项目名称
     */
    private String name;

    /**
     * 部位
     */
    private String checkPart;

    /**
     * 叫号状态 1、已叫号；2、已完成；3、弃呼
     */
    private String jhzt;

    /**
     * 叫号时间
     */
    private Date jhsj;

    /**
     * 完成时间
     */
    private Date wcsj;

    /**
     * 报到机名
     */
    private String bdjm;

    /**
     * 检查地点
     */
    @TableField(exist = false)
    private String address;
}
