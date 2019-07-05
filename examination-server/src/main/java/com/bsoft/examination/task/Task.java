package com.bsoft.examination.task;

import com.bsoft.examination.common.Result;
import com.bsoft.examination.service.reservation.CheckInService;
import com.bsoft.examination.service.resource.ReserveTemplateService;
import com.bsoft.examination.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 * @
 */
@Slf4j
@Component
public class Task {

    private final ReserveTemplateService reserveTemplateService;

    private final CheckInService checkInService;

    public Task(ReserveTemplateService reserveTemplateService,
                CheckInService checkInService) {
        this.reserveTemplateService = reserveTemplateService;
        this.checkInService = checkInService;
    }

    /**
     * 预约资源增加一天
     */
    @Scheduled(cron = "0 0 0/1 * * *")
    public void increaseReserveResource() {
        log.info("===================资源复制任务开始执行======================");
        reserveTemplateService.rotateReserveResource();
        log.info("===================资源复制任务结束==========================");
    }

    /**
     * 查询叫号情况，发送消息给前端
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void sendCheckInMessage() {
        Result result = checkInService.getAlwaysCallList();

        boolean flag = WebSocketUtil.sendAll(result);
        if (flag) {
            log.info("===================消息发送成功===================");
        } else {
            log.info("===================消息发送失败===================");
        }
    }
}
