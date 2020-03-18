package com.imooc.service.impl;

import com.imooc.service.StuService;
import com.imooc.service.TestTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestTransServiceImpl implements TestTransService {
    @Autowired
    StuService stuService;

    /**
     * 事务的传播 - Propagation
     * REQUIRED:1. 使用当前事务，如果没有事务，则自己创建一个事务，子方法是必须运行在一个事务中；
     *          2.如果当前存在事务则加入事务则成为整体
     *          举例：领导没饭吃，我自己买，领导有的吃，蹭吃
     * SUPPORTS:如果当前有事务，则使用事务；如果当前没有事务，则不适用事务
     * MANDATORY: 该强制属性必须存在，如果不存在，抛异常
     *             举例：领导必须管饭，不管抛异常
     * REQUIRES_NEW:如果当前有事务，则挂起该事务，并创建自己的一个新事物给自己
     *               如果当前没有事务同required((上一级出现回滚不影响当前事务提交)
     * NOT_SUPPORTED:如果当前有事务，则把事务挂起，自己不使用事务去运行数据库操作
     * NERVER :如果当前存在事务，则抛异常
     * NESTED: 如果当前有事务，则开启子事务（嵌套事务），嵌套事务 独立提交或回滚；
     *          如果当前没有事务，同required
     *          如果主事务提交，则会携带父类事务提交，如果子事务回滚，则子事务会一起回滚
     *          如果catch子事务异常，父事务不会回滚
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void testPropagationTrans() {
        stuService.saveParent();
            stuService.saveChildren();

//        int i = 1/0;
    }
}
