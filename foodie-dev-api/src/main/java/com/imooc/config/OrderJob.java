package com.imooc.config;

import com.immmoc.utils.DateUtil;
import com.imooc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname OrderJob
 * @Description
 * @Date 2020/3/15 11:29
 * @Created by lyf
 */
@Component
public class OrderJob {
    @Autowired
    private OrderService orderService;

    /**
     * 使用定时任务处理超时未执行的任务，存在的问题
     *    1. 会有时间差   10：39下单 11：00 未关 12：00 超过时间21min
     *    2.不支持集群：使用集群后会有多个定时任务
     *                  解决方案：只使用一台执行机执行
*         3.会对数据库全表搜索，影响性能  select * from order where orderStatus = 10
     *          定时任务仅适合小型轻量项目，传统项目
     *    4.用MQ ->RabbitMq,RocketMq,kafka,zeroMq 使用延时任务（队列）
     *            10：39下单 ，未付款*（10）w未付款，11：39检查，如果当前状态为10，直接关闭订单
     */
    @Scheduled(cron = "0/3 * * 0/1 * ?")
    public void autoCloseOrder(){
        orderService.closeOrder();
    }
}
