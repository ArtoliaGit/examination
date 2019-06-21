package com.bsoft.examination.service.resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.resource.CheckItem;
import com.bsoft.examination.mapper.resource.CheckItemMapper;
import com.bsoft.examination.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 检查项目业务类
 * @author artolia
 */
@Service
public class CheckItemService extends BaseService<CheckItem, CheckItemMapper> {

    private final CheckItemMapper checkItemMapper;

    public CheckItemService(CheckItemMapper checkItemMapper) {
        this.checkItemMapper = checkItemMapper;
    }

    /**
     * 获取检查项目列表
     * @return Result
     */
    public Result getList(Map<String, Object> params) {
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("pageSize");
        Page<CheckItem> page = new Page<>(1, 10);

        if (StringUtils.isNoneBlank(pageNum, pageNum)) {
            page.setCurrent(Long.parseLong(pageNum));
            page.setSize(Long.parseLong(pageSize));
        }
        return super.page(page);
    }

    @Override
    public CheckItemMapper getBaseMapper() {
        return checkItemMapper;
    }
}
