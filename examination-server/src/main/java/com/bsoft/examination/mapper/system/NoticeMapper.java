package com.bsoft.examination.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsoft.examination.domain.system.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 提示信息mapper
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
