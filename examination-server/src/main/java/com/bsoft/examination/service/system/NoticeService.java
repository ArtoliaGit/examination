package com.bsoft.examination.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.system.Notice;
import com.bsoft.examination.mapper.system.NoticeMapper;
import com.bsoft.examination.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 提示信息业务类
 * @author artolia
 */
@Service
public class NoticeService extends BaseService<Notice, NoticeMapper> {

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 获取提示信息列表
     * @return Result
     */
    public Result getList(Map<String, Object> params) {
        String pageNum = (String) params.get("page");
        String pageSize = (String) params.get("pageSize");
        Page<Notice> page = new Page<>(1, 10);

        if (StringUtils.isNoneBlank(pageNum, pageNum)) {
            page.setCurrent(Long.parseLong(pageNum));
            page.setSize(Long.parseLong(pageSize));
        }
        return super.page(page);
    }

    @Override
    public NoticeMapper getBaseMapper() {
        return noticeMapper;
    }
}
