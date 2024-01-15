package org.example.ddduser.application.service.impl;

import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.application.service.UserQueryService;
import org.example.ddduser.domain.repository.SecurityRepository;
import org.example.ddduser.domain.repository.UserRepository;
import org.example.ddduser.domain.user.UserEntity;
import org.example.ddduser.infrastructure.convertor.UserConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private UserConvertor userConvertor;

    @Override
    public UserProfile profile() {
        Long userId = securityRepository.findLoginUser();
        UserEntity userEntity = userRepository.find(userId);
        return userConvertor.toDto(userEntity);
    }
}
