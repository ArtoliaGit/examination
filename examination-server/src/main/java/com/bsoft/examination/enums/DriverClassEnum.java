package com.bsoft.examination.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.gson.annotations.SerializedName;

/**
 * 数据库类型
 * @author artolia
 */
public enum DriverClassEnum implements IEnum<String> {
    @SerializedName("Mysql")
    ONE("1", "Mysql"),
    @SerializedName("Sql Server")
    TWO("2", "Sql Server"),
    @SerializedName("Oracle")
    THREE("3", "Oracle");

    private String value;
    private String desc;

    DriverClassEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.desc;
    }

    @JsonCreator
    public static DriverClassEnum formValue(String value) {
        for (DriverClassEnum driverClassEnum : values()) {
            if (driverClassEnum.getValue().equals(value)) {
                return driverClassEnum;
            }
        }
        return null;
    }
}
