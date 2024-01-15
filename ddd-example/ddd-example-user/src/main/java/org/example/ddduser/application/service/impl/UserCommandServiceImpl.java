package org.example.ddduser.application.service.impl;

import org.example.ddduser.application.dto.command.ModifyAddressCommand;
import org.example.ddduser.application.dto.command.ModifyPasswordCommand;
import org.example.ddduser.application.dto.command.ModifyProfileCommand;
import org.example.ddduser.application.dto.command.UserRegisterCommand;
import org.example.ddduser.application.service.UserCommandService;
import org.example.ddduser.domain.repository.LocationRepository;
import org.example.ddduser.domain.repository.SecurityRepository;
import org.example.ddduser.domain.repository.UserRepository;
import org.example.ddduser.domain.repository.WorkspaceRepository;
import org.example.ddduser.domain.user.UserDomainService;
import org.example.ddduser.domain.user.UserEntity;
import org.example.ddduser.domain.user.valueobject.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCommandServiceImpl implements UserCommandService {
    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Override
    public void register(UserRegisterCommand command) {
        userDomainService.validateUsername(command.getUsername());
        UserEntity user = UserEntity.create(command.getUsername(), command.getPassword());
        Long userId = userRepository.save(user);
        workspaceRepository.create(userId);
    }

    @Override
    public void modifyProfile(ModifyProfileCommand command) {
        Long userId = securityRepository.findLoginUser();
        UserEntity user = userRepository.find(userId);
        user.modifyProfile(command.getNickName(), command.getAvatar(), command.getGender(), command.getAge());
        userRepository.save(user);
    }

    @Override
    public void modifyPassword(ModifyPasswordCommand command) {
        Long userId = securityRepository.findLoginUser();
        UserEntity user = userRepository.find(userId);
        user.modifyPassword(command.getOldPassword(), command.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public void modifyAddress(ModifyAddressCommand command) {
        Long userId = securityRepository.findLoginUser();
        UserEntity user = userRepository.find(userId);
        Address address = locationRepository.find(command.getLongitude(), command.getLatitude());
        user.modifyAddress(address);
        userRepository.save(user);
    }
}
