package com.goldsudo.dao;

import com.goldsudo.bean.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: SuccessKilledDao
 * @Description: SuccessKilled的DAO层方法接口
 * @Author: goldsudo
 * @Date: 2018/8/6
 */
public interface SuccessKilledDao {
    /**
     * @return int
     * @Author Klay.Wang
     * @Description 插入成功购买明细
     * @Date 2018/8/6
     * @Param [seckillId, userPhone]
     **/
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * @return com.goldsudo.bean.SuccessKilled
     * @Author Klay.Wang
     * @Description 根据id获取SucessKilled商品
     * @Date 2018/8/6
     * @Param [seckillId, userPhone]
     **/
    SuccessKilled queryByIdWihtSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
