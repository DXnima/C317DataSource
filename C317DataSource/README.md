# C317DataUI多数据源连接后端

## 主要技术和环境
| java 1.8.0_281
| maven 3.6.3
| tomcat 9
| Spring Boot
| Mybaits
| MongoDB
| Java JDBC

## 主要实现
通过查看[RdbmsSyncTool 关系型数据库同步工具](https://gitee.com/xwintop/x-RdbmsSyncTool) 源码，实现JDBC不同数据库的链接。
其中在C317DataUI连接的MongoDB数据库中增加jdbc表,用于存储连接信息。

## 部署
1. 推荐IDEA打开项目，等jar包加载完毕即可启动, application.properties 配置数据库连接相关信息。接口文档地址：http://localhost:8400/api/jdbc/doc.html

2. 若发布项目，待项目启动成功，在生成target文件中，启动 C317DataSource-0.1.jar 即可部署；使用命令:
    ```
    java -jar C317DataSource-0.1.jar
    ```

