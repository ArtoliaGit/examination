package com.bsoft.examination.util.excel;

import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * excel列model
 */
public class CommonRowModel extends BaseRowModel {

    @TableField(exist = false)
    private Map<Integer, CellStyle> cellStyleMap = new HashMap<Integer,CellStyle>();
}
