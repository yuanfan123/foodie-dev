package com.imooc.service.impl;

import com.imooc.mapper.StuMapper;
import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StuServiceImpl implements StuService {
    @Autowired
    private StuMapper stuMapper;
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Stu getStuInfo(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveStu() {
        Stu stu = new Stu();
        stu.setName("jack1");
        stu.setAge(18);
        stuMapper.insertSelective(stu);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateStu(int id) {
        Stu stu = new Stu();
        stu.setId(id);
        stu.setName("jack1");
        stu.setAge(100);
        stuMapper.updateByPrimaryKey(stu);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteStu(int id) {
        Stu stu = new Stu();
        stu.setId(id);
        stu.setName("jack");
        stu.setAge(10);
        stuMapper.deleteByPrimaryKey(stu);
    }

    public void saveParent(){
        Stu stu = new Stu();
        stu.setName("Parent");
        stu.setAge(18);
        stuMapper.insert(stu);
    }
    @Transactional(propagation = Propagation.NESTED)
    public void saveChildren(){
        try {
            saveChild();
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveChild2();
    }

    public void saveChild() {
        Stu stu = new Stu();
        stu.setName("child-1");
        stu.setAge(18);
        int a = 1/0;
        stuMapper.insert(stu);
    }
    public void saveChild2() {
        Stu stu = new Stu();
        stu.setName("child-2");
        stu.setAge(18);
        stuMapper.insert(stu);
    }

}
