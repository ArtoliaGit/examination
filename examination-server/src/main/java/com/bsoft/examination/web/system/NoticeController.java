package com.bsoft.examination.web.system;

import com.bsoft.examination.domain.system.Notice;
import com.bsoft.examination.service.system.NoticeService;
import com.bsoft.examination.util.RequestParamPaser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 提示信息维护
 * @author artolia
 */
@RestController
@RequestMapping("notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * 保存或更新提示信息
     * @param notice 提示信息实体类
     * @return String
     */
    @PostMapping("/save")
    public String save(@RequestBody Notice notice) {
        return noticeService.saveOrUpdate(notice).toJson();
    }

    /**
     * 获取提示信息列表
     * @param request 请求
     * @return String
     */
    @GetMapping("/getList")
    public String getList(HttpServletRequest request) {
        return noticeService.getList(RequestParamPaser.getParameters(request)).toJson();
    }

    /**
     * 删除提示信息
     * @param id 提示信息id
     * @return String
     */
    @GetMapping("/delete")
    public String delete(String id) {
        return noticeService.removeById(id).toJson();
    }
}
