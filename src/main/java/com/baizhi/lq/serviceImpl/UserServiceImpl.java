package com.baizhi.lq.serviceImpl;

import com.baizhi.lq.annotation.AddCach;
import com.baizhi.lq.annotation.AddLog;
import com.baizhi.lq.annotation.DelCach;
import com.baizhi.lq.dao.UserMapper;
import com.baizhi.lq.entity.User;
import com.baizhi.lq.entity.UserExample;
import com.baizhi.lq.entity.VideoExample;
import com.baizhi.lq.service.UserService;
import com.baizhi.lq.util.AliyunOssUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Author 李瓊
 * @Date 2020/3/27 13:39
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    /**
     * 分页展示用户信息
     *
     * @param page
     * @param rows
     * @return
     */
    @Override
    @AddCach
    public HashMap<String, Object> queryAllUser(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();

        UserExample example = new UserExample();
        //获取总条数
        Integer records = userMapper.selectCountByExample(example);
        map.put("records", records);
        //总页数   total   总条数/每页展示条数  是否有余数
        Integer total = records % rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);
        //当前页
        map.put("page", page);
        //参数  忽略几条  获取几条
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        //分页查询所有数据
        List<User> users = userMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows", users);
        return map;
    }

    /**
     * 文件上传
     *
     * @param headImg
     * @param id
     * @param session
     */
    @Override
    public void upload(MultipartFile headImg, String id, HttpSession session) {
        if ("".equals(headImg.getOriginalFilename())) {
            System.out.println("不做修改图片的操作");
        } else {
            //根据相对路径获取绝对路径
            String realPath = session.getServletContext().getRealPath("/upload/photo");
            File file = new File(realPath);
            //判断文件夹是否存在
            if (!file.exists()) {
                //创建文件夹
                file.mkdirs();
            }
            //获取文件名
            String filename = headImg.getOriginalFilename();
            //给文件名加个时间戳
            String newName = System.currentTimeMillis() + filename;
            try {
                //文件上传
                headImg.transferTo(new File(realPath, newName));
                //设置条件
                UserExample example = new UserExample();
                example.createCriteria().andIdEqualTo(id);
                User user = new User();
                //设置修改结果
                user.setHeadImg(newName);
                //修改名字
                userMapper.updateByExampleSelective(user, example);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void uploadUserAliyuns(MultipartFile headImg, String id, HttpServletRequest request) {
        if ("".equals(headImg.getOriginalFilename())) {
            System.out.println("不做修改图片的操作");
        } else {
            //获取文件名
            String filename = headImg.getOriginalFilename();
            String newName = System.currentTimeMillis() + "-" + filename;

            //截取视频第一帧

            //上传封面
            //文件上传至阿里云
            AliyunOssUtil.uploadFileBytes("yingxue", headImg, "photos/" + newName);
            //修改的条件
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(id);

            User user = new User();
            //设置修改的结果
            user.setHeadImg("https://liqiong.vip/photos/" + newName);
            //修改
            userMapper.updateByExampleSelective(user, example);
        }
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @DelCach
    @AddLog("添加用户")
    @Override
    public String insert(User user) {
        //获取UUID
        String uuid = UUID.randomUUID().toString();
        //拆分UUID
        String uid = uuid.replace("-", "");
        //设置id  状态  创建时间
        user.setId(uid);
        user.setStatus("0");
        user.setCreateDate(new Date());
        userMapper.insert(user);
        return uid;
    }

    /**
     * 修改状态
     *
     * @param id
     */
    @DelCach
    @AddLog("修改状态")
    @Override
    public void updateStatus(String id) {
        //根据id查询
        User user = userMapper.selectByPrimaryKey(id);
        //判断状态
        if ("0".equals(user.getStatus())) {
            user.setStatus("1");
            //修改状态
            userMapper.updateByPrimaryKey(user);
        } else {
            user.setStatus("0");
            userMapper.updateByPrimaryKey(user);
        }
    }

    /**
     * 修改用户信息
     *
     * @param user
     */
    @DelCach
    @AddLog("修改用户信息")
    @Override
    public void updateUser(User user) {
        //设置条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(user.getId());
        //根据id查询视频
        User users = userMapper.selectOneByExample(example);
        //字符串拆分
        String[] imgSplit = users.getHeadImg().split("/");
        String headImg = imgSplit[imgSplit.length - 2] + "/" + imgSplit[imgSplit.length - 1];
        System.out.println(headImg);
        //删除封面
        AliyunOssUtil.delete("yingxue", headImg);
        user.setHeadImg(null);
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 删除用户
     *
     * @param user
     */
    @DelCach
    @AddLog("删除用户")
    @Override
    public void delete(User user) {

        userMapper.delete(user);
    }

    /**
     * 批量删除用户
     *
     * @param ids
     */
    @DelCach
    @AddLog("批量删除用户")
    @Override
    public void deleteUser(String[] ids) {
        UserExample example = new UserExample();
        example.createCriteria().andIdIn(Arrays.asList(ids));
        userMapper.deleteByExample(example);
    }


}
