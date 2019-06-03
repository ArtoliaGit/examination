package com.bsoft.examination.service.base;

import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bsoft.examination.common.HttpStatus;
import com.bsoft.examination.common.Result;
import com.bsoft.examination.util.excel.ExcelUtil;
import com.bsoft.examination.util.excel.ExcelWriterFactory;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 基础服务类
 * @author Artolia Pendragon
 * @version 1.0.0
 */
public abstract class BaseService<T, M extends BaseMapper<T>> {

    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    /**
     * 批量操作 SqlSession
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }

    /**
     * 释放sqlSession
     *
     * @param sqlSession session
     */
    protected void closeSqlSession(SqlSession sqlSession) {
        SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(currentModelClass()));
    }

    /**
     * 获取 SqlStatement
     *
     * @param sqlMethod ignore
     * @return ignore
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

    /**
     * 保存或更新数据
     * @param entity 实体
     * @return Result
     */
    public Result saveOrUpdate(T entity) {
        Result<T> result = new Result<>();
        try {
            BaseMapper baseMapper = getBaseMapper();
            if (null != entity) {
                Class<?> cls = entity.getClass();
                TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
                Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
                String keyProperty = tableInfo.getKeyProperty();
                Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal) || Objects.isNull(baseMapper.selectById((Serializable) idVal))) {
                    baseMapper.insert(entity);
                } else {
                    baseMapper.updateById(entity);
                }
                result.setCode(HttpStatus.OK);
                result.setMessage("保存成功");
            } else {
                result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage("数据不能为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 批量插入或更新
     * @param entityList 实体列表
     * @param batchSize 一次插入数量
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        Class<?> cls = currentModelClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T entity : entityList) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, keyProperty);
                if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
                    batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), entity);
                } else {
                    MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                    param.put(Constants.ENTITY, entity);
                    batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
                }

                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    /**
     * 根据 ID 删除
     * @param id @TableId注解的主键
     * @return Result
     */
    public Result removeById(Serializable id) {
        Result result = new Result();
        try {
            BaseMapper baseMapper = getBaseMapper();
            if (SqlHelper.retBool(baseMapper.deleteById(id))) {
                result.setCode(HttpStatus.OK);
                result.setMessage("删除成功");
            } else {
                result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 根据 columnMap 条件，删除记录
     * @param columnMap 表字段 map 对象
     * @return Result
     */
    public Result removeByMap(Map<String, Object> columnMap) {
        Result result = new Result();
        try {
            Assert.notEmpty(columnMap, "error: columnMap must not be empty");
            BaseMapper baseMapper = getBaseMapper();
            if (SqlHelper.retBool(baseMapper.deleteByMap(columnMap))) {
                result.setCode(HttpStatus.OK);
                result.setMessage("删除成功");
            } else {
                result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 根据 entity 条件，删除记录
     * @param wrapper 条件构造器
     * @return Result
     */
    public Result remove(Wrapper<T> wrapper) {
        Result result = new Result();
        try {
            BaseMapper baseMapper = getBaseMapper();
            if (SqlHelper.retBool(baseMapper.delete(wrapper))) {
                result.setCode(HttpStatus.OK);
                result.setMessage("删除成功");
            } else {
                result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 根据 ID 查询
     * @param id @TableId注解的主键
     * @return Result
     */
    public Result getById(Serializable id) {
        Result<T> result = new Result<>();
        try {
            BaseMapper baseMapper = getBaseMapper();
            T t = (T) baseMapper.selectById(id);
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
            result.setData(t);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 查询列表
     * @param queryWrapper 条件构造器
     * @return Result
     */
    public Result list(Wrapper<T> queryWrapper) {
        Result<List<T>> result = new Result<>();
        try {
            BaseMapper baseMapper = getBaseMapper();
            List<T> list = baseMapper.selectList(queryWrapper);
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 查询所有
     * @return Result
     */
    public Result list() {
        return list(Wrappers.emptyWrapper());
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     * @param queryWrapper 条件构造器
     * @return Result
     */
    public Result count(Wrapper<T> queryWrapper) {
        Result<Integer> result = new Result<>();
        try {
            BaseMapper baseMapper = getBaseMapper();
            int total = SqlHelper.retCount(baseMapper.selectCount(queryWrapper));
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
            result.setData(total);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 查询总记录数
     * @return Result
     */
    public Result count() {
        return count(Wrappers.emptyWrapper());
    }

    /**
     * 翻页查询
     * @param page 分页对象
     * @param queryWrapper 条件构造器
     * @return Result
     */
    public Result page(IPage<T> page, Wrapper<T> queryWrapper) {
        Result<List<T>> result = new Result<>();
        try {
            BaseMapper baseMapper = getBaseMapper();
            IPage<T> iPage = baseMapper.selectPage(page, queryWrapper);
            List<T> list = iPage.getRecords();
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
            result.setData(list);
            result.setTotal(iPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 无条件翻页查询
     * @param page 分页对象
     * @return Result
     */
    public Result page(IPage<T> page) {
        Result<List<T>> result = new Result<>();
        try {
            BaseMapper baseMapper = getBaseMapper();
            IPage<T> iPage = baseMapper.selectPage(page, Wrappers.emptyWrapper());
            List<T> list = iPage.getRecords();
            result.setCode(HttpStatus.OK);
            result.setMessage("查询成功");
            result.setData(list);
            result.setTotal(iPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("服务错误");
        }
        return result;
    }

    /**
     * 导出excel
     * @param response 响应
     * @param fileName 文件名
     * @param queryWrapper 条件构造器
     * @param <T> BaseRowModel
     */
    public <T extends BaseRowModel> Result exportExcel(HttpServletResponse response, String fileName, Wrapper<T> queryWrapper) {
        Result result = new Result();
        try {
            BaseMapper baseMapper = getBaseMapper();
            List<T> list = baseMapper.selectList(queryWrapper);
            if (list.size() == 0) {
                result.setCode(HttpStatus.NO_CONTENT);
                result.setMessage("没有数据");
                return result;
            }
            ExcelWriterFactory writer = ExcelUtil.writeExcel(response, fileName, ExcelTypeEnum.XLS);
            writer.write(list, "Sheet1", getEntity());
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("导出失败");
        }
        return result;
    }

    /**
     * 导出所有数据
     * @param response 响应
     * @param fileName 文件名
     * @param <T> BaseRowModel
     * @return Result
     */
    public <T extends BaseRowModel> Result exportExcel(HttpServletResponse response, String fileName) {
        return exportExcel(response, fileName, Wrappers.emptyWrapper());
    }

    /**
     * 获取实体类
     * @param <T> BaseRowModel
     * @return Result
     */
    public <T extends BaseRowModel> T getEntity() {
        return (T) new BaseRowModel();
    }

    /**
     * 获取对应 entity 的 BaseMapper
     * @return BaseMapper
     */
    public abstract M getBaseMapper();
}
