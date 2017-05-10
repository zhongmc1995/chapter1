# 架构探险从零开始写javaweb框架demo1
### 1.IEDA创建一个标准的Maven项目
### 2.调整目录结构，转为java web项目
### 3.添加Maven依赖，包括添加plugin
### 4.IDEA中配置Tomcat，其中一种添加Maven插件，另一种为内嵌本地Tomcat
### 5.将代码托管到github中，编写.gitignore文件来出去一些不需要控制的文件
### 6.虽然DataBaseHelper封装的相当精简，但是每次调用一次数据库操作都要getConnection(),这样会导致巨大的开销，毕竟数据库的连接次数有限，因此可以使用一种池化的解决方案，这里运用第三方框架DBCP数据库连接池