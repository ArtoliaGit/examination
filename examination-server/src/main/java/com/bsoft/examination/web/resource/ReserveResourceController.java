package com.bsoft.examination.web.resource;

import com.bsoft.examination.domain.resource.ReserveResource;
import com.bsoft.examination.service.resource.ReserveResourceService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 预约资源controller
 * @author artolia
 */
@RestController
@RequestMapping("reserveResource")
public class ReserveResourceController {

    private final ReserveResourceService reserveResourceService;

    public ReserveResourceController(ReserveResourceService reserveResourceService) {
        this.reserveResourceService = reserveResourceService;
    }

    /**
     * 保存或更新预约资源
     * @param reserveResource 预约资源实体类
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody ReserveResource reserveResource) {
        return reserveResourceService.saveOrUpdate(reserveResource).toJson();
    }

    /**
     * 获取预约资源列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getList")
    public String getList(HttpServletRequest request) {
        return reserveResourceService.getList(RequestParamPaser.getParameters(request)).toJson();
    }

    /**
     * 删除预约资源
     * @param id 预约资源id
     * @return String
     */
    @GetMapping("/delete")
    public String delete(String id) {
        return reserveResourceService.removeById(id).toJson();
    }

    /**
     * 获取预约资源列表
     * @param checkItem 检查项目
     * @return String
     */
    @GetMapping("/getTableList")
    public String getTableList(String checkItem) {
        return reserveResourceService.getTableList(checkItem).toJson();
    }

    /**
     * 批量保存
     * @param resources 预约资源列表
     * @return String
     */
    @PostMapping("/batchSave")
    public String batchSave(@RequestBody List<ReserveResource> resources) {
        return reserveResourceService.batchSave(resources).toJson();
    }
}
