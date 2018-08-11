package com.goldsudo.dao;

import com.goldsudo.bean.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Autowired
    SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        long id = 1004L;
        long userPhone = 13312344321L;
        int result = successKilledDao.insertSuccessKilled(id, userPhone);
        System.out.println("=====================================");
        System.out.println(result);
        System.out.println("=====================================");
    }

    @Test
    public void queryByIdWihtSeckill() {
        SuccessKilled sk = successKilledDao.queryByIdWihtSeckill(1004, 13312344321L);
        System.out.println("=====================================");
        System.out.println(sk);
        System.out.println("=====================================");
    }
}