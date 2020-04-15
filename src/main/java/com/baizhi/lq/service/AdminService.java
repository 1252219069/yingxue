package com.baizhi.lq.service;

import com.baizhi.lq.entity.Admin;

import java.util.HashMap;

/**
 * @Author 李瓊
 * @Date 2020/3/26 21:15
 */
public interface AdminService {
    /**
     * 管理员登陆
     *
     * @param admin
     * @param code
     * @return
     */
    HashMap<String, Object> adminLogin(Admin admin, String code);
}
