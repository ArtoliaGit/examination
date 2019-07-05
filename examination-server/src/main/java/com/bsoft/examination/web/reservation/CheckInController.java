package com.bsoft.examination.web.reservation;

import com.bsoft.examination.domain.reservation.CheckIn;
import com.bsoft.examination.service.reservation.CheckInService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 预约报到controller
 */
@RestController
@RequestMapping("checkIn")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    /**
     * 获取待呼的人
     * @return String
     */
    @GetMapping("/getList")
    public String getList(HttpServletRequest request) {
        return checkInService.getList(RequestParamPaser.getParameters(request)).toJson();
    }

    /**
     * 更新呼叫状态
     * @param checkIn 呼叫信息
     * @return String
     */
    @PostMapping("/updateStatus")
    public String updateStatus(@RequestBody CheckIn checkIn) {
        return checkInService.saveOrUpdate(checkIn).toJson();
    }

}
