package com.bsoft.examination.web.resource;

import com.bsoft.examination.domain.resource.ReserveTemplate;
import com.bsoft.examination.service.resource.ReserveTemplateService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 资源模板controller
 * @author artolia
 */
@RestController
@RequestMapping("reserveTemplate")
public class ReserveTemplateController {

    private final ReserveTemplateService reserveTemplateService;

    public ReserveTemplateController(ReserveTemplateService reserveTemplateService) {
        this.reserveTemplateService = reserveTemplateService;
    }

    /**
     * 保存或更新资源模板
     * @param reserveTemplate 资源模板实体类
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody ReserveTemplate reserveTemplate) {
        return reserveTemplateService.saveOrUpdate(reserveTemplate).toJson();
    }

    /**
     * 获取资源模板列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getList")
    public String getList(HttpServletRequest request) {
        return reserveTemplateService.getList(RequestParamPaser.getParameters(request)).toJson();
    }

    /**
     * 删除资源模板
     * @param id 资源模板id
     * @return String
     */
    @GetMapping("/delete")
    public String delete(String id) {
        return reserveTemplateService.removeById(id).toJson();
    }

    /**
     * 初始化数据
     * @param checkItem 检查项目
     * @return String
     */
    @GetMapping("/initReserveResource")
    public String initReserveResource(String checkItem) {
        return reserveTemplateService.initReserveResource(checkItem).toJson();
    }

    /**
     * 获取模板列表
     * @param checkItem 检查项目
     * @return String
     */
    @GetMapping("/getTableList")
    public String getTableList(String checkItem) {
        return reserveTemplateService.getTableList(checkItem).toJson();
    }

    /**
     * 批量保存
     */
    @PostMapping("/batchSave")
    public String batchSave(@RequestBody List<ReserveTemplate> maps) {
        return reserveTemplateService.batchSave(maps).toJson();
    }
}
