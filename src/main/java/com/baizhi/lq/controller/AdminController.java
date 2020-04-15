package com.baizhi.lq.controller;

import com.baizhi.lq.entity.Admin;
import com.baizhi.lq.service.AdminService;
import com.baizhi.util.ImageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @Author 李瓊
 * @Date 2020/3/26 22:05
 */
@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    /**
     * 管理员登陆
     *
     * @param admin
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping("login")
    public HashMap<String, Object> adminLogin(Admin admin, String code) {//管理员登录
        //返回map集合
        HashMap<String, Object> map = adminService.adminLogin(admin, code);
        return map;
    }

    /**
     * 验证码
     *
     * @param session
     * @param outputStream
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("img")
    public void code(HttpSession session, OutputStream outputStream) throws IOException {
        //获得随机字符
        String securityCode = ImageCodeUtil.getSecurityCode();
        //打印随机字符
        System.out.println("验证码=" + securityCode);
        //将验证码存入session
        session.setAttribute("code", securityCode);
        //生成图片
        BufferedImage image = ImageCodeUtil.createImage(securityCode);
        //响应图片   参数 :  图片  格式   响应流
        ImageIO.write(image, "png", outputStream);
        //释放资源
        outputStream.close();
    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping("loginOut")
    public String loginOut(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/login/login.jsp";
    }


}
