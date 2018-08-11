package com.goldsudo.dao.cache;

import com.goldsudo.bean.Seckill;
import com.goldsudo.dao.SeckillDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoCacheTest {

    private final Logger LOGGER = LoggerFactory.getLogger(SeckillDaoCache.class);

    @Autowired
    SeckillDaoCache seckillDaoCache;

    @Autowired
    SeckillDao seckillDao;

    @Test
    public void getSeckillFromCache() {
        Seckill seckill = seckillDaoCache.getSeckillFromCache(1004L);
        if(seckill != null){
            LOGGER.info("***** get from cache success:"+seckill.toString());
        }else{
            LOGGER.info("***** get from cache fail");
        }
    }

    @Test
    public void updateSeckill2Cache() {
        Seckill seckill = seckillDao.getSeckillById(1004L);
        String result = seckillDaoCache.updateSeckill2Cache(seckill);
        assertEquals(result,"OK");
    }
}