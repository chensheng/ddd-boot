# ddd-archetypes
DDD项目脚手架

## 模块

### ddd-microservice
脚手架模板（基于该模板生成脚手架源码）

### ddd-microservice-archetype
脚手架源码


## 安装脚手架

### 生成源码
```shell
cd ddd-microservice
mvn archetype:create-from-project
```

### 复制源码
```shell
rm -rf ddd-microservice-archetype/src
mv ddd-microservice/target/generated-sources/archetype/src ddd-microservice-archetype/src
```

### 安装脚手架
```shell
cd ddd-microservice-archetype
mvn clean install
```
