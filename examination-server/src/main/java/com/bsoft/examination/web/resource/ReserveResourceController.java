package com.bsoft.examination.web.resource;

import com.bsoft.examination.domain.resource.ReserveResource;
import com.bsoft.examination.service.resource.ReserveResourceService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 预约资源controller
 * @author artolia
 */
@RestController
@RequestMapping("reserveResource")
public class ReserveResourceController {

    @Autowired
    private ReserveResourceService reserveResourceService;

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
}
