# ddd-boot-nacos-config

* [概述](#概述)
* [引入依赖](#引入依赖)
* [配置](#配置)


## 概述
改造[Nacos配置中心SDK](https://github.com/nacos-group/nacos-spring-boot-project) ，以支持SpringBoot2.7。

## 引入依赖
```xml
<dependency>
  <groupId>io.github.chensheng</groupId>
  <artifactId>ddd-boot-nacos-config</artifactId>
</dependency>
```

## 配置
```yaml
nacos:
  config:
    type: yaml
    server-addr: ${NACOS_URL:127.0.0.1:8848}
    namespace: ${NACOS_NAMESPACE:dev}
    data-id: ddd-example
    auto-refresh: true
    group: ${NACOS_GROUP:DEFAULT_GROUP}
    username: ${NACOS_USERNAME:nacos}
    password: ${NACOS_PASSWORD:xxxxxx}
    bootstrap:
      enable: true
      log-enable: true
```