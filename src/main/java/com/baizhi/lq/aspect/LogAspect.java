package com.baizhi.lq.aspect;

import com.baizhi.lq.annotation.AddLog;
import com.baizhi.lq.dao.LogMapper;
import com.baizhi.lq.entity.Admin;
import com.baizhi.lq.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * @Author 李瓊
 * @Date 2020/4/6 14:07
 */
@Aspect
@Configuration
public class LogAspect {
    @Resource
    LogMapper logMapper;
    @Resource
    HttpSession session;

    @Around("@annotation(com.baizhi.lq.annotation.AddLog)")
    public Object addLogs(ProceedingJoinPoint joinPoint) throws Throwable {
        //谁   什么时间   做了什么事
        Admin admin = (Admin) session.getAttribute("admin");
        //时间   new date();

        //操作那个方法
        String methodName = joinPoint.getSignature().getName();
        //获取方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法上的注解
        AddLog addLog = method.getAnnotation(AddLog.class);
        //获取注解中的值
        String value = addLog.value();
        Object proceed = null;
        //放行方法
        try {
            proceed = joinPoint.proceed();
            String message = "success";

            Log log = new Log();
            String id = UUID.randomUUID().toString().replace("-", "");
            log.setId(id);
            log.setAdminName(admin.getName());
            log.setDate(new Date());
            log.setOperation(value);
            log.setStatus(message);
            logMapper.insert(log);
            System.out.println(log);

            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            String message = "error";
            Log log = new Log();
            String id = UUID.randomUUID().toString().replace("-", "");
            log.setId(id);
            log.setAdminName(admin.getName());
            log.setDate(new Date());
            log.setOperation(value);
            log.setStatus(message);
            logMapper.insert(log);
            System.out.println(log);
            return proceed;
        }
    }
}
