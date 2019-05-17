package com.bsoft.examination.domain.datasource;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("datasource")
public class DataSourceEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String datasourceName;
    private String datasourceDriverclass;
    private String datasourceIp;
    private String datasourcePort;
    private String datasourceUser;
    private String datasourcePassword;
    private String organcode;
    private String type;
    private String createUser;
    private String createTime;
}
