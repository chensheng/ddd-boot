package org.example.ddduser.application.service.impl;

import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.application.service.UserMsQueryService;
import org.example.ddduser.domain.repository.UserRepository;
import org.example.ddduser.domain.user.UserEntity;
import org.example.ddduser.infrastructure.convertor.UserConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMsQueryServiceImpl implements UserMsQueryService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConvertor userConvertor;

    @Override
    public UserProfile findByUserId(Long userId) {
        UserEntity userEntity = userRepository.find(userId);
        return userConvertor.toDto(userEntity);
    }
}
