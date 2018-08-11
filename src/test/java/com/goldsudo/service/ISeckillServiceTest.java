package com.goldsudo.service;

import com.goldsudo.bean.Seckill;
import com.goldsudo.dto.Exposer;
import com.goldsudo.dto.SeckillExcution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class ISeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISeckillService seckillSerivce;

    @Test
    public void getSeckillById() {
        long id = 1004L;
        Seckill seckill = seckillSerivce.getSeckillById(id);
        logger.info("seckill="+seckill.toString());
    }

    @Test
    public void getSeckills() {
        List<Seckill> list = seckillSerivce.getSeckills(0,10);
        for (Seckill seckill : list) {
            logger.info("seckill_list="+seckill.toString());
        }
    }

    @Test
    public void exportSeckillUrl() {
        long id = 1001L;
        Exposer exposer = seckillSerivce.exportSeckillUrl(id);
        logger.info("exposer="+exposer.toString());
    }

    @Test
    public void excuteSeckill() {
        long id = 1004L;
        long userPhone = 12200003333L;
        String md5 = "98e229df145e5071fe750c3ca541b3c1";
        SeckillExcution seckillExcution = seckillSerivce.excuteSeckill(id,userPhone,md5);
        logger.info("seckillExcution = {}",seckillExcution.toString());
    }

    @Test
    public void excuteSeckillByProcedure() {
        long id = 1004L;
        long userPhone = 12200005555L;
        String md5 = "98e229df145e5071fe750c3ca541b3c1";
        SeckillExcution seckillExcution = seckillSerivce.excuteSeckillByProcedure(id,userPhone,md5);
        logger.info("seckillExcution = {}",seckillExcution.toString());
    }
}