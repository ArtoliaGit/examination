package com.bsoft.examination.configuration.database;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DsHisProcessor extends DsProcessor {

    @Override
    public boolean matches(String key) {
        log.info("=========================进入HIS数据库匹配====================");
        boolean flag = false;
        if ("#his".equals(key)) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        log.info("=========================进入自定义处理器====================");

        return "azsqwsfwzx";
    }
}
