package com.bsoft.examination.service.resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.common.auth.UserInfo;
import com.bsoft.examination.domain.auth.User;
import com.bsoft.examination.domain.resource.ReserveResource;
import com.bsoft.examination.domain.resource.ReserveTime;
import com.bsoft.examination.mapper.resource.CheckItemMapper;
import com.bsoft.examination.mapper.resource.ReserveResourceMapper;
import com.bsoft.examination.mapper.resource.ReserveTimeMapper;
import com.bsoft.examination.service.base.BaseService;
import com.bsoft.examination.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预约资源业务类
 * @author artolia
 */
@Service
public class ReserveResourceService extends BaseService<ReserveResource, ReserveResourceMapper> {

    private final ReserveResourceMapper reserveResourceMapper;

    private final CheckItemMapper checkItemMapper;

    private final ReserveTimeMapper reserveTimeMapper;

    private final UserInfo userInfo;

    public ReserveResourceService(
            ReserveResourceMapper reserveResourceMapper,
            CheckItemMapper checkItemMapper,
            ReserveTimeMapper reserveTimeMapper,
            UserInfo userInfo
    ) {
        this.reserveResourceMapper = reserveResourceMapper;
        this.checkItemMapper = checkItemMapper;
        this.reserveTimeMapper = reserveTimeMapper;
        this.userInfo = userInfo;
    }

    private ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

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

    /**
     * 获取预约资源列表
     * @param checkItem 检查项目
     * @return Result
     */
    public Result getTableList(String checkItem) {
        Result<Map<String, Object>> result = new Result<>();
        try {
            int days = checkItemMapper.getDays(new String[]{checkItem});
            LocalDate localDate = LocalDate.now();
            List<Map<String, Object>> headers = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("M.d");
            for (int i = 0; i <= days; i++) {
                Map<String, Object> map = new HashMap<>();
                LocalDate plusDate = localDate.plusDays(i);
                String strPlusDate = plusDate.format(formatter);
                map.put("date", plusDate);
                map.put("field", "field" + strPlusDate);
                switch (plusDate.getDayOfWeek().getValue()) {
                    case 1:
                        map.put("week", "周一(" + plusDate.format(formatter1) + ")");
                        break;
                    case 2:
                        map.put("week", "周二(" + plusDate.format(formatter1) + ")");
                        break;
                    case 3:
                        map.put("week", "周三(" + plusDate.format(formatter1) + ")");
                        break;
                    case 4:
                        map.put("week", "周四(" + plusDate.format(formatter1) + ")");
                        break;
                    case 5:
                        map.put("week", "周五(" + plusDate.format(formatter1) + ")");
                        break;
                    case 6:
                        map.put("week", "周六(" + plusDate.format(formatter1) + ")");
                        break;
                    case 7:
                        map.put("week", "周日(" + plusDate.format(formatter1) + ")");
                        break;
                    default:
                        break;
                }
                headers.add(map);
            }

            QueryWrapper<ReserveResource> wrapper = new QueryWrapper<>();
            wrapper.eq("check_item", checkItem);
            wrapper.gt("reserve_date", DateUtil.localDate2Date(LocalDate.now().plusDays(-1)));
            List<ReserveResource> resources = reserveResourceMapper.selectList(wrapper);

            QueryWrapper<ReserveTime> timeQueryWrapper = new QueryWrapper<>();
            timeQueryWrapper.orderByAsc("start_time");
            List<ReserveTime> times = reserveTimeMapper.selectList(timeQueryWrapper);

            List<Map<String, Object>> resourceList = times.parallelStream().map(
                    item -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("timeSlot", item.getId());
                        resources.parallelStream().filter(x -> item.getId().equals(x.getTimeSlot())).forEach(
                                y -> map.put("field" + sdf.get().format(y.getReserveDate()), y.getTotalLimit())
                        );
                        return map;
                    }
            ).collect(Collectors.toList());

            Map<String, Object> map = new HashMap<>();
            map.put("headers", headers);
            map.put("data", resourceList);
            result.setData(map);
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("查询失败");
        }
        return result;
    }

    /**
     * 批量保存
     * @return Result
     */
    public Result batchSave(List<ReserveResource> maps) {
        Result result = new Result();
        try {
            batchSaveData(maps);
            result.setCode(HttpStatus.OK);
            result.setMessage("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("保存失败");
        }
        return result;
    }

    @Transactional
    void batchSaveData(List<ReserveResource> maps) {
        Date date = new Date();
        User user = userInfo.getUser();
        maps.parallelStream().forEach(item -> {
            item.setCreateTime(date);
            item.setCreateUnit(user.getOrgancode());
            item.setCreateUser(user.getUsername());
        });
        for (ReserveResource reserveResource : maps) {
            QueryWrapper<ReserveResource> wrapper = new QueryWrapper<>();
            wrapper.eq("time_slot", reserveResource.getTimeSlot());
            wrapper.eq("reserve_date", reserveResource.getReserveDate());
            wrapper.eq("check_item", reserveResource.getCheckItem());
            int num = reserveResourceMapper.update(reserveResource, wrapper);
            if (num == 0) {
                reserveResourceMapper.insert(reserveResource);
            }
        }
    }

    /**
     * 根据条件获取预约资源
     * @param params 参数
     * @return Result
     */
    public Result getResourceByConditions(Map<String, Object> params) {
        Result<List<ReserveResource>> result = new Result<>();
        try {
            List<ReserveResource> list = reserveResourceMapper.selectByMap(params);
            result.setCode(HttpStatus.OK);
            result.setData(list);
            result.setMessage("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    @Override
    public ReserveResourceMapper getBaseMapper() {
        return reserveResourceMapper;
    }
}
