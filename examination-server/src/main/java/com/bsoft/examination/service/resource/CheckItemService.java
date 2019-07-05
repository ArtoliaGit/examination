package com.bsoft.examination.service.resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.resource.CheckItem;
import com.bsoft.examination.mapper.resource.CheckItemMapper;
import com.bsoft.examination.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * 根据条件查询检查项目
     * @param params 条件
     * @return Result
     */
    public Result getCheckItemByConditions(Map<String, Object> params) {
        Result<List<CheckItem>> result = new Result<>();

        try {
            List<CheckItem> list = checkItemMapper.selectByMap(params);
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("查询失败");
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @Override
    public CheckItemMapper getBaseMapper() {
        return checkItemMapper;
    }
}
