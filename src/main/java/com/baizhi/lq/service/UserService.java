package com.baizhi.lq.service;

import com.baizhi.lq.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @Author 李瓊
 * @Date 2020/3/27 13:39
 */
public interface UserService {
    /**
     * 分页查询
     *
     * @param page
     * @param rows
     * @return
     */
    HashMap<String, Object> queryAllUser(Integer page, Integer rows);

    /**
     * 文件上传
     *
     * @param headImg
     * @param id
     * @param session
     */
    void upload(MultipartFile headImg, String id, HttpSession session);

    /**
     * 用户添加
     *
     * @param user
     * @return
     */
    String insert(User user);

    /**
     * 状态修改
     *
     * @param id
     */
    void updateStatus(String id);

    /**
     * 修改用户信息
     *
     * @param user
     */
    void updateUser(User user);

    /**
     * 删除用户
     *
     * @param user
     */
    void delete(User user);

    /**
     * 批量删除用户
     *
     * @param id
     */
    void deleteUser(String[] id);

    /**
     * 阿里云文件上传
     *
     * @param headImg
     * @param id
     * @param request
     */
    void uploadUserAliyuns(MultipartFile headImg, String id, HttpServletRequest request);
}
