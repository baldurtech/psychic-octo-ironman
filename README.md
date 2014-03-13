# 说明
一个简单的 jdbc web 应用

# 脚本
## clean
用于清空编译结果

## compile
编译源文件

## deploy
部署到 Tomcat

*注意： 需要事先配置好系统环境变量 CATALINA_HOME*

# 使用

## 初始化数据库
导入数据库脚本 sql/mysql.sql

## 构建、部署
`./clean && ./compile && ./deploy`

或者同时指定 Tomcat 的路径

`./clean && ./compile && CATALINA_HOME=~/apache-tomcat-7.0.50 ./deploy`

