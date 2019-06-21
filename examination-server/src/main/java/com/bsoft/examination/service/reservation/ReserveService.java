package com.bsoft.examination.service.reservation;

import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.reservation.Reserve;
import com.bsoft.examination.mapper.reservation.ReserveMapper;
import com.bsoft.examination.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 预约信息service
 * @author artolia
 */
@Service
public class ReserveService extends BaseService<Reserve, ReserveMapper> {

    private final ReserveMapper reserveMapper;

    public ReserveService(ReserveMapper reserveMapper) {
        this.reserveMapper = reserveMapper;
    }

    /**
     * 获取预约信息列表
     * @return Result
     */
    public Result getList(Map<String, Object> params) {
        Result result = new Result();

        return result;
    }

    @Override
    public ReserveMapper getBaseMapper() {
        return reserveMapper;
    }
}
