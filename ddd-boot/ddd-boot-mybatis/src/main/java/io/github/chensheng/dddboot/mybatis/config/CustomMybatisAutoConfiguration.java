package io.github.chensheng.dddboot.mybatis.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
@ConditionalOnClass(SqlSessionFactory.class)
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
public class CustomMybatisAutoConfiguration {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(Optional<List<InnerInterceptor>> extraInterceptors) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if(extraInterceptors.isPresent()) {
            for(InnerInterceptor innerInterceptor : extraInterceptors.get()) {
                interceptor.addInnerInterceptor(innerInterceptor);
            }
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
