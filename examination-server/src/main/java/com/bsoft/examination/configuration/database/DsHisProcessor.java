package com.bsoft.examination.configuration.database;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.bsoft.examination.common.auth.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * 数据源适配
 * @author artolia
 */
@Slf4j
@Component
public class DsHisProcessor extends DsProcessor {

    private final UserInfo userInfo;

    public DsHisProcessor(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

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
        String dbName = userInfo.getOrgan();
        log.info("=========================数据库[{}]========================", dbName);
        return dbName;
    }
}
