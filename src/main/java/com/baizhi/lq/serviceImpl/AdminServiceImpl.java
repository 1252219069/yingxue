package com.baizhi.lq.serviceImpl;

import com.baizhi.lq.dao.AdminMapper;
import com.baizhi.lq.entity.Admin;
import com.baizhi.lq.entity.AdminExample;
import com.baizhi.lq.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 李瓊
 * @Date 2020/3/26 21:25
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    HttpSession httpSession;

    /**
     * 管理员登陆
     *
     * @param admin
     * @param code
     * @return
     */
    @Override
    public HashMap<String, Object> adminLogin(Admin admin, String code) {
        HashMap<String, Object> map = new HashMap<>();
        AdminExample example = new AdminExample();
        example.createCriteria().andUsernameEqualTo(admin.getUsername());
        List<Admin> admins = adminMapper.selectByExample(example);
        //获取session 中的验证码
        String codes = (String) httpSession.getAttribute("code");
        //判断验证码是否正确
        if (codes.equals(code)) {
            //判断用户是否存在
            if (admins.toString() != "[]") {
                for (Admin admin1 : admins) {
                    System.out.println(admin1);
                    //判断密码是否正确
                    if (admin1.getPassword().equals(admin.getPassword())) {
                        httpSession.setAttribute("admin", admin1);
                        map.put("status", "200");
                        map.put("message", "登陆成功");
                    } else {
                        map.put("status", "400");
                        map.put("message", "用户名或密码错误,请重新输入!");
                    }
                }
            } else {
                System.out.println("进来了");
                map.put("status", "400");
                map.put("message", "用户名不存在,请重新输入!");
            }
        } else {
            map.put("status", "400");
            map.put("message", "验证码错误,请重新输入!");
        }
        return map;
    }
}
