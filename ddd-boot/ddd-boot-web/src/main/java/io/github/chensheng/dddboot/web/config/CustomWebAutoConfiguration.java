package io.github.chensheng.dddboot.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.github.chensheng.dddboot.web.core.CustomResponseBodyAdvice;
import io.github.chensheng.dddboot.web.core.CustomWebExceptionHandler;
import io.github.chensheng.dddboot.web.core.ResponseBodyDecorateCenter;
import io.github.chensheng.dddboot.web.core.SmartStringToDateConverter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Configuration
@ConditionalOnClass({DispatcherServlet.class})
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class CustomWebAutoConfiguration {

    @Bean
    public CustomResponseBodyAdvice customResponseBodyAdvice(ResponseBodyDecorateCenter responseBodyDecorateCenter) {
        return new CustomResponseBodyAdvice(responseBodyDecorateCenter);
    }

    @Bean
    public CustomWebExceptionHandler customWebExceptionHandler() {
        return new CustomWebExceptionHandler();
    }

    @Bean
    public SmartStringToDateConverter smartStringToDateConverter() {
        return new SmartStringToDateConverter();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonConverter() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .json()
                .failOnUnknownProperties(false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializerByType(Long.class, ToStringSerializer.instance)
                .timeZone(TimeZone.getTimeZone("GMT+8"))
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return new MappingJackson2HttpMessageConverter(builder.build());
    }

    @Bean
    public MappingJackson2XmlHttpMessageConverter xmlConverter() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .xml()
                .failOnUnknownProperties(false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializerByType(Long.class, ToStringSerializer.instance)
                .timeZone(TimeZone.getTimeZone("GMT+8"))
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build());
    }

    @Bean
    public StringHttpMessageConverter stringConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Bean
    public ResponseBodyDecorateCenter responseBodyDecorateCenter(ApplicationContext applicationContext) {
        return new ResponseBodyDecorateCenter(applicationContext);
    }
}
