package com.bsoft.examination.web.resource;

import com.bsoft.examination.domain.resource.ReserveTime;
import com.bsoft.examination.service.resource.ReserveTimeService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 预约时段controller
 */
@RestController
@RequestMapping("reserveTime")
public class ReserveTimeController {

    private final ReserveTimeService reserveTimeService;

    public ReserveTimeController(ReserveTimeService reserveTimeService) {
        this.reserveTimeService = reserveTimeService;
    }

    /**
     * 保存或更新预约时段
     * @param reserveTime 预约时段实体类
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody ReserveTime reserveTime) {
        return reserveTimeService.saveOrUpdate(reserveTime).toJson();
    }

    /**
     * 获取预约时段列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getList")
    public String getList(HttpServletRequest request) {
        return reserveTimeService.getList(RequestParamPaser.getParameters(request)).toJson();
    }

    /**
     * 删除预约时段
     * @param id 预约时段id
     * @return String
     */
    @GetMapping("/delete")
    public String delete(String id) {
        return reserveTimeService.removeById(id).toJson();
    }

    /**
     * 获取所有时段
     */
    @GetMapping("/getAllList")
    public String getAllList() {
        return reserveTimeService.list().toJson();
    }
}
