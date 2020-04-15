package com.baizhi.lq.serviceImpl;

import com.baizhi.lq.dao.LogMapper;
import com.baizhi.lq.entity.Log;
import com.baizhi.lq.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 李瓊
 * @Date 2020/4/6 15:01
 */
@Service
public class LogServiceImpl implements LogService {
    @Resource
    LogMapper logMapper;

    @Override
    public List<Log> queryAllLog() {
        List<Log> logs = logMapper.selectAll();
        return logs;
    }
}
