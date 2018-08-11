package com.goldsudo.dao;

import com.goldsudo.bean.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SeckillDao
 * @Description: Seckill的DAO层方法接口
 * @Author: goldsudo
 * @Date: 2018/8/6
 */
public interface SeckillDao {
    /**
     * @return int
     * @Author Klay.Wang
     * @Description 使得由seckillID指定的商品的库存量减1，
     * 减库存前保证库存量大于1，且保证当前时间属于商品的秒杀开放日期范围
     * @Date 2018/8/6
     * @Param [seckillId, seckillTime]
     **/
    public int reduceNumber(@Param("seckillId") long seckillId, @Param("seckillTime") Date seckillTime);

    /**
     * @return com.goldsudo.bean.Seckill
     * @Author Klay.Wang
     * @Description 返回由seckillID指定的商品对象
     * @Date 2018/8/6
     * @Param [seckillId]
     **/
    public Seckill getSeckillById(long seckillId);

    /**
     * @return java.util.List<com.goldsudo.bean.Seckill>
     * @Author Klay.Wang
     * @Description 根据偏移量offset与限制量limit返回商品列表
     * @Date 2018/8/6
     * @Param [offset, limit]
     **/
    public List<Seckill> getAllSeckill(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * @return void
     * @Author Klay.Wang
     * @Description 通过存储过程执行秒杀操作
     * @Date 2018/8/8
     * @Param [paramMap]
     **/
    void killByProcedure(Map<String, Object> paramMap);
}
