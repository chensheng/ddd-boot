package org.example.ddduser.domain.user;

import io.github.chensheng.dddboot.web.core.BizException;
import org.example.ddduser.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDomainService {
    @Autowired
    private UserRepository userRepository;

    public void validateUsername(String username) {
        UserEntity existingUser = userRepository.find(username);
        if(existingUser != null) {
            throw new BizException("用户名已存在");
        }
    }

    public UserEntity validateUserId(Long userId) {
        if(userId == null) {
            throw new BizException("用户不存在");
        }

        UserEntity user = userRepository.find(userId);
        if(user == null) {
            throw new BizException("用户不存在");
        }

        return user;
    }
}
