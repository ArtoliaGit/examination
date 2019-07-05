package com.bsoft.examination.service.system;

import com.baomidou.dynamic.datasource.DynamicDataSourceCreator;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.configuration.database.CustomDynamicDataSourceProvider;
import com.bsoft.examination.domain.system.Organ;
import com.bsoft.examination.mapper.system.OrganMapper;
import com.bsoft.examination.service.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据源service
 */
@Slf4j
@Service
public class OrganService extends BaseService<Organ, OrganMapper> {

    private final DataSource dataSource;

    /**
     * 多数据源创建器
     */
    private final DynamicDataSourceCreator dynamicDataSourceCreator;

    private final CustomDynamicDataSourceProvider customDynamicDataSourceProvider;

    private final OrganMapper organMapper;

    public OrganService(@Qualifier("dataSource") DataSource dataSource, DynamicDataSourceCreator dynamicDataSourceCreator, CustomDynamicDataSourceProvider customDynamicDataSourceProvider, OrganMapper organMapper) {
        this.dataSource = dataSource;
        this.dynamicDataSourceCreator = dynamicDataSourceCreator;
        this.customDynamicDataSourceProvider = customDynamicDataSourceProvider;
        this.organMapper = organMapper;
    }

    /**
     * 保存和更新机构
     * @param organ 机构实体
     * @return Result
     */
    public Result save(Organ organ, String op) {
        Result<Organ> result = new Result<>();

        try {
            organ.setCreateTime(new Date());
            if ("update".equals(op)) {
                removeDataSource(organ);
                boolean flag = addDataSource(organ);
                if (flag) {
                    organMapper.updateById(organ);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("数据源更新成功");
                    result.setData(organ);
                } else {
                    result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    result.setMessage("数据源更新失败");
                }

            } else {

                boolean flag = addDataSource(organ);
                if (flag) {
                    organMapper.insert(organ);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("数据源添加成功");
                    result.setData(organ);
                } else {
                    result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    result.setMessage("数据源添加失败");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 查询机构列表
     * @param params 查询参数
     * @return Result
     */
    public Result getOrganList(Map<String, Object> params) {
        Result<List<Organ>> result = new Result<>();
        try {
            String pageNum = (String) params.get("page");
            String pageSize = (String) params.get("pageSize");
            Page<Organ> page = new Page<>(1, 10);

            if (StringUtils.isNoneBlank(pageNum, pageNum)) {
                page.setCurrent(Long.parseLong(pageNum));
                page.setSize(Long.parseLong(pageSize));
            }

            IPage<Organ> userIPage = organMapper.selectPage(page, Wrappers.emptyWrapper());
            long total = userIPage.getTotal();
            List<Organ> list = userIPage.getRecords();
            result.setCode(HttpStatus.OK);
            result.setData(list);
            result.setTotal(total);
            result.setMessage("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("查询失败");
        }
        return result;
    }

    /**
     * 删除
     * @param organ 机构实体
     * @return Result
     */
    public Result deleteOrgan(Organ organ) {
        Result result = new Result();
        try {
            boolean flag = removeDataSource(organ);
            if (flag) {
                organMapper.deleteById(organ.getId());
                result.setCode(HttpStatus.OK);
                result.setMessage("删除数据源成功");
            } else {
                result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage("删除数据源失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("删除数据源失败");
        }
        return result;
    }

    /**
     * 添加数据源
     * @param organ 数据源实体
     * @return boolean
     */
    private boolean addDataSource(Organ organ) {
        boolean flag = false;

        String name = organ.getName();
        String driverType = organ.getDriverclass();
        String ip = organ.getIp();
        String port = organ.getPort();
        String user = organ.getUsername();
        String password = organ.getPassword();
        String organcode = organ.getOrgancode();

        if (StringUtils.isAnyBlank(name, driverType, ip, port, user, password, organcode)) {
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

            dataSourceProperty.setPollName(organcode);

            DataSource newDataSource = dynamicDataSourceCreator.createDataSource(dataSourceProperty);

            dynamicRoutingDataSource.addDataSource(dataSourceProperty.getPollName(), newDataSource);

            log.info("=======================添加数据源成功============================");
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 删除数据源
     * @param organ 数据源实体
     * @return boolean
     */
    private boolean removeDataSource(Organ organ) {
        boolean flag;
        String type = organ.getType();
        String ds;
        if ("1".equals(type)) {
            ds = organ.getOrgancode();
        } else {
            ds = organ.getName();
        }
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

    @Override
    public OrganMapper getBaseMapper() {
        return organMapper;
    }
}
