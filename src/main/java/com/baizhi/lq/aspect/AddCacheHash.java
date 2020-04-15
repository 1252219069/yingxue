package com.baizhi.lq.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @Author 李瓊
 * @Date 2020/4/9 21:27
 */
@Configuration
@Aspect
public class AddCacheHash {
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(com.baizhi.lq.annotation.AddCach)")
    public Object addCahe(ProceedingJoinPoint proceedingJoinPoint) {
        //序列号解决乱码
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        StringBuilder stringBuilder = new StringBuilder();

        //key value 类型
        //key  类的全限定名+方法名+参数名
        //value 缓存的数据  string类型

        //KEY  Hash<Key,value>
        // 类全限定名   (方法名+参数名（实参）,数据)
        //    1方法   数据
        //    2方法   数据

        //获取全限定名
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        stringBuilder.append(methodName);
        //获取参数
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            stringBuilder.append(arg);
        }

        //拼接key   小key
        String key = stringBuilder.toString();

        //取出key
        Boolean aBoolean = redisTemplate.opsForHash().hasKey(className, key);
        HashOperations hashOperations = redisTemplate.opsForHash();

        Object result = null;
        //在redis判断key是否存在
        if (aBoolean) {
            //存在   缓存中有数据  直接取出数据
            result = hashOperations.get(className, key);
        } else {
            try {
                //不存在   缓存中没有   放行方法得到结果
                result = proceedingJoinPoint.proceed();
                //不存在  获取缓存结果  加入缓存中
                hashOperations.put(className, key, result);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return result;
    }

    @Around("@annotation(com.baizhi.lq.annotation.DelCach)")
    public Object delCach(ProceedingJoinPoint proceedingJoinPoint) {
        //获取类的全限定名
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        //删除该类下的所有的数据
        redisTemplate.delete(className);
        try {
            Object proceed = proceedingJoinPoint.proceed();
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
