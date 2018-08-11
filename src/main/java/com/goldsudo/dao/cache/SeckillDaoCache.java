package com.goldsudo.dao.cache;

import com.goldsudo.bean.Seckill;
import com.goldsudo.dao.SeckillDao;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName: SeckillDaoCache
 * @Description: TODO
 * @Author: goldsudo
 * @Date: 2018/8/7
 */
public class SeckillDaoCache {

    private final Logger LOGGER = LoggerFactory.getLogger(SeckillDaoCache.class);

    private final JedisPool pool;

    @Autowired
    SeckillDao seckillDao;

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    SeckillDaoCache(String host, int port) {
        pool = new JedisPool(host, port);
    }

    public Seckill getSeckillFromCache(long seckillId) {
        try {
            Jedis jedis = pool.getResource();
            try {
                byte[] key = ("seckill:" + seckillId).getBytes();
                byte[] values = jedis.get(key);
                //缓存命中
                if (values != null) {
                    Seckill seckill = schema.newMessage();
                    //反序列化
                    ProtostuffIOUtil.mergeFrom(values, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            //缓存服务挂了，仅记录日志不抛出异常，避免业务因此终结
            LOGGER.error(e.getMessage(), e);
        }
        //未命中
        return null;
    }

    public String updateSeckill2Cache(Seckill seckill) {
        try {
            Jedis jedis = pool.getResource();
            try {
                byte[] key = ("seckill:" + seckill.getSeckillId()).getBytes();
                //序列化
                byte[] values = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                String result = jedis.setex(key, 3600, values);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
