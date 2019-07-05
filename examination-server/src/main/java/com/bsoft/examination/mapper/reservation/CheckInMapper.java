package com.bsoft.examination.mapper.reservation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsoft.examination.domain.reservation.CheckIn;

import java.util.List;
import java.util.Map;

/**
 * 预约报到mapper
 */
public interface CheckInMapper extends BaseMapper<CheckIn> {

    List<CheckIn> getList(Map<String, Object> map);

    List<CheckIn> getAlwaysCallList(Map<String, Object> map);
}
