package com.bsoft.examination.web.resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bsoft.examination.domain.resource.CheckItem;
import com.bsoft.examination.service.resource.CheckItemService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 检查项目controller
 * @author artolia
 */
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    private final CheckItemService checkItemService;

    public CheckItemController(CheckItemService checkItemService) {
        this.checkItemService = checkItemService;
    }

    /**
     * 保存或更新检查项目
     * @param checkItem 检查项目实体类
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody CheckItem checkItem) {
        checkItem.setCreateTime(new Date());
        return checkItemService.saveOrUpdate(checkItem).toJson();
    }

    /**
     * 获取检查项目列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getList")
    public String getList(HttpServletRequest request) {
        return checkItemService.getList(RequestParamPaser.getParameters(request)).toJson();
    }

    /**
     * 删除检查项目
     * @param id 检查项目id
     * @return String
     */
    @GetMapping("/delete")
    public String delete(String id) {
        return checkItemService.removeById(id).toJson();
    }

    /**
     * 获取所有检测项目
     */
    @GetMapping("/getAllList")
    public String getAllList(HttpServletRequest request) {
        String way = request.getParameter("way");
        QueryWrapper<CheckItem> wrapper = new QueryWrapper<>();
        if (way != null) {
            wrapper.eq("way", way);
        }
        String status = request.getParameter("status");
        if (status != null) {
            wrapper.eq("status", status);
        }

        return checkItemService.list(wrapper).toJson();
    }

    /**
     * 根据条件获取检查项目
     * @param request 请求
     * @return String
     */
    @GetMapping("/getCheckItemByConditions")
    public String getCheckItemByConditions(HttpServletRequest request) {
        return checkItemService.getCheckItemByConditions(RequestParamPaser.getParameters(request)).toJson();
    }
}
