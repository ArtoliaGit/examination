package com.bsoft.examination.service.resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.resource.Limb;
import com.bsoft.examination.mapper.resource.LimbMapper;
import com.bsoft.examination.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 部位信息业务类
 * @author artolia
 */
@Service
public class LimbService extends BaseService<Limb, LimbMapper> {

    private final LimbMapper limbMapper;

    public LimbService(LimbMapper limbMapper) {
        this.limbMapper = limbMapper;
    }

    /**
     * 获取部位列表
     * @return Result
     */
    public Result getList(Map<String, Object> params) {
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("pageSize");
        Page<Limb> page = new Page<>(1, 10);

        if (StringUtils.isNoneBlank(pageNum, pageNum)) {
            page.setCurrent(Long.parseLong(pageNum));
            page.setSize(Long.parseLong(pageSize));
        }
        return super.page(page);
    }

    /**
     * 根据检查项目获取部位
     * @param checkItem 检查项目
     * @return Result
     */
    public Result getListByCheckItem(String checkItem) {
        Result<List<Limb>> result = new Result<>();

        try {
            QueryWrapper<Limb> wrapper = new QueryWrapper<>();
            wrapper.eq("cid", checkItem);
            List<Limb> list = limbMapper.selectList(wrapper);
            result.setCode(HttpStatus.OK);
            result.setData(list);
            result.setMessage("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("查询失败");
        }
        return result;
    }

    @Override
    public LimbMapper getBaseMapper() {
        return limbMapper;
    }
}
