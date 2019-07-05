package com.bsoft.examination.mapper.resource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsoft.examination.domain.resource.CheckItem;

/**
 * 检查项目mapper
 * @author artolia
 */
public interface CheckItemMapper extends BaseMapper<CheckItem> {

    /**
     * 获取可预约天数
     * @param code 检查项目code
     * @return int
     */
    Integer getDays(String[] code);
}
