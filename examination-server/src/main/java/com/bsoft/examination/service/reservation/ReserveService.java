package com.bsoft.examination.service.reservation;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.domain.reservation.Reserve;
import com.bsoft.examination.mapper.reservation.ReserveMapper;
import com.bsoft.examination.mapper.resource.CheckItemMapper;
import com.bsoft.examination.mapper.resource.ReserveResourceMapper;
import com.bsoft.examination.service.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 预约信息service
 * @author artolia
 */
@Service
public class ReserveService extends BaseService<Reserve, ReserveMapper> {

    private final ReserveMapper reserveMapper;

    private final CheckItemMapper checkItemMapper;

    private final ReserveResourceMapper reserveResourceMapper;

    public ReserveService(
            ReserveMapper reserveMapper,
            CheckItemMapper checkItemMapper,
            ReserveResourceMapper reserveResourceMapper
    ) {
        this.reserveMapper = reserveMapper;
        this.checkItemMapper = checkItemMapper;
        this.reserveResourceMapper = reserveResourceMapper;
    }

    /**
     * 获取预约信息列表
     * @return Result
     */
    public Result getList(Map<String, Object> params) {
        Result<List<Reserve>> result = new Result<>();

        try {
            QueryWrapper<Reserve> wrapper = new QueryWrapper<>();
            wrapper.nested(i -> i.eq("apply_no", params.get("apply_no"))
                    .or().eq("mzhm", params.get("apply_no")))
                    .and(i -> i.eq("status", "0"));
            List<Reserve> list = reserveMapper.selectList(wrapper);
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
     * 获取可预约日期列表
     * @param checkItem 检查项目
     * @return Result
     */
    public Result getReserveCalendar(String checkItem) {
        Result<List<Map<String, Object>>> result = new Result<>();
        try {
            Integer days = checkItemMapper.getDays(checkItem.split(","));
            LocalDate localDate = LocalDate.now();
            List<Map<String, Object>> list = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (int i = 1; i <= days; i++) {
                LocalDate plusDate = localDate.plusDays(i);
                Map<String, Object> map = new HashMap<>(8);
                map.put("year", plusDate.getYear());
                map.put("month", plusDate.getMonthValue());
                map.put("day", plusDate.getDayOfMonth());
                map.put("date", plusDate.format(formatter));
                list.add(map);
            }
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

    /**
     * 获取预约时段信息
     * @param checkItem 检查项目
     * @param reserveDate 预约日期
     * @return Result
     */
    public Result getTimeSlot(String checkItem, String reserveDate) {
        Result<List<Map<String, Object>>> result = new Result<>();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("checkItems", checkItem.split(","));
            map.put("reserveDate", reserveDate);
            List<Map<String, Object>> list = reserveResourceMapper.getReserveResourceByCheckItem(map);
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
     * 从his数据库获取预约信息
     * @param applyNo 申请单号
     * @return Result
     */
    @DS("#his")
    public Result getReservePersonInfo(String applyNo, String type) {
        Result<List<Map<String, Object>>> result = new Result<>();

        try {
            List<Map<String, Object>> list = reserveMapper.getReservePersonInfo(applyNo, type);
            result.setCode(HttpStatus.OK);
            result.setData(list);
            result.setMessage("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("查询失败");
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /**
     * 保存预约信息
     * @param entity 预约信息
     * @return Result
     */
    @Transactional
    public Result save(Reserve entity) {
        if (StringUtils.isNotBlank(entity.getId())) {
            reserveMapper.updateStatus(entity.getId());
        }
        String id;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String date = LocalDate.now().format(formatter);
        String maxId = reserveMapper.getMaxId(date);
        String surfix = "000000001";
        if (StringUtils.isNotBlank(maxId)) {
            surfix = String.format("%09d", Integer.parseInt(maxId.substring(6)) + 1);
        }
        id = date + surfix;
        entity.setId(id);
        entity.setCreateTime(new Date());
        Result result = super.saveOrUpdate(entity);
        result.setData(id);
        return result;
    }

    /**
     * 退约
     * @param id 预约号
     * @return Result
     */
    public Result cancelReserve(String id) {
        Result result = new Result();
        try {
            reserveMapper.updateStatus(id);
            result.setCode(HttpStatus.OK);
            result.setMessage("退约成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("退约失败");
        }
        return result;
    }

    @Override
    public ReserveMapper getBaseMapper() {
        return reserveMapper;
    }

}
