package com.bsoft.examination.service.resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.resource.ReserveTime;
import com.bsoft.examination.mapper.resource.ReserveTimeMapper;
import com.bsoft.examination.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预约时段service
 * @author artolia
 */
@Service
public class ReserveTimeService extends BaseService<ReserveTime, ReserveTimeMapper> {

    @Resource
    private ReserveTimeMapper reserveTimeMapper;

    /**
     * 获取预约时段列表
     * @return Result
     */
    public Result getList(Map<String, Object> params) {
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("pageSize");
        Page<ReserveTime> page = new Page<>(1, 10);

        if (StringUtils.isNoneBlank(pageNum, pageNum)) {
            page.setCurrent(Long.parseLong(pageNum));
            page.setSize(Long.parseLong(pageSize));
        }
        return super.page(page);
    }

    @Override
    public ReserveTimeMapper getBaseMapper() {
        return reserveTimeMapper;
    }
}
