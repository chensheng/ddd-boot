# ddd-boot-openfeign

* [概述](#概述)
* [引入依赖](#引入依赖)
* [最小配置](#最小配置)
* [GET请求（简单参数）](#get请求简单参数)
* [GET请求（复杂参数）](#get请求复杂参数)
* [POST请求（json请求体）](#post请求json请求体)
* [POST请求（简单form请求体）](#post请求简单form请求体)
* [POST请求（复杂form请求体）](#post请求复杂form请求体)
* [调用api](#调用api)
* [请求拦截器](#请求拦截器)
* [@FeignClient参数](#FeignClient参数)
* [配置信息](#配置信息)


## 概述
基于[OpenFeign](https://github.com/OpenFeign/feign)和[Apache Http Client](http://hc.apache.org/httpcomponents-client-4.5.x/index.html)，通过注解的方式来调用HTTP接口。

## 引入依赖

```xml
<dependency>
  <groupId>io.github.chensheng</groupId>
  <artifactId>ddd-boot-openfeign</artifactId>
</dependency>
```


## 最小配置

在application.yml中添加以下配置（定义要扫描的包）

```yml
openfeign:
  base-package: org.example.ddduser.infrastructure.repository.microservice
```


## GET请求（简单参数）

```java
@FeignClient(url = "http://localhost:8080")
public interface TestApi {
    @RequestLine("GET /api/simple?username={username}")
    TestResponse getSimple(@Param("username") String username);
}
```


## GET请求（复杂参数）

```java
@FeignClient(url = "http://localhost:8080")
public interface TestApi {
    @RequestLine("GET /api/complex")
    TestResponse getComplex(@QueryMap TestGetQuery query);
}
```


## POST请求（json请求体）

```java
@FeignClient(url = "http://localhost:8080")
public interface TestApi {
    @RequestLine("POST /api/json")
    @Headers("Content-Type: application/json")
    TestResponse postJson(TestPostBody body);
}
```


## POST请求（简单form请求体）

```java
@FeignClient(url = "http://localhost:8080")
public interface TestApi {
    @RequestLine("POST /api/simple_form")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TestResponse postSimpleForm(@Param("username") String username, @Param("age") Integer age);
}
```


## POST请求（复杂form请求体）

```java
@FeignClient(url = "http://localhost:7071", encoder = DefaultFormEncoder.class)
public interface TestFormApi {
    @RequestLine("POST /api/complex_form")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TestResponse postComplexForm(TestPostBody body);
}
```


## 调用api

```java
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestApi testApi;
 
    public TestResponse test() {
        return testApi.getSimple("hello");
    }
}
```

## 请求拦截器

* [定义拦截器](#定义拦截器)
* [使用拦截器](#使用拦截器)

#### 定义拦截器

```java
public class TokenInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("token", "123456");
    }
}
```

#### 使用拦截器

```java
@FeignClient(url = "http://localhost:8080", interceptors = {TokenInterceptor.class})
public interface TestSecurityApi {
    @RequestLine("GET /api/security?username={username}")
    ApiResponse<TestResponse> get(@Param("username") String username);
}
```


## @FeignClient参数

参数|说明|类型|默认值
-----|-----|-----|-----
url|必填，请求API的url|String
name|API的名称，必须唯一|String|接口的全限定名
encoder|API请求体的编码方式|Class|DefaultJacksonEncoder.class
decoder|API返回值的解码方式|Class|DefaultJacksonDecoder.class
interceptors|请求拦截器|Class[]|
retryer|重试机制|Class|
contract|注解解析策略|Class|


## 配置信息

参数|说明|类型|默认值
-----|-----|-----|-----
openfeign.basePackage|扫描该包下面有@FeignClient注解的接口|String|
openfeign.httpclient.maxConnTotal|最大连接数|Integer|200
openfeign.httpclient.maxConnPerRoute|单个线路最大连接数|Integer|50
openfeign.httpclient.tcpNoDelay|设置是否启用Nagle算法，设置true后禁用Nagle算法。Nagle算法试图通过减少分片的数量来节省带宽。当应用程序希望降低网络延迟并提高性能时，它们可以关闭Nagle算法，这样数据将会更早地发送，但是增加了网络消耗。|Boolean|true
openfeign.httpclient.socketTimeoutMillis|连接超过该时间无数据交互则被视为超时|Integer|30000
openfeign.httpclient.connectTimeoutMillis|尝试建立连接的超时时间|Integer|10000
openfeign.httpclient.connectionRequestTimeoutMillis|从连接池中获取连接的超时时间|Integer|5000