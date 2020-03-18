package com.imooc.controller;


import com.immmoc.utils.IMOOCJSONResult;
import com.immmoc.utils.MobileEmailUtils;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import com.imooc.pojo.bo.ShopcartBo;
import com.imooc.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "地址相关",tags = {"地址相关接口controller"})
@RestController
@RequestMapping("address")
public class AddressController {
    /**
     * 用户在确认订单页面，可以针对地址做如下操作：
     *      1.查询用户的所有收货地址列表
     *      2.新增收货地址
     *      3.删除收货地址
     *      4，修改收货地址
     *      5.设置默认地址
     */
    @Autowired
    private AddressService addressService;
    private final static Logger logger = LoggerFactory.getLogger(AddressController.class);
    @PostMapping("/list")
    @ApiOperation(value = "根据用户id查询收货列表", notes ="根据用户id查询收货列表",httpMethod = "POST")
    public IMOOCJSONResult list(@RequestParam String userId){
        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("");
        }
        List<UserAddress> list = addressService.queryAll(userId);
        return IMOOCJSONResult.ok(list);
    }
    @PostMapping("/add")
    @ApiOperation(value = "用户新增地址", notes ="用户新增地址",httpMethod = "POST")
    public IMOOCJSONResult add(@RequestBody AddressBo addressBo){
        IMOOCJSONResult ckResult = checkAddress(addressBo);
        if (ckResult.getStatus()!=200){
            return ckResult;
        }
        addressService.addNewUserAddress(addressBo);
        return IMOOCJSONResult.ok();
    }
    private IMOOCJSONResult checkAddress(AddressBo addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }

        return IMOOCJSONResult.ok();
    }
    @PostMapping("/update")
    @ApiOperation(value = "用户修改地址", notes ="用户修改地址",httpMethod = "POST")
    public IMOOCJSONResult update(@RequestBody AddressBo addressBo){
        if(StringUtils.isBlank(addressBo.getAddressId())){
            return IMOOCJSONResult.errorMsg("修改地址错误：addressId不能为空");
        }
        IMOOCJSONResult ckResult = checkAddress(addressBo);
        if (ckResult.getStatus()!=200){
            return ckResult;
        }
        addressService.updateUserAddress(addressBo);
        return IMOOCJSONResult.ok();
    }
    @PostMapping("/delete")
    @ApiOperation(value = "用户删除地址", notes ="用户删除地址",httpMethod = "POST")
    public IMOOCJSONResult delete(@RequestParam String  userId,
                                  @RequestParam String  addressId){
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("");
        }
        addressService.deleteUserAddress(userId,addressId);
        return IMOOCJSONResult.ok();
    }

    @PostMapping("/setDefalut")
    @ApiOperation(value = "用户设置默认地址", notes ="用户设置默认地址",httpMethod = "POST")
    public IMOOCJSONResult setDefalut(@RequestParam String  userId,
                                  @RequestParam String  addressId){
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("");
        }
        addressService.userAddressToBeDefault(userId,addressId);
        return IMOOCJSONResult.ok();
    }
}
