package com.bsoft.examination.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.gson.annotations.SerializedName;

/**
 * 用户状态
 * @author Artolia Pendragon
 * @version 1.0.0
 */
public enum StatusEnum implements IEnum<String> {
    @SerializedName("正常")
    ONE("1", "正常"),
    @SerializedName("注销")
    ZERO("0", "注销");

    private String value;
    private String desc;

    StatusEnum(String value, String desc) {
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
    public static StatusEnum formValue(String value) {
        for (StatusEnum statusEnum : values()) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
