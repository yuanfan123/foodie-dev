package com.imooc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Classname ServiceLogAspect
 * @Description
 * @Date 2020/3/4 1:04
 * @Created by lyf
 */
@Component
@Aspect
public class ServiceLogAspect {
    public  static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
    /**
     * Aop 通知
     *  1.前置通知：在方法之间执行
     *  2.后置通知：在方法之前调用通知
     *  3.环绕通知：在方法前后都分别执行的通知
     *  4.异常通知发送异常通知
     *  5.最终通知：方法之后通知 类似finally
     */

    /**
     * 切面表达式
     *      execution代表需要执行的表达式主体
     *      第一处 * 代表返回类型 * 代表所有类型
     *      第二处 包名，监控所有类所在的包
     *      第三处 .. 代表包及子包的所有类方法
     *      第四处 * 代表类名，* 代表所有类
     *      第五处 *(..) 代表方法中的方法名 ，（..）代表方法中的任意参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.imooc.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("======== 开始执行{}.{} ======",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        //记录开始时间
        long beginTime = System.currentTimeMillis();
        //执行目标对象 service
        Object result = joinPoint.proceed();
        //记录结束时间
        long end = System.currentTimeMillis();
        long takeTime = end - beginTime;
        if (takeTime>3000){
            logger.error("========== 执行结束，耗时：{}毫秒 ================",takeTime);
        }else if (takeTime>2000){
            logger.warn("========== 执行结束，耗时：{}毫秒 ================",takeTime);
        }else {
            logger.info("========== 执行结束，耗时：{}毫秒 ================",takeTime);
        }
        return result;
    }
}
