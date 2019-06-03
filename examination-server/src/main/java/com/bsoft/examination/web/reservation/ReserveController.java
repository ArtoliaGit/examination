package com.bsoft.examination.web.reservation;

import com.bsoft.examination.domain.reservation.Reserve;
import com.bsoft.examination.service.reservation.ReserveService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 预约信息controller
 * @author artolia
 */
@RestController
@RequestMapping("reserve")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    /**
     * 保存或更新预约信息
     * @param reserve 预约信息实体类
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody Reserve reserve) {
        return reserveService.saveOrUpdate(reserve).toJson();
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
}
