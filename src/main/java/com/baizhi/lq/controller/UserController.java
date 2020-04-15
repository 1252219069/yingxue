package com.baizhi.lq.controller;

import com.baizhi.lq.entity.User;
import com.baizhi.lq.service.UserService;
import com.baizhi.lq.util.AliyunSendPhoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 李瓊
 * @Date 2020/3/27 13:49
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;


    /**
     * 分页展示
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("queryAllUser")
    public Map<String, Object> queryAllUser(Integer page, Integer rows) {
        HashMap<String, Object> map = userService.queryAllUser(page, rows);
        return map;
    }

    /**
     * 用户添加
     *
     * @param user
     * @param oper
     * @param id
     * @return
     */
    @RequestMapping("insert")
    public String insert(User user, String oper, String[] id) {
        String uid = null;
        if (oper.equals("add")) {
            uid = userService.insert(user);
        }
        if (oper.equals("edit")) {
            uid = user.getId();
            userService.updateUser(user);
        }

        if (oper.equals("del")) {
            userService.deleteUser(id);
        }
        return uid;
    }

    /**
     * 文件上传
     *
     * @param headImg
     * @param id
     * @param request
     */
    @RequestMapping("upload")
    public void upload(MultipartFile headImg, String id, HttpServletRequest request) {
        //上传到阿里云
        userService.uploadUserAliyuns(headImg, id, request);
    }

    /**
     * 修改状态
     */
    @RequestMapping("updateStatus")
    public void updateStatus(String id) {
        userService.updateStatus(id);
    }

    /**
     * 手机验证码
     */
    @RequestMapping("phone")
    public void phone(String phone) {
        System.out.println("手机号 = " + phone);
        String random = AliyunSendPhoneUtil.getRandom(6);
        System.out.println("验证码 = " + random);
        String message = AliyunSendPhoneUtil.sendCode(phone, random);
        System.out.println(message);
    }
}
