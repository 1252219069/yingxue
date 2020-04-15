package com.baizhi.lq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    private String id;

    private String username;

    private String phone;
    @Column(name = "head_img")
    private String headImg;

    private String sign;

    private String wechat;

    private String status;
    @Column(name = "create_date")
    private Date createDate;


}