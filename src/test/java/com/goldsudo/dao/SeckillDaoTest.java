package com.goldsudo.dao;

import com.goldsudo.bean.Seckill;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() {

        Date today = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式时间对象
        try {
            today = sdf.parse("2018-08-06 12:00:00");
            int reduceNum = seckillDao.reduceNumber(1004L, today);
            System.out.println("=====================================");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getSeckillById() {

        long id = 1000;
        Seckill seckill = seckillDao.getSeckillById(id);
        System.out.println("=====================================");

        System.out.println(seckill.getName());
        System.out.println(seckill);
        Assert.assertEquals(seckill.getName(), "1000元秒杀iphone7");
    }

    @Test
    public void getAllSeckill() {

        List<Seckill> seckills = seckillDao.getAllSeckill(0, 100);

        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }
    }
}