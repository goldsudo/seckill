package com.goldsudo.service.impl;

import com.goldsudo.bean.Seckill;
import com.goldsudo.bean.SuccessKilled;
import com.goldsudo.dao.SeckillDao;
import com.goldsudo.dao.SuccessKilledDao;
import com.goldsudo.dao.cache.SeckillDaoCache;
import com.goldsudo.dto.Exposer;
import com.goldsudo.dto.SeckillExcution;
import com.goldsudo.enums.SeckillStateEnum;
import com.goldsudo.exception.RepeatKillException;
import com.goldsudo.exception.SeckillCloseException;
import com.goldsudo.exception.SeckillException;
import com.goldsudo.service.ISeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SeckillServiceImpl
 * @Description: TODO
 * @Author: goldsudo
 * @Date: 2018/8/7
 */
@Service
public class SeckillServiceImpl implements ISeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SeckillDao seckillDao;

    @Autowired
    SuccessKilledDao successKilledDao;

    @Autowired
    SeckillDaoCache seckillDaoCache;

    private final String SLAT = "KJHKJ(*&^&*^&*%^*$^%$$JHHKJHJK'':L";

    @Override
    public Seckill getSeckillById(long seckillId) {
        Seckill seckill = seckillDaoCache.getSeckillFromCache(seckillId);
        if (seckill == null) {
            seckill = seckillDao.getSeckillById(seckillId);
            seckillDaoCache.updateSeckill2Cache(seckill);
        }
        return seckill;
    }

    @Override
    public List<Seckill> getSeckills(int offset, int limit) {
        return seckillDao.getAllSeckill(offset, limit);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDaoCache.getSeckillFromCache(seckillId);
        if (seckill == null) {
            seckill = seckillDao.getSeckillById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            }
            seckillDaoCache.updateSeckill2Cache(seckill);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional
    public SeckillExcution excuteSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        //md5校验，避免伪造
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("您的操作不合法！invalid operation!");
        }
        try {
            return saveSeckillExcuteInfo2Db(seckillId, userPhone);

            /**
             * saveSeckillExcuteInfo2DbOptimization方法为优化后的saveSeckillExcuteInfo2Db方法，
             * 将入库秒杀结果的操作提前到更新库存的操作之前，
             * 使得事务持有mysql行锁的时间减少，从而提高并发量。
             *
             * return saveSeckillExcuteInfo2DbOptimization(seckillId,userPhone);
             *
             */
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            //捕获所有异常，转化成运行期异常，告诉spring声明式实物，进行事物回滚
            throw new SeckillException("Seckill inner error! " + e.getMessage(), e);
        }
    }

    @Override
    public SeckillExcution excuteSeckillByProcedure(long seckillId, long userPhone, String md5) {
        //md5校验，避免伪造
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("您的操作不合法！invalid operation!");
        }
        //执行存储过程之后，seckillResult将获得结果值
        try {
            //当前服务器时间
            Date nowTime = new Date();
            //参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("seckillId", seckillId);
            paramMap.put("userPhone", userPhone);
            paramMap.put("seckillTime", nowTime);
            paramMap.put("seckillResult", null);
            seckillDao.killByProcedure(paramMap);
            //获取seckillResult
            int result = MapUtils.getInteger(paramMap, "seckillResult");
            if (result == 0) {
                SuccessKilled successKilled = successKilledDao.queryByIdWihtSeckill(seckillId, userPhone);
                return new SeckillExcution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            } else if (result == -1) {
                //userPhone此电话号码重复秒杀了seckillId对应的产品a
                throw new RepeatKillException("您已经秒杀了该产品，请勿重复秒杀！");
            } else if (result == -2) {
                //没有更新到记录，代表库存小于1或日期已过期
                throw new SeckillCloseException("seckill is closed");
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            //捕获所有异常，转化成运行期异常，告诉spring声明式实物，进行事物回滚
            throw new SeckillException("Seckill inner error! " + e.getMessage(), e);
        }
        return null;
    }

    private SeckillExcution saveSeckillExcuteInfo2Db(long seckillId, long userPhone) throws Exception, SeckillCloseException, RepeatKillException {
        //当前服务器时间
        Date nowTime = new Date();
        //减库存
        int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
        if (updateCount <= 0) {
            //没有更新到记录，代表库存小于1或日期已过期
            throw new SeckillCloseException("seckill is closed");
        } else {
            //插入秒杀明细
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //userPhone此电话号码重复秒杀了seckillId对应的产品
                throw new RepeatKillException("您已经秒杀了该产品，请勿重复秒杀！");
            } else {
                SuccessKilled successKilled = successKilledDao.queryByIdWihtSeckill(seckillId, userPhone);
                return new SeckillExcution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            }
        }
    }

    private SeckillExcution saveSeckillExcuteInfo2DbOptimization(long seckillId, long userPhone) throws Exception, RepeatKillException, SeckillCloseException {
        //当前服务器时间
        Date nowTime = new Date();
        //插入秒杀明细
        int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        if (insertCount <= 0) {
            //userPhone此电话号码重复秒杀了seckillId对应的产品
            throw new RepeatKillException("您已经秒杀了该产品，请勿重复秒杀！");
        } else {
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //没有更新到记录，代表库存小于1或日期已过期
                throw new SeckillCloseException("seckill is closed");
            } else {
                SuccessKilled successKilled = successKilledDao.queryByIdWihtSeckill(seckillId, userPhone);
                return new SeckillExcution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            }
        }
    }

    private String getMD5(long secillId) {
        String base = secillId + "/" + SLAT;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
