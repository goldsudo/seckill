package com.goldsudo.bean;

import java.util.Date;

/**
 * @ClassName: SuccessKilled
 * @Description: 秒杀结果表实体对象
 * @Author: goldsudo
 * @Date: 2018/8/6
 */
public class SuccessKilled {
    //秒杀id
    private long seckillId;

    //用户手机号
    private long userPhone;

    //秒杀状态：-1无效，0成功，1已付款 2已发货
    private short state;

    //创建时间
    private Date createTime;

    //秒杀id对应的秒杀实体对象
    private Seckill seckill;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                ", seckill=" + seckill +
                '}';
    }
}
