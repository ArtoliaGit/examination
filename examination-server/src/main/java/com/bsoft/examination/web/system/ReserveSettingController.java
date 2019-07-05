package com.bsoft.examination.web.system;

import com.bsoft.examination.domain.system.ReserveSetting;
import com.bsoft.examination.service.system.ReserveSettingService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 预约设置
 * @author artolia
 */
@RestController
@RequestMapping("reserveSetting")
public class ReserveSettingController {

    private final ReserveSettingService reserveSettingService;

    public ReserveSettingController(ReserveSettingService reserveSettingService) {
        this.reserveSettingService = reserveSettingService;
    }

    /**
     * 保存或更新预约设置
     * @param reserveSetting 预约设置实体类
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody ReserveSetting reserveSetting) {
        reserveSetting.setCreateTime(new Date());
        return reserveSettingService.saveOrUpdate(reserveSetting).toJson();
    }

    /**
     * 获取预约设置
     * @return String
     */
    @GetMapping("/getData")
    public String getData() {
        return reserveSettingService.list().toJson();
    }

}
