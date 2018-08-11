package com.goldsudo.dto;

/**
 * 暴露秒杀接口DTO
 */
public class Exposer {

    //是否开启秒杀
    private boolean exposed;

    //加密
    private  String md5;

    private long seckillId;

    //当前系统时间（毫秒）
    private long now;

    //开始时间
    private long starttime;

    //结束时间
    private long endtime;

    //成功
    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    //失败
    public Exposer(boolean exposed, long seckillId, long now, long starttime, long endtime) {
        this.exposed = exposed;
        this.now = now;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                '}';
    }
}
