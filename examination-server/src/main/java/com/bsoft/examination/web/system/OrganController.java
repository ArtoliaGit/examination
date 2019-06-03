package com.bsoft.examination.web.system;

import com.bsoft.examination.domain.system.Organ;
import com.bsoft.examination.service.system.OrganService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 机构controller
 */
@RestController
@RequestMapping("organ")
public class OrganController {

    @Autowired
    private OrganService organService;

    /**
     * 获取机构列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getOrganList")
    public String getOrganList(HttpServletRequest request) {
        Map<String, Object> parameters = RequestParamPaser.getParameters(request);
        return organService.getOrganList(parameters).toJson();
    }

    /**
     * 保存和更新机构
     * @param organ 机构实体
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody Organ organ, HttpServletRequest request) {
        String op = request.getParameter("op");
        return organService.save(organ, op).toJson();
    }

    /**
     * 删除机构
     * @param organ 机构实体
     * @return String
     */
    @PostMapping("/deleteOrgan")
    public String deleteOrgan(@RequestBody Organ organ) {
        return organService.deleteOrgan(organ).toJson();
    }

    /**
     * 获取所有机构
     */
    @GetMapping("/getAllList")
    public String getAllList() {
        return organService.list().toJson();
    }
}
