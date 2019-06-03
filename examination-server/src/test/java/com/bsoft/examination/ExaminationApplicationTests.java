package com.bsoft.examination;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.bsoft.examination.domain.system.Notice;
import com.bsoft.examination.domain.system.Organ;
import com.bsoft.examination.mapper.system.NoticeMapper;
import com.bsoft.examination.mapper.auth.UserMapper;
import com.bsoft.examination.service.system.OrganService;
import com.bsoft.examination.service.test.TestService;
import com.bsoft.examination.util.WordUtil;
import com.bsoft.examination.util.excel.ExcelStyleHandler;
import com.bsoft.examination.util.excel.ExcelUtil;
import com.bsoft.examination.util.excel.ExcelWriterFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExaminationApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void contextLoads() {

        List<DemoInfo> list = getList();
        String fileName = "一个 Excel 文件";
        String sheetName = "第一个 sheet";

        ExcelWriterFactory writer = ExcelUtil.writeExcel(fileName, ExcelTypeEnum.XLS, new ExcelStyleHandler());
        writer.write(list, "测试", new DemoInfo());
        writer.write(list, "测试2", new DemoInfo());
        writer.finish();
    }

    @Test
    public void whenUploadSuccess() throws Exception {
        String result = mockMvc.perform(multipart("/file")
                .file(new MockMultipartFile("file", "test.txt", "multipart/form-data", "hello upload".getBytes("UTF-8"))))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void testCreateWord() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("username", "张三");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        dataMap.put("currDate", sdf.format(new Date()));

        dataMap.put("content", "这是正文");

        try {
            FileInputStream fis = new FileInputStream(new File("D:\\下载\\33.png"));
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            dataMap.put("image", Base64.getEncoder().encodeToString(data));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> newList = new ArrayList<>();
        for (int i = 1; i <= 10; i ++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "标题" + i);
            map.put("content", "内容" + i);
            map.put("author", "作者" + i);
            newList.add(map);
        }
        dataMap.put("newList", newList);

        Random r = new Random();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        StringBuffer sb = new StringBuffer();
        sb.append(sdf1.format(new Date()));
        sb.append("_");
        sb.append(r.nextInt(100));

        String filePath = "./upload";

        String fileOnlyName = "导出word" + sb + ".doc";
//		String fileName = "导出word.doc";

        WordUtil.createWord(dataMap, "freemarker模板.ftl", fileOnlyName);
    }

    @Autowired
    private TestService testService;

    @Autowired
    private OrganService organService;

    @Test
    public void testDs() {
//        testService.test();
//        testService.getUser();
        Organ organ = new Organ();
        organ.setName("bs_tjxt");
        organ.setDriverclass("1");
        organ.setIp("192.168.1.201");
        organ.setPort("1433");
        organ.setUsername("sa");
        organ.setPassword("bsoft");
        organ.setType("1");
        organ.setOrgancode("azsqwsfwzx");
        organService.addDataSource(organ);
        organService.removeDataSource(organ);
    }

    public List<DemoInfo> getList() {
        List<DemoInfo> list = new ArrayList<>();
        DemoInfo model1 = new DemoInfo();
        model1.setName("howie");
        model1.setAge("19");
        model1.setAddress("123456789");
        model1.setEmail("123456789@gmail.com");
        list.add(model1);
        DemoInfo model2 = new DemoInfo();
        model2.setName("harry");
        model2.setAge("20");
        model2.setAddress("198752233");
        model2.setEmail("198752233@gmail.com");
        list.add(model2);
        return list;
    }

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testNotice() {
        Notice notice = new Notice();
        notice.setCheckItem("123");
        noticeMapper.insert(notice);

        userMapper.deleteById("ccb95bf9806611e9971c844bf5225f31");
    }


}
