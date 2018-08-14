--数据库初始化脚本

--创建数据库
CREATE DATABASE seckill;

--使用数据库
use seckill;

--创建秒杀库存表
CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '库存量',
`start_time` timestamp NOT NULL COMMENT '秒杀开始时间',
`end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY(seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

--初始化秒杀数据
insert into seckill(name,number,start_time,end_time)
values('1000元秒杀iphone6',100,'2018-06-07 00:00:00','2018-06-07 00:00:00'),
('500元秒杀xxxxx',200,'2018-06-07 00:00:00','2018-09-07 00:00:00'),
('300元秒杀xxxx',300,'2018-06-07 00:00:00','2018-06-08 00:00:00'),
('200元秒杀xxx',400,'2018-06-07 00:00:00','2018-07-07 00:00:00'),
('100元秒杀小米路由器',500,'2018-08-05 00:00:00','2018-08-15 00:00:00');

--创建秒杀成功明细表
CREATE TABLE success_seckilled(
`seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
`user_phone` bigint NOT NULL COMMENT '用户手机号',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态表示：-1无效，0成功，1已付款 2已发货',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY(seckill_id,user_phone),
KEY idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

--连接数据库控制台
--mysql -u root -p

--执行秒杀入库操作事务的存储过程：excuteSeckill
--入参：seckillId userPhone seckillTime 出参：seckillResult(0:成功，-1：重复秒杀 -2：秒杀结束[库存为空或时间过期])
--步骤：
--0.set seckillResult = -99
--1.开启事务
--2.插入秒杀结果表(使用ignore忽略主键冲突)
--3.如果插入0条，回滚事务，set seckillResult为-1，存储过程结束
--4.如果插入1条，那么：
--	5.更新库存表，使库存减一
--	6.如果更新0条，回滚事务，set seckillResult为-2，存储过程结束
--	7.如果更新1条，提交事务，set seckillResult为0，存储过程结束

DELIMITER $$
CREATE PROCEDURE `seckill`.`excuteSeckill` (IN seckillId bigint,IN userPhone bigint,IN seckillTime timestamp,OUT seckillResult int)
BEGIN
  DECLARE result INT ;
  START TRANSACTION ;
  insert ignore into success_seckilled(seckill_id,user_phone,state)
        values (seckillId,userPhone,0);
  set result = ROW_COUNT();
  IF (result <= 0)
  THEN
    ROLLBACK ;
  	set seckillResult = -1;
  ELSE
  	update seckill set number = number-1
        where seckill_id = seckillId
        and start_time <= seckillTime
        and end_time >= seckillTime
        and number>0;
    set result = ROW_COUNT();
    IF (result <= 0)
    THEN
      ROLLBACK ;
    	set seckillResult = -2;
    ELSE
      COMMIT;
    	set seckillResult = 0;
    END IF;
   END IF;
END $$
DELIMITER ;

--测试存储过程
set @result = -99;--设置一个变量用于检测存储过程执行结果
select @result;
call seckill.excuteSeckill(1004,13312344321,'2018-08-08',@result);
select @result;--结果应为0
call seckill.excuteSeckill(1004,13312344321,'2018-08-08',@result);
select @result;--结果应为-1，重复秒杀
call seckill.excuteSeckill(1004,13312344321,'2099-08-08',@result);
select @result;--结果应为-2，秒杀时间过期