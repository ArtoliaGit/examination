package com.bsoft.examination.mapper.resource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsoft.examination.domain.resource.ReserveResource;

import java.util.List;
import java.util.Map;

/**
 * 预约资源mapper
 * @author artolia
 */
public interface ReserveResourceMapper extends BaseMapper<ReserveResource> {

    List<Map<String, Object>> getReserveResourceByCheckItem(Map<String, Object> map);
}
