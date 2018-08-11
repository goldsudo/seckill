package com.goldsudo.service;

import com.goldsudo.bean.Seckill;
import com.goldsudo.dto.Exposer;
import com.goldsudo.dto.SeckillExcution;
import com.goldsudo.exception.RepeatKillException;
import com.goldsudo.exception.SeckillCloseException;
import com.goldsudo.exception.SeckillException;

import java.util.List;

/**
 * @ClassName: ISeckillService
 * @Description: TODO
 * @Author: goldsudo
 * @Date: 2018/8/7
 */
public interface ISeckillService {
    /**
     * @Author Klay.Wang
     * @Description 根据商品id查询商品详情
     * @Date 2018/8/7
     * @Param [seckillId]
     * @return com.goldsudo.bean.Seckill
     **/
    public Seckill getSeckillById(long seckillId);

    /**
     * @Author Klay.Wang
     * @Description 根据偏移量查询商品列表
     * @Date 2018/8/7
     * @Param [offset, limit]
     * @return com.goldsudo.bean.Seckill
     **/
    public List<Seckill> getSeckills(int offset, int limit);

    /**
     * 输入秒杀接口的地址
     * @param seckillId
     * 对秒杀接口进行加密，防止盗用
     */
    Exposer exportSeckillUrl(long seckillId);


    /**
     * 执行秒杀
     * @param  seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExcution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;

    /**
     * 执行秒杀 通过存储过程
     * @param  seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExcution excuteSeckillByProcedure(long seckillId, long userPhone, String md5);
}
