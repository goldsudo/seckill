## seckill项目介绍
seckill是一个完整秒杀商品的demo项目，来源慕课网课程《Java高并发秒杀系统实践》。<br>
课程地址：https://www.imooc.com/u/2145618/courses?sort=publish <br>
原课程讲的非常详细，但也因此该课程的老师也没有将源码贴在教程中，以督促学习视频的同学自己动手进行实践。<br>
我学习完课程后，参照着课程的讲解写出来了该项目，毕竟编程这件事情，想要提高的话，无他唯手熟尔。<br>

## 技术栈介绍

### 一、后端
这个项目涵盖了目前比较流行的后端技术栈：
1. 使用maven构建项目的jar包依赖
2. 数据库选用mysql
3. 使用redis作为缓存层
4. 使用junit4作为单元测试框架
5. 选择slf4j与logback作为日志工具
6. 使用spring管理bean与声明式事务控制
7. 使用spring与mybatis整合作为dao层实现
8. 最使用springMVC管理请求分发，并实现RESTful风格的api

### 二、前端
由于该项目主要是用于学习后端的相关框架与工具，因此前端部分的内容比较简陋，只是简单的使用bootstrap与jquery配合jstl实现了几个简单的交互页面。<br>
但是值得一提的是，这个课程的老师在实现前端jquery时所用模块化编程思维是很值得借鉴的。

## 项目环境搭建与启用
1. 在本地安装mysql，maven，redis（可选的，不安装也可以），具体安装方法请参照网上的教程，这里不再赘述
2. clone或者直接下载源码到本地
3. 打开mysql客户端，在本地的项目目录下打开src/main/sql/shema.sql文件，将其中的建表，初始化数据以及存储过程相关的sql全都执行一遍
4. 使用eclipse，myeclipse，idea或者其他IDE以导入maven项目的方式导入本项目，等待jar包依赖下载完毕
5. 在本地的项目目录下打开src/main/resources/jdbc.properties文件，将其中的配置修改为本地的mysql配置
6. 使用IDE执行所有单元测试，如果单元测试执行通过证明环境已经搭建完成
注：如果本地没有安装redis服务端的话，有一条测试用例将通过不了，但是不影响使用，因为service代码中做了防止redis挂掉的额外处理
7. 使用IDE自带的tomcat或者jetty等中间件启动项目即可在本地浏览，也可以使用maven package达成war包后直接部署到tomcat上进行测试

## 项目截图
项目启动成功后将显示如下页面：

![image](https://github.com/goldsudo/seckill/blob/master/SNAP-SHOT/home.png?raw=true)

列表页：

![image](https://github.com/goldsudo/seckill/blob/master/SNAP-SHOT/list.png?raw=true)

详情页，提示输入手机号：

![image](https://github.com/goldsudo/seckill/blob/master/SNAP-SHOT/phone.png?raw=true)

手机号码校验：

![image](https://github.com/goldsudo/seckill/blob/master/SNAP-SHOT/wrong-phone.png?raw=true)

秒杀界面：

![image](https://github.com/goldsudo/seckill/blob/master/SNAP-SHOT/start.png?raw=true)

秒杀成功：

![image](https://github.com/goldsudo/seckill/blob/master/SNAP-SHOT/success.png?raw=true)

重复秒杀：

![image](https://github.com/goldsudo/seckill/blob/master/SNAP-SHOT/repeat.png?raw=true)

秒杀尚未开始时，展示倒计时：

![image](https://github.com/goldsudo/seckill/blob/master/SNAP-SHOT/countdown.png?raw=true)

