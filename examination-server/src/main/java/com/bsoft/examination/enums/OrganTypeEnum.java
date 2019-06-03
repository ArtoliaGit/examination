package com.bsoft.examination.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.gson.annotations.SerializedName;

/**
 * 机构类型
 * @author artolia
 */
public enum OrganTypeEnum implements IEnum<String> {
    @SerializedName("中心")
    ONE("1", "中心"),
    @SerializedName("下属站")
    TWO("2", "下属站");

    private String value;
    private String desc;

    OrganTypeEnum(String value, String desc) {
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
    public static OrganTypeEnum formValue(String value) {
        for (OrganTypeEnum organTypeEnum : values()) {
            if (organTypeEnum.getValue().equals(value)) {
                return organTypeEnum;
            }
        }
        return null;
    }
}
