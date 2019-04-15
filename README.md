## 前言

　　`zjh_simple`项目快速开发框架，为现有的项目提供一套简单快捷开发的分布式框架。

---

## 项目介绍

　　基于SpringBoot(2.0.2.RELEASE)+Mybatis+Dubbo分布式敏捷开发系统架构，提供基础公共微服务服务模块。致力于业务和框架解耦，提高框架扩展性，目标是统一现有中小型项目的架构，方便各位快速开发。

---

### 组织结构

``` lua
addplus_dubbo
├── addplus_connector -- 数据库连接模块
├── addplus_api -- api接口模块
├── addplus_shiro -- shiro实现模块
├── addplus_provider -- Restful接口服务提供者
├── addplus_consumer -- Restful接口消费者
```

---

### 技术选型
技术 | 名称 | 官网
----|------|----
Spring Framework | 容器  | [http://projects.spring.io/spring-framework/](http://projects.spring.io/spring-framework/)
SpringMVC | MVC框架  | [http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc](http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc)
Apache Shiro | 安全框架  | [http://shiro.apache.org/](http://shiro.apache.org/)
Spring session | 分布式Session管理  |
MyBatis | ORM框架  | [http://www.mybatis.org/mybatis-3/zh/index.html](http://www.mybatis.org/mybatis-3/zh/index.html)
MyBatis Generator | 代码生成  | [http://www.mybatis.org/generator/index.html](http://www.mybatis.org/generator/index.html)
PageHelper | MyBatis物理分页插件  | [http://git.oschina.net/free/Mybatis_PageHelper](http://git.oschina.net/free/Mybatis_PageHelper)
Druid | 数据库连接池  | [https://github.com/alibaba/druid](https://github.com/alibaba/druid)
Thymeleaf | 模板引擎  | [http://www.thymeleaf.org/](http://www.thymeleaf.org/)
ZooKeeper | 分布式协调服务  | [http://zookeeper.apache.org/](http://zookeeper.apache.org/)
Dubbo | 分布式服务框架  | [http://dubbo.io/](http://dubbo.io/)
Redis | 分布式缓存数据库  | [https://redis.io/](https://redis.io/)
Log4J | 日志组件  | [http://logging.apache.org/log4j/1.2/](http://logging.apache.org/log4j/1.2/)
AliOSS & Qiniu & QcloudCOS | 云存储  | [https://www.aliyun.com/product/oss/](https://www.aliyun.com/product/oss/) [http://www.qiniu.com/](http://www.qiniu.com/) [https://www.qcloud.com/product/cos](https://www.qcloud.com/product/cos)
Protobuf & json | 数据序列化  | [https://github.com/google/protobuf](https://github.com/google/protobuf)
Maven | 项目构建管理  | [http://maven.apache.org/](http://maven.apache.org/)

---

#### 模块依赖

1. addplus_connector --> addplus_api
2. addplus_api --> addplus_privider、addplus_privider
3. addplus_shiro --> addplus_consumer
---

#### 模块介绍

> addplus_connector

Mysql、Redis、MongoDB等公共数据源连接配置，暂只支持配置单数据源，后续更新支持多数据源配置。Redis支持配置单/多节点、集群模式。需要增加新的数据源在此添加

> addplus_api

MyBatis、SpringDataRedis、Morphia数据库操作方法类，例如：mapper,mongodao统一在api写，方便各个provider直接调用操作数据库。pojo、service也统一实现在这里，方便各个工程调用

> addplus_shiro

基于Shiro权限控制框架集成的权限模块，使用redis实现分布式session以及内部进行了优化处理，处理速度会比原生使用shiro速度更快，控制细粒度更高。其他工程需要使用到权限框架，可单独抽出快速集成


> addplus_provider

基于dubbo分布式框架的Restful服务提供者，在Filter实现自有token校验机制，确保consumer发送合法请求，加强http安全

> addplus_consumer

基于dubbo分布式框架的Restful消费者，提供统一Controller，一般不需要另外写控制层。同时提供参数加解密方法，进一步加强请求安全性。提供有js和ES6的javascript加解密方法。

---
### 功能特别说明

#### token校验
- js加密文件在addplus_rest_consumer的resource下面。启动addplus_rest_consumer工程，访问：http://localhost:8080/index 可以展示加密后数据
- 前端用户登录后，需要返回Token信息，token里面有：accessKey和secretKey。accesskey用于拼接token。secretKey用于加密token的密钥。注意secretKey需要前端进行存储，不在网络中进行传输
- token组成：token : accessKey+ sign（sign:加密后的当前网络时间戳。请使用Aes加密,密钥：secretKey，偏移量：addplus+addplus+）
- 每次请求需要token的接口都需要生成新的token，每个token有效期暂定是5s。超过时间后请重新发起请求
- 校验机制：不需要token的接口在对应Service方法实现类添加@NotToken,标明不检验token。其他接口会先通过accessKey获取redis存储的secretKey,如果没有则说明需要重新登录。如果存在则通过secretKey解密sign信息获取对应时间戳，检验当前时间戳是否在5s内。校验通过则发送请求，没有则抛出对应异常码

#### 参数加解密
- 可以通过配置param.encryted标注是否需要参数加密，方便开发时候数据调用查看
- 参数加解密采用AES，与上述token加解密方法一致，只是对应偏移量不一样
- 参数加密规则：使用对应请求方法，名称生成SHA1的的字符，截取前16位的数字作为密钥。例如：sayHello -->f5993492c8a0d66e ,生成的字符串是加解密的密钥。
- get请求不做入参加密，post需要对入参进行加密。启用加密后返回报文是加密后的字符串，解密后是json字符串，需要自行处理

#### 注意点
- token机制和参数加解密目的是让我们构建程序更加安全，抵御网络攻击请求。
- 建议所有向服务器提交数据使用post并且加上token和参数加解密，防止被攻击。
- 上述有问题的可以随时与作者进行沟通。

### 编译流程

初次克隆后maven编译安装addplus_dubbo/pom.xml文件即可

### 启动顺序
1. addplus_provider
2. addplus_provider
5. 优先provider服务提供者，再启动其他consumer

### 后续展望
- 后续继续优化该项目，会根据现有情况架构进行框架调整，力求达到高效开发
- 会及时更新最新的springboot和dubbo版本，尽量降低后续
- 欢迎大家一起完善该项目，谢谢！