package com.bsoft.examination.service.resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.common.auth.UserInfo;
import com.bsoft.examination.domain.auth.User;
import com.bsoft.examination.domain.resource.ReserveResource;
import com.bsoft.examination.domain.resource.ReserveTemplate;
import com.bsoft.examination.domain.resource.ReserveTime;
import com.bsoft.examination.mapper.resource.CheckItemMapper;
import com.bsoft.examination.mapper.resource.ReserveTemplateMapper;
import com.bsoft.examination.mapper.resource.ReserveTimeMapper;
import com.bsoft.examination.service.base.BaseService;
import com.bsoft.examination.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源模板
 * @author artolia
 */
@Slf4j
@Service
public class ReserveTemplateService extends BaseService<ReserveTemplate, ReserveTemplateMapper> {

    private final ReserveTemplateMapper reserveTemplateMapper;

    private final CheckItemMapper checkItemMapper;

    private final UserInfo userInfo;

    private final ReserveResourceService reserveResourceService;

    private final ReserveTimeMapper reserveTimeMapper;

    public ReserveTemplateService(ReserveTemplateMapper reserveTemplateMapper,
                                  CheckItemMapper checkItemMapper,
                                  UserInfo userInfo,
                                  ReserveResourceService reserveResourceService,
                                  ReserveTimeMapper reserveTimeMapper) {
        this.reserveTemplateMapper = reserveTemplateMapper;
        this.checkItemMapper = checkItemMapper;
        this.userInfo = userInfo;
        this.reserveResourceService = reserveResourceService;
        this.reserveTimeMapper = reserveTimeMapper;
    }

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

    /**
     * 获取模板列表
     * @param checkItem 检查项目
     * @return Result
     */
    public Result getTableList(String checkItem) {
        Result<List<Map<String, Object>>> result = new  Result<>();
        try {
            QueryWrapper<ReserveTemplate> templateQueryWrapper = new QueryWrapper<>();
            templateQueryWrapper.eq("check_item", checkItem);
            List<ReserveTemplate> templates = reserveTemplateMapper.selectList(templateQueryWrapper);
            QueryWrapper<ReserveTime> timeQueryWrapper = new QueryWrapper<>();
            timeQueryWrapper.orderByAsc("start_time");
            List<ReserveTime> times = reserveTimeMapper.selectList(timeQueryWrapper);
            List<Map<String, Object>> list = times.parallelStream().map(item -> {
                Map<String, Object> map = new HashMap<>();
                map.put("timeSlot", item.getId());
                templates.parallelStream().filter(x -> item.getId().equals(x.getTimeSlot())).forEach(
                        y -> {
                            switch (y.getWeek().intValue()) {
                                case 1:
                                    map.put("monday", y.getTotalLimit());
                                    break;
                                case 2:
                                    map.put("tuesday", y.getTotalLimit());
                                    break;
                                case 3:
                                    map.put("wednesday", y.getTotalLimit());
                                    break;
                                case 4:
                                    map.put("thursday", y.getTotalLimit());
                                    break;
                                case 5:
                                    map.put("friday", y.getTotalLimit());
                                    break;
                                case 6:
                                    map.put("saturday", y.getTotalLimit());
                                    break;
                                case 7:
                                    map.put("sunday", y.getTotalLimit());
                                    break;
                                default:
                                    break;
                            }
                        }
                );
                return map;
            }).collect(Collectors.toList());
            result.setData(list);
            result.setCode(HttpStatus.OK);
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
    public Result batchSave(List<ReserveTemplate> maps) {
        Result result = new Result();
        try {
            Date date = new Date();
            User user = userInfo.getUser();
            maps.parallelStream().forEach(item -> {
                item.setCreateTime(date);
                item.setCreateUnit(user.getOrgancode());
                item.setCreateUser(user.getUsername());
            });
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
    void batchSaveData(List<ReserveTemplate> maps) {
        for (ReserveTemplate reserveTemplate : maps) {
            QueryWrapper<ReserveTemplate> wrapper = new QueryWrapper<>();
            wrapper.eq("time_slot", reserveTemplate.getTimeSlot());
            wrapper.eq("week", reserveTemplate.getWeek());
            wrapper.eq("check_item", reserveTemplate.getCheckItem());
            int num = reserveTemplateMapper.update(reserveTemplate, wrapper);
            if (num == 0) {
                reserveTemplateMapper.insert(reserveTemplate);
            }
        }
    }

    /**
     * 初始化预约资源
     * @return Result
     */
    public Result initReserveResource(String checkItem) {
        Result result = new Result();
        try {
            List<ReserveTemplate> list = reserveTemplateMapper.getUnInitReserveResource(checkItem);
            int days = checkItemMapper.getDays(new String[]{checkItem});
            List<ReserveResource> initList = new ArrayList<>(30);

            LocalDate now = LocalDate.now();
            for (int i = 1; i <= days; i++) {
                LocalDate plusDate = now.plusDays(i);
                long week = plusDate.getDayOfWeek().getValue();
                initList.addAll(list.stream().filter(item -> item.getWeek() == week).map(item -> {
                    ReserveResource reserveResource = new ReserveResource();
                    reserveResource.setCheckItem(checkItem);
                    reserveResource.setTimeSlot(item.getTimeSlot());
                    reserveResource.setTotalLimit(item.getTotalLimit());
                    reserveResource.setAvailableLimit(item.getTotalLimit());
                    reserveResource.setReserveDate(DateUtil.localDate2Date(plusDate));
                    reserveResource.setCreateUnit(userInfo.getOrgan());
                    reserveResource.setCreateUser(userInfo.getUsername());
                    return reserveResource;
                }).collect(Collectors.toList()));
            }
            if (initList.size() == 0) {
                result.setCode(HttpStatus.NO_CONTENT);
                result.setMessage("没有数据需要初始化");
            } else {
                boolean successFlag = reserveResourceService.saveOrUpdateBatch(initList, 100);
                if (successFlag) {
                    Date initDate = new Date();
                    list = list.parallelStream().peek(item -> {
                        item.setInitFlag("1");
                        item.setInitDate(initDate);
                    }).collect(Collectors.toList());
                    saveOrUpdateBatch(list, 100);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("初始化成功");
                } else {
                    result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    result.setMessage("初始化失败");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("初始化失败");
        }
        return result;
    }

    /**
     * 复制预约资源
     */
    public void rotateReserveResource() {
        try {
            List<ReserveResource> list = reserveTemplateMapper.getRotateReserveResource();
            if (list.size() > 0) {
                boolean successFlag = reserveResourceService.saveOrUpdateBatch(list, 100);
                if (successFlag) {
                    log.info("=======================预约资源复制成功======================");
                } else {
                    log.error("======================预约资源复制失败=======================");
                }
            } else {
                log.info("============================没有资源被复制===========================");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("======================预约资源复制失败=======================");
        }
    }

    @Override
    public ReserveTemplateMapper getBaseMapper() {
        return reserveTemplateMapper;
    }
}
