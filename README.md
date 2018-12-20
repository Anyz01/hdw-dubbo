## hdw-dubbo
- hdw-dubbo微服务化开发平台，具有统一授权、认证后台管理系统，其中包含具备用户管理、资源权限管理等多个模块，支持多业务系统并行开发，可以作为后端服务的开发脚手架。代码简洁，架构清晰，适合学习和直接项目中使用。
- 核心技术采用SpringBoot、Dubbo、Mybatis、Mybatis-plus、Druid、Redis、ActiveMQ、Quartz、JWT Token等主要框架和中间件。前端采用vue-element-ui组件。
- 前后端分离，通过token进行数据交互，可独立部署
- 灵活的权限控制，可控制到页面或按钮，满足绝大部分的权限需求
- 页面交互使用Vue2.x，极大的提高了开发效率
- 完善的代码生成机制，可在线生成entity、xml、dao、service、vue、sql代码，减少70%以上的开发任务
- 引入dubbo服务治理
- 引入quartz定时任务，可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能
- 引入API模板，根据token作为登录令牌，极大的方便了APP接口开发
- 引入Hibernate Validator校验框架，轻松实现后端校验
- 引入swagger文档支持，方便编写API接口文档
- 前端地址：https://github.com/tumao2/hdw-dubbo-vue
- 演示地址：http://locahost:8004 (账号密码：admin/123456)


## 运行项目
- 1.安装Redis、zookeeper 、ActiveMQ
- 2.启动Redis 、zookeeper、ActiveMQ
- 3.启动MonitorApplication
- 4.启动UpmsServiceApplication
- 5.等待UpmsServiceApplication完全启动后，启动UpmsWebApplication
- 6.默认用户
用户名：admin
密码：123456
- 7.启动前端

## 开发计划
- elasticsearch集成
- activeMQ 、WebSocket消息推送
- 分布式事物处理

## 技术交流
- hdw-dubbo技术交流群：948405874

## 系统预览
![image](https://github.com/tumao2/hdw-dubbo/blob/master/img/QQ%E6%88%AA%E5%9B%BE20181219204905.png)
![image](https://github.com/tumao2/hdw-dubbo/blob/master/img/QQ%E6%88%AA%E5%9B%BE20181219210136.png)
![image](https://github.com/tumao2/hdw-dubbo/blob/master/img/QQ%E6%88%AA%E5%9B%BE20181219210210.png)
![image](https://github.com/tumao2/hdw-dubbo/blob/master/img/QQ%E6%88%AA%E5%9B%BE20181219210441.png)
![image](https://github.com/tumao2/hdw-dubbo/blob/master/img/QQ%E6%88%AA%E5%9B%BE20181219210502.png)
![image](https://github.com/tumao2/hdw-dubbo/blob/master/img/hdw-dubbo%E6%8A%80%E6%9C%AF%E4%BA%A4%E6%B5%81%E7%BE%A4%E7%BE%A4%E4%BA%8C%E7%BB%B4%E7%A0%81.png)



## 说明文档
项目开发、部署等说明都在[wiki](https://github.com/tumao2/hdw-dubbo/wiki)中。


## 更新日志
每个版本的详细更改都记录在[release notes](https://github.com/tumao2/hdw-dubbo/releases)中。
