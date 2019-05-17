package com.bsoft.examination.service.datasource;

import com.baomidou.dynamic.datasource.DynamicDataSourceCreator;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.bsoft.examination.configuration.database.CustomDynamicDataSourceProvider;
import com.bsoft.examination.domain.datasource.DataSourceEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Service
public class DataSourceService {

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    /**
     * 多数据源参数
     */
    @Autowired
    private DynamicDataSourceProperties properties;

    /**
     * 多数据源创建器
     */
    @Autowired
    private DynamicDataSourceCreator dynamicDataSourceCreator;

    @Autowired
    private CustomDynamicDataSourceProvider customDynamicDataSourceProvider;

    public boolean addDataSource(DataSourceEntity dataSourceEntity) {
        boolean flag = false;

        String name = dataSourceEntity.getDatasourceName();
        String driverType = dataSourceEntity.getDatasourceDriverclass();
        String ip = dataSourceEntity.getDatasourceIp();
        String port = dataSourceEntity.getDatasourcePort();
        String user = dataSourceEntity.getDatasourceUser();
        String password = dataSourceEntity.getDatasourcePassword();
        String type = dataSourceEntity.getType();
        String organcode = dataSourceEntity.getOrgancode();

        if (StringUtils.isAnyBlank(name, driverType, ip, port, user, password, type, organcode)) {
            return false;
        }

        try {
            DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
            log.info("=========================增加数据源==========================");

            DataSourceProperty dataSourceProperty = new DataSourceProperty();

            Map<String, String> map = customDynamicDataSourceProvider.getUrl(driverType, ip, port, name);
            String url = map.get("url");
            String driverClass = map.get("driverClass");

            dataSourceProperty.setDriverClassName(driverClass);
            dataSourceProperty.setUrl(url);
            dataSourceProperty.setUsername(user);
            dataSourceProperty.setPassword(password);

            if ("1".equals(type)) {
                dataSourceProperty.setPollName(organcode);
            } else {
                dataSourceProperty.setPollName(name);
            }

            DataSource newDataSource = dynamicDataSourceCreator.createDataSource(dataSourceProperty);

            dynamicRoutingDataSource.addDataSource(dataSourceProperty.getPollName(), newDataSource);

            log.info("=======================添加数据源成功============================");
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public boolean removeDataSource(String ds) {
        boolean flag = false;
        try {
            log.info("=======================删除数据源======================");
            DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
            dynamicRoutingDataSource.removeDataSource(ds);
            flag = true;
            log.info("=======================删除数据源成功=======================");
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
