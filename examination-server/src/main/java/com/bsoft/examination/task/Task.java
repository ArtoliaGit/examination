package com.bsoft.examination.task;

import com.bsoft.examination.service.resource.ReserveTemplateService;
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

    public Task(ReserveTemplateService reserveTemplateService) {
        this.reserveTemplateService = reserveTemplateService;
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
}
