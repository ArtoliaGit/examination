package com.bsoft.examination.configuration.database;

import com.baomidou.dynamic.datasource.DynamicDataSourceCreator;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import javax.validation.constraints.NotBlank;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源
 * @author artolia
 */
@Slf4j
@Configuration
public class CustomDynamicDataSourceProvider extends YmlDynamicDataSourceProvider {

    @Value("${spring.datasource.dynamic.primary}")
    private String master;

    /**
     * 多数据源参数
     */
    private DynamicDataSourceProperties properties;

    /**
     * 多数据源创建器
     */
    private DynamicDataSourceCreator dynamicDataSourceCreator;

    public CustomDynamicDataSourceProvider(DynamicDataSourceProperties properties, DynamicDataSourceCreator dynamicDataSourceCreator) {
        super(properties, dynamicDataSourceCreator);
        this.properties = properties;
        this.dynamicDataSourceCreator = dynamicDataSourceCreator;
    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        Map<String, DataSource> dataSourceMap = super.loadDataSources();
        Map<String, DataSourceProperty> dataSourcePropertiesMap = properties.getDatasource();

        DataSourceProperty masterDataSource = dataSourcePropertiesMap.get(StringUtils.defaultIfBlank(master, "master"));
        String driverClassName = masterDataSource.getDriverClassName();
        String url = masterDataSource.getUrl();
        String user = masterDataSource.getUsername();
        String password = masterDataSource.getPassword();

        Connection connection = null;
        Statement stmt = null;

        try {
            Class.forName(driverClassName);
            log.info("成功加载数据库驱动程序");
            connection = DriverManager.getConnection(url, user, password);
            log.info("成功获取数据库连接");
            stmt = connection.createStatement();

            Map<String, DataSourceProperty> propertiesMap = executeStmt(stmt);
            if (propertiesMap != null) {
                for (Map.Entry<String, DataSourceProperty> item : propertiesMap.entrySet()) {
                    String pollName = item.getKey();
                    DataSourceProperty dataSourceProperty = item.getValue();
                    dataSourceProperty.setPollName(pollName);
                    dataSourceMap.put(pollName, dynamicDataSourceCreator.createDataSource(dataSourceProperty));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(connection);
            JdbcUtils.closeStatement(stmt);
        }
        return dataSourceMap;
    }

    /**
     * 执行语句获得数据源参数
     *
     * @param statement 语句
     * @return 数据源参数
     * @throws SQLException sql异常
     */
    private Map<String, DataSourceProperty> executeStmt(@NotBlank Statement statement) throws SQLException {
        String sql = "select name, driverclass, ip, port," +
                " username, password, organcode, type from organ";
        ResultSet result = statement.executeQuery(sql);

        Map<String, DataSourceProperty> dataSourcePropertyMap = new HashMap<>();
        while (result.next()) {
            String name = result.getString(1);
            String driverType = result.getString(2);
            String ip = result.getString(3);
            String port = result.getString(4);
            String user = result.getString(5);
            String password = result.getString(6);
            String organcode = result.getString(7);

            Map<String, String> map = getUrl(driverType, ip, port, name);
            String url = map.get("url");
            String driverClass = map.get("driverClass");

            if ("".equals(driverClass)) {
                continue;
            }

            DataSourceProperty property = new DataSourceProperty();
            property.setDriverClassName(driverClass);
            property.setUrl(url);
            property.setUsername(user);
            property.setPassword(password);

            dataSourcePropertyMap.put(organcode, property);
        }
        return dataSourcePropertyMap;
    }

    public Map<String, String> getUrl(String driverType, String ip, String port, String name) {
        String driverClass;
        String url;

        switch (driverType) {
            case "2": //sql server
                driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                url = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName=" + name;
                break;
            case "3": //oracle
                driverClass = "oracle.jdbc.OracleDriver";
                url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + name;
                break;
            default: //mysql
                driverClass = "com.mysql.cj.jdbc.Driver";
                url = "jdbc:mysql://" + ip + ":" + port +"/" + name + "?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true";
        }

        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("driverClass", driverClass);
        return map;
    }
}
