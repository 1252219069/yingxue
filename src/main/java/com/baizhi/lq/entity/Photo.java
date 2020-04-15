package com.baizhi.lq.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Excel(name = "ID", width = 20, height = 20)
    private String id;
    @Excel(name = "头像", type = 2)
    private String cover;
    @Excel(name = "年龄")
    private Integer age;
    @Excel(name = "生日", format = "yyyy-MM-dd")
    private Date bir;

}
