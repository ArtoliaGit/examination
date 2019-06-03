package com.bsoft.examination.service.system;

import com.bsoft.examination.domain.system.ReserveSetting;
import com.bsoft.examination.mapper.system.ReserveSettingMapper;
import com.bsoft.examination.service.base.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 预约设置
 * @author artolia
 */
@Service
public class ReserveSettingService extends BaseService<ReserveSetting, ReserveSettingMapper> {

    @Resource
    private ReserveSettingMapper reserveSettingMapper;

    @Override
    public ReserveSettingMapper getBaseMapper() {
        return reserveSettingMapper;
    }
}
