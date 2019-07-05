package com.bsoft.examination.mapper.reservation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsoft.examination.domain.reservation.Reserve;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预约信息mapper
 * @author artolia
 */
public interface ReserveMapper extends BaseMapper<Reserve> {

    /**
     * 从his中获取人员信息
     * @param applyNo 申请单号
     * @param type 类型
     * @return 人员信息
     */
    List<Map<String, Object>> getReservePersonInfo(
            @Param("applyNo") String applyNo, @Param("type") String type);

    /**
     * 获取当天最大id值
     * @param date 日期
     */
    String getMaxId(String date);

    /**
     * 更新状态
     * @param id 预约号
     */
    int updateStatus(String id);
}
