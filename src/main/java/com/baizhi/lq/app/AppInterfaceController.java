package com.baizhi.lq.app;

import com.baizhi.lq.common.CommonResult;
import com.baizhi.lq.service.VideoService;
import com.baizhi.lq.util.AliyunSendPhoneUtil;
import com.baizhi.lq.vo.VideoVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 李瓊
 * @Date 2020/4/7 20:08
 */
@RestController
@RequestMapping("app")
public class AppInterfaceController {
    @Resource
    VideoService videoService;

    @RequestMapping("getPhoneCode")
    public CommonResult getPhoneCode(String phone) {
        HashMap<String, Object> map = new HashMap<>();
        System.out.println("手机号 = " + phone);
        String random = AliyunSendPhoneUtil.getRandom(6);
        System.out.println("验证码 = " + random);
        String message = AliyunSendPhoneUtil.sendCode(phone, random);
        System.out.println(message);

        if (message.equals("发送成功")) {
            return new CommonResult().success("100", "验证码发送成功", phone);
        } else {
            return new CommonResult().failed("验证码发送失败:" + message, null);
        }
    }

    /**
     * 首页查询视频信息接口
     *
     * @return
     */
    @RequestMapping("queryByReleaseTime")
    public CommonResult queryByReleaseTime() {
        try {
            //查询数据
            List<VideoVo> videoVos = videoService.queryByReleaseTime();
            return new CommonResult().success(videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }

    }


}
