package com.bsoft.examination.mapper.resource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsoft.examination.domain.resource.ReserveResource;
import com.bsoft.examination.domain.resource.ReserveTemplate;

import java.util.List;

/**
 * 资源模板mapper
 * @author artolia
 */
public interface ReserveTemplateMapper extends BaseMapper<ReserveTemplate> {

    /**
     * 获取未初始化的资源
     * @param checkItem 检查项目
     * @return List<ReserveTemplate>
     */
    List<ReserveTemplate> getUnInitReserveResource(String checkItem);

    /**
     * 获取需要复制的预约模板
     * @return List<ReserveTemplate>
     */
    List<ReserveResource> getRotateReserveResource();
}
