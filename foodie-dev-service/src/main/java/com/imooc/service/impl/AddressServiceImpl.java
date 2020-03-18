package com.imooc.service.impl;

import com.immmoc.enums.YesOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import com.imooc.service.AddressService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Classname AddressServiceImpl
 * @Description TODO
 * @Date 2020/3/9 20:49
 * @Created by lyf
 */
@Service
public class AddressServiceImpl  implements AddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserAddress> queryAll(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNewUserAddress(AddressBo addressBo) {
        //1.判断用户是否存在地址，没有，则新增为·默认地址·
        List<UserAddress> userAddresses = this.queryAll(addressBo.getUserId());
        Integer isDefault = 0;
        if (userAddresses == null || userAddresses.isEmpty()|| userAddresses.size()==0) {
            isDefault = 1;
        }
        String addressId = sid.nextShort();
        //2.保存地址到数据库
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBo,userAddress);
        userAddress.setId(addressId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddress(AddressBo addressBo) {
        String addressId = addressBo.getAddressId();
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBo,userAddress);
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        userAddressMapper.delete(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void userAddressToBeDefault(String userId, String addressId) {
        //1.产兆默认地址设置不默认
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setIsDefault(1);
        List<UserAddress> list = userAddressMapper.select(userAddress);
        list.stream().forEach(n-> {
            n.setIsDefault(YesOrNo.No.type);
            userAddressMapper.updateByPrimaryKeySelective(n);
        });
        //2.根据地址id修改为默认地址
        UserAddress userAddress1 = new UserAddress();
        userAddress1.setUserId(userId);
        userAddress1.setId(addressId);
        userAddress1.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(userAddress1);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress1 = new UserAddress();
        userAddress1.setUserId(userId);
        userAddress1.setId(addressId);
        return userAddressMapper.selectOne(userAddress1);
    }
}
