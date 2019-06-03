package com.bsoft.examination.service.resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.resource.ReserveResource;
import com.bsoft.examination.mapper.resource.ReserveResourceMapper;
import com.bsoft.examination.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预约资源业务类
 * @author artolia
 */
@Service
public class ReserveResourceService extends BaseService<ReserveResource, ReserveResourceMapper> {

    @Resource
    private ReserveResourceMapper reserveResourceMapper;

    /**
     * 获取预约资源
     * @return Result
     */
    public Result getList(Map<String, Object> params) {
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("pageSize");
        Page<ReserveResource> page = new Page<>(1, 10);

        if (StringUtils.isNoneBlank(pageNum, pageNum)) {
            page.setCurrent(Long.parseLong(pageNum));
            page.setSize(Long.parseLong(pageSize));
        }
        return super.page(page);
    }

    @Override
    public ReserveResourceMapper getBaseMapper() {
        return reserveResourceMapper;
    }
}
