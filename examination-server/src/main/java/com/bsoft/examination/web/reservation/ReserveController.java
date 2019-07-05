package com.bsoft.examination.web.reservation;

import com.bsoft.examination.domain.reservation.Reserve;
import com.bsoft.examination.service.reservation.ReserveService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 预约信息controller
 * @author artolia
 */
@RestController
@RequestMapping("reserve")
public class ReserveController {

    private final ReserveService reserveService;

    public ReserveController(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    /**
     * 保存或更新预约信息
     * @param reserve 预约信息实体类
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody Reserve reserve) {
        return reserveService.save(reserve).toJson();
    }

    /**
     * 获取预约信息列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getList")
    public String getList(HttpServletRequest request) {
        return reserveService.getList(RequestParamPaser.getParameters(request)).toJson();
    }

    /**
     * 删除预约信息
     * @param id 预约信息id
     * @return String
     */
    @GetMapping("/delete")
    public String delete(String id) {
        return reserveService.removeById(id).toJson();
    }

    /**
     * 获取可预约日期列表
     * @param checkItem 检查项目
     * @return String
     */
    @GetMapping("/getReserveCalendar")
    public String getReserveCalendar(String checkItem) {
        return reserveService.getReserveCalendar(checkItem).toJson();
    }

    /**
     * 获取预约时段信息
     * @param checkItem 检查项目
     * @param reserveDate 预约日期
     * @return String
     */
    @GetMapping("/getTimeSlot")
    public String getTimeSlot(String checkItem, String reserveDate) {
        return reserveService.getTimeSlot(checkItem, reserveDate).toJson();
    }

    /**
     * 从his数据库获取预约信息
     * @param applyNo 申请单号
     * @return String
     */
    @GetMapping("/getReservePersonInfo")
    public String getReservePersonInfo(String applyNo, String type) {
        return reserveService.getReservePersonInfo(applyNo, type).toJson();
    }

    /**
     * 退约
     * @param id 预约号
     * @return String
     */
    @GetMapping("/cancelReserve")
    public String cancelReserve(String id) {
        return reserveService.cancelReserve(id).toJson();
    }
}
