package com.baizhi.lq.controller;

import com.baizhi.lq.entity.Log;
import com.baizhi.lq.service.LogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 李瓊
 * @Date 2020/4/6 15:02
 */
@RestController
@RequestMapping("log")
public class LogController {
    @Resource
    LogService logService;

    @RequestMapping("queryAllLog")
    public List<Log> queryAllLog() {
        List<Log> logs = logService.queryAllLog();
        System.out.println(logs);
        return logs;
    }
}
