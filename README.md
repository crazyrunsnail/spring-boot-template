一个简单的spring-boot项目脚手架

SpringBoot很方便，但不能开箱即用，现在开源的脚手架又过于复杂，于是整理了这个简单的脚手脚。

## 特性
- 简单
- 开箱即用
- 登录和鉴权
- 代码生成
- 接口文档
- 参数校验

## 使用到库
- SpringBoot
- SpringSecurity
- Mybatis
- Mybatis Page Helper
- Apache common lang3
- Jwtt

## 如何开始
1. 拷贝项目到本地，导入IDE，在 `pom.xml` 修改项目的 groupId、artifactId
    ```shell
    git clone git@github.com:crazyrunsnail/spring-boot-template.git my-project-name
    ```
2. 在数据库执行 `src/main/resources/db/migration/V1__init.sql` 里的sql语句
3. （可选）修改项目路径，在IDE中使用 rename 功能
4. 开发业务

## 参考的工作流
1. 数据库建模，生在SQL语句，使用 `mybatis-generator` 的 maven 插件生成 Model, Mapper接口和Mapper.xml。
   1. 将Model中的getter和setter删除使用Lombok `@Data`
   2. 在Mapper接口上加上 `@Mapper`
2. 新增对应 Service 的接口实现，常规会有四个方法和实现，分别是：
   - `search` 
   - `getById` 
   - `create` 
   - `update`
   
   相对应的是三个入参DTO，分别是：
   - `ModelSearchParam` 
   - `ModelCreateParam` 
   - `ModelUpdateParam`
   
   对应一个出参DTO：
   - `ModelDTO`
3. 新增对应的 Controller 调用对应 Service 并对结果进行包装和文档化

## 为什么这么做
[为什么这么做](WHY.md)

**Have fun coding!**
