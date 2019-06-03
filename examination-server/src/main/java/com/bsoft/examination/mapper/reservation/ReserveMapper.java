package com.bsoft.examination.mapper.reservation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsoft.examination.domain.reservation.Reserve;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预约信息mapper
 * @author artolia
 */
@Mapper
public interface ReserveMapper extends BaseMapper<Reserve> {
}
