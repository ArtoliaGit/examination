package com.bsoft.examination.service.resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.resource.ReserveTemplate;
import com.bsoft.examination.mapper.resource.ReserveTemplateMapper;
import com.bsoft.examination.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 资源模板
 * @author artolia
 */
@Service
public class ReserveTemplateService extends BaseService<ReserveTemplate, ReserveTemplateMapper> {

    @Resource
    private ReserveTemplateMapper reserveTemplateMapper;

    /**
     * 获取资源模板
     * @return Result
     */
    public Result getList(Map<String, Object> params) {
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("pageSize");
        Page<ReserveTemplate> page = new Page<>(1, 10);

        if (StringUtils.isNoneBlank(pageNum, pageNum)) {
            page.setCurrent(Long.parseLong(pageNum));
            page.setSize(Long.parseLong(pageSize));
        }
        return super.page(page);
    }

    @Override
    public ReserveTemplateMapper getBaseMapper() {
        return reserveTemplateMapper;
    }
}
