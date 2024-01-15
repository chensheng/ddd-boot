package org.example.ddduser.application.service.impl;

import org.example.ddduser.application.service.UserMngCommandService;
import org.example.ddduser.domain.repository.UserRepository;
import org.example.ddduser.domain.user.UserEntity;
import org.example.ddduser.domain.user.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserMngCommandServiceImpl implements UserMngCommandService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDomainService userDomainService;

    @Override
    public void enable(Long userId) {
        UserEntity user = userDomainService.validateUserId(userId);
        user.enable();
        userRepository.save(user);
    }

    @Override
    public void disable(Long userId) {
        UserEntity user = userDomainService.validateUserId(userId);
        user.disable();
        userRepository.save(user);
    }
}
