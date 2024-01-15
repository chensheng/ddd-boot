# ddd-boot-web

* [概述](#概述)
* [引入依赖](#引入依赖)
* [统一返回格式](#统一返回格式)
* [业务状态码](#业务状态码)
* [请求参数校验](#请求参数校验)
* [业务异常处理](#业务异常处理)
* [高级用法](#高级用法)

## 概述
基于[spring MVC](https://docs.spring.io/spring-framework/docs/5.3.27/reference/html/web.html) ，封装了统一返回格式、接口参数校验、系统异常处理、业务异常处理等功能。

## 引入依赖

```xml
<dependency>
  <groupId>io.github.chensheng</groupId>
  <artifactId>ddd-boot-web</artifactId>
</dependency>
```

## 统一返回格式
```json
{
  "code": "BIZ_SUCCESS",
  "msg": "BIZ_SUCCESS",
  "data": null
}
```

字段|说明
-----|-----
code | 业务状态码。除了使用框架定义的业务码，开发者也可以自定义业务状态码。
msg | 提示信息
data | 数据，任意类型。

## 业务状态码
code | 说明
-----|-----
BIZ_SUCCESS | 成功
BIZ_ERROR | 失败
SYS_ERROR | 系统异常

## 请求参数校验
使用[Hibernate Validator](https://hibernate.org/validator/releases/6.0/) 对请求参数进行校验。

```java
@Data
public class ExampleRegisterCommand {
    @NotEmpty(message = "请输入用户名")
    private String username;

    @NotEmpty(message = "请输入密码")
    @Pattern(regexp = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$", message = "密码必须由数字、字母、特殊字符_#@!组成，且不能少于8位！")
    private String password;
}
```

```java
@RestController
@RequestMapping("/web/example")
public class ExampleController {
    @Autowired
    private ExampleCommandService exampleCommandService;

    @PostMapping
    public void register(@Valid @RequestBody ExampleRegisterCommand command) {
        exampleCommandService.register(command);
    }
}
```

## 业务异常处理
抛出`BizException`业务异常，由框架统一处理。

```java
@Component
public class ExampleDomainService {
    @Autowired
    private ExampleRepository userRepository;

    public void validateUsername(String username) {
        ExampleEntity existingUser = userRepository.find(username);
        if(existingUser != null) {
            throw new BizException("用户名已存在");
        }
    }
}
```

## 高级用法

### ResponseBodyDecorator
开发者可实现该接口，根据实际应用场景修改接口返回值。
```java
@Component
public class CustomResponseBodyDecorator implements ResponseBodyDecorator {
    public Object decorate(Object body) {
        
    }
}
```

### @IgnoreResponseWrapper
开发者可通过将@IgnoreResponseWrapper注解添加在Controller方法上，来跳过对接口返回结果的包装，直接返回原始数据。
```java
@RestController
@RequestMapping("/microservice/user")
public class UserMsController {
    @Autowired
    private UserMsQueryService userMsQueryService;

    @GetMapping("/{id}")
    @IgnoreResponseWrapper
    public UserProfile profile(@PathVariable("id") Long userId) {
        return userMsQueryService.findByUserId(userId);
    }
}
```
