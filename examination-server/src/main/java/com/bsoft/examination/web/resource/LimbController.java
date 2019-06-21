package com.bsoft.examination.web.resource;

import com.bsoft.examination.domain.resource.Limb;
import com.bsoft.examination.service.resource.LimbService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 部位信息controller
 * @author artolia
 */
@RestController
@RequestMapping("/limb")
public class LimbController {

    private final LimbService limbService;

    public LimbController(LimbService limbService) {
        this.limbService = limbService;
    }

    /**
     * 保存或更新检查部位
     * @param limb 检查部位实体类
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody Limb limb) {
        return limbService.saveOrUpdate(limb).toJson();
    }

    /**
     * 获取检查部位列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getList")
    public String getList(HttpServletRequest request) {
        return limbService.getList(RequestParamPaser.getParameters(request)).toJson();
    }

    /**
     * 删除检查部位
     * @param id 检查部位id
     * @return String
     */
    @GetMapping("/delete")
    public String delete(String id) {
        return limbService.removeById(id).toJson();
    }
}
