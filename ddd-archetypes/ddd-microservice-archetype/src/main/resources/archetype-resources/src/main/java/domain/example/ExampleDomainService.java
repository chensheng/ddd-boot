#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.example;

import io.github.chensheng.dddboot.web.core.BizException;
import ${package}.domain.example.entity.ExampleEntity;
import ${package}.domain.example.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleDomainService {
    @Autowired
    private ExampleRepository exampleRepository;

    public void validateUsername(String username) {
        ExampleEntity existingUser = exampleRepository.getByUsername(username);
        if(existingUser != null) {
            throw new BizException("用户名已存在");
        }
    }
}
