package com.bsoft.examination.service.test;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.bsoft.examination.mapper.test.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TestService {

    @Resource
    private TestMapper testMapper;

    @DS("#his")
    public void test() {
        List<String> list = testMapper.test();
//        System.out.println(list);
        list.stream().forEach(System.out::println);
    }

    public void getUser() {
        List<Map> list = testMapper.getUser();
        System.out.println(list);
    }
}
