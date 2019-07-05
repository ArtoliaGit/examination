package com.bsoft.examination.service.reservation;

import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.reservation.CheckIn;
import com.bsoft.examination.mapper.reservation.CheckInMapper;
import com.bsoft.examination.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预约报到service
 */
@Service
public class CheckInService extends BaseService<CheckIn, CheckInMapper> {

    private final CheckInMapper checkInMapper;

    public CheckInService(CheckInMapper checkInMapper) {
        this.checkInMapper = checkInMapper;
    }

    /**
     * 获取叫号数据
     * @param params 条件
     * @return Result
     */
    public Result getList(Map<String, Object> params) {
        Result<List<CheckIn>> result = new Result<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String startDate = LocalDate.now().format(formatter);
            String endDate = LocalDate.now().plusDays(1).format(formatter);
            params.put("startDate", startDate);
            params.put("endDate", endDate);
            List<CheckIn> checkIns = checkInMapper.getList(params);
            List<CheckIn> list = checkIns.parallelStream()
                    .filter(item -> item.getBdxl().startsWith("V"))
                    .sorted(Comparator.comparing(CheckIn::getBdxl))
                    .collect(Collectors.toList());
            list.addAll(checkIns.parallelStream()
                    .filter(item -> !item.getBdxl().startsWith("V"))
                    .sorted(Comparator.comparing(CheckIn::getBdxl))
                    .collect(Collectors.toList()));
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("查询失败");
        }
        return result;
    }

    /**
     * 获取已呼叫和弃呼的人
     * @return Result
     */
    public Result getAlwaysCallList() {
        Result<Map<String, Object>> result = new Result<>();
        try {
            Map<String, Object> params = new HashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String startDate = LocalDate.now().format(formatter);
            String endDate = LocalDate.now().plusDays(1).format(formatter);
            params.put("startDate", startDate);
            params.put("endDate", endDate);
            params.put("jhzt", "1");
            List<CheckIn> list1 = checkInMapper.getAlwaysCallList(params);
            params.put("jhzt", "3");
            List<CheckIn> list2 = checkInMapper.getAlwaysCallList(params);
            Map<String, Object> map = new HashMap<>();
            map.put("alwaysCall", list1);
            map.put("cancelCall", list2);
            result.setCode(HttpStatus.OK);
            result.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("查询失败");
        }
        return result;
    }

    @Override
    public CheckInMapper getBaseMapper() {
        return checkInMapper;
    }
}
