package com.imooc.service;

import com.imooc.pojo.Carousel;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.OrderVO;

import java.util.List;

/**
 * @Classname OrderService
 * @Description
 * @Date 2020/3/7 10:52
 * @Created by lyf
 */
public interface OrderService {

    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     */
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId,Integer orderStatus);

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder();

    /**
     * 关闭
     * @param orderId
     */
    public  void doCloseOrder(String orderId);
}
