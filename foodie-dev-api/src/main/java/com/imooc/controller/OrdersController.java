package com.imooc.controller;


import com.immmoc.enums.OrderStatusEnum;
import com.immmoc.enums.PayMethod;
import com.immmoc.utils.IMOOCJSONResult;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "订单相关",tags = {"订单相关接口controller"})
@RestController
@RequestMapping("orders")
public class OrdersController  extends BaseController{
    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    private final static Logger logger = LoggerFactory.getLogger(OrdersController.class);
    @PostMapping("/create")
    @ApiOperation(value = "用户下单", notes ="用户下单",httpMethod = "POST")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO subMitOrderBo,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        logger.info("订单信息{}",subMitOrderBo);
        if (subMitOrderBo.getPayMethod()!= PayMethod.WEIXIN.type
                && subMitOrderBo.getPayMethod()!= PayMethod.ALIPAY.type){
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        //1.创建订单
        OrderVO orderVo = orderService.createOrder(subMitOrderBo);
        String orderId = orderVo.getOrderId();
        //2.创建订单后，移除购物车中已结算（已提交）的商品
        //TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前段的cookie
        // CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);
        //3.向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVo.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);
        merchantOrdersVO.setAmount(1);

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.add("imoocUserId","imooc");
//        httpHeaders.add("'password","imooc");
//        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO,httpHeaders);
//        ResponseEntity<IMOOCJSONResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity, IMOOCJSONResult.class);
//
//        IMOOCJSONResult paymentResult = responseEntity.getBody();
        IMOOCJSONResult paymentResult = IMOOCJSONResult.ok();
        if (paymentResult.getStatus() != 200) {
            return IMOOCJSONResult.errorMsg("创建订单失败，请练习管理员");
        }
        return IMOOCJSONResult.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId){
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

}
