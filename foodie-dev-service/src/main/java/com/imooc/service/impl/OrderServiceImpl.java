package com.imooc.service.impl;

import com.immmoc.enums.OrderStatusEnum;
import com.immmoc.enums.YesOrNo;
import com.immmoc.utils.DateUtil;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * @Classname OrderServiceImpl
 * @Description
 * @Date 2020/3/7 10:54
 * @Created by lyf
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private  OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        //邮费设置为0
        Integer postAmount = 0;
        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);
        //1.新订单数据保存
        Orders orders = new Orders();
        String orderId = sid.nextShort();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverAddress(userAddress.getProvince() + " " + userAddress.getCity()
                + " " + userAddress.getDistrict() + " " + userAddress.getDetail());
        orders.setReceiverMobile(userAddress.getMobile());

        orders.setPostAmount(postAmount);
        orders.setPayMethod(payMethod);
        orders.setLeftMsg(leftMsg);
        orders.setIsComment(YesOrNo.No.type);
        orders.setIsDelete(YesOrNo.No.type);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());

        //2.循环规格的itemSpecIds保存商品订单信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        //TODO 商品数量默认为1 整合redis后从redis中获取
        Integer buyCount = 1;
        AtomicReference<Integer> totalAmount = new AtomicReference<>(0);//原价累计
        AtomicReference<Integer> realpayAmount = new AtomicReference<>(0);//优惠后的实际支付价格累计
        Stream.of(itemSpecIdArr).map(itemService::queryItemSpecById).forEach(n -> {
            // 2.1 获取主要的价格
            totalAmount.updateAndGet(v -> v + n.getPriceNormal() * buyCount);
            realpayAmount.updateAndGet(v -> v + n.getPriceDiscount() * buyCount);
            //2.2 根据商品id,获得商品信息以及商品图片
            String itemId = n.getItemId();
            Items items = itemService.queryItemsById(itemId);
            String imgUrl = itemService.queryItemMainImageById(items.getId());
            //2.3循环保存订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems orderItems = new OrderItems();
            orderItems.setId(subOrderId);
            orderItems.setOrderId(orderId);
            orderItems.setItemId(itemId);
            orderItems.setItemName(items.getItemName());
            orderItems.setItemImg(imgUrl);
            orderItems.setItemSpecId(n.getId());
            orderItems.setBuyCounts(buyCount);
            orderItems.setItemSpecName(n.getName());
            orderItems.setPrice(n.getPriceDiscount());
            orderItemsMapper.insert(orderItems);

            //2.4在用户订单中扣除库存
            itemService.decreaseItemSpecStock(n.getId(),buyCount);
        });
        orders.setTotalAmount(totalAmount.get());
        orders.setRealPayAmount(realpayAmount.get());
        ordersMapper.insert(orders);
        //3.保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);
        //4.构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realpayAmount.get()+postAmount);
        merchantOrdersVO.setPayMethod(payMethod);
        //5.构建自定义VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        return  orderVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void closeOrder() {
        //查询所有未付款订单，判断时间是否超时（1天）
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(queryOrder);
        list.stream().forEach(n->{
            //获取订单创建时间
            Date createdTime = n.getCreatedTime();
            //与当前时间对比
            int days = DateUtil.daysBetween(createdTime,new Date());
            if (days>=1){
                //超过一日关闭订单
                doCloseOrder(n.getOrderId());
            }
        });
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public  void doCloseOrder(String orderId) {
        OrderStatus close = new OrderStatus();
        close.setOrderId(orderId);
        close.setOrderStatus(OrderStatusEnum.CLOSE.type);
        close.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(close);
    }
}
