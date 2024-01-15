package org.example.application.example.service.impl;

import org.example.application.example.dto.command.ExampleCreateCommand;
import org.example.application.example.dto.command.ExampleUpdatePasswordCommand;
import org.example.application.example.service.ExampleCommandService;
import org.example.domain.example.ExampleDomainService;
import org.example.domain.example.entity.ExampleEntity;
import org.example.domain.example.repository.ExampleRepository;
import org.example.domain.example.repository.ExampleSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExampleCommandServiceImpl implements ExampleCommandService {
    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private ExampleSecurityRepository securityRepository;

    @Autowired
    private ExampleDomainService exampleDomainService;

    @Override
    public void create(ExampleCreateCommand command) {
        exampleDomainService.validateUsername(command.getUsername());
        ExampleEntity entity = ExampleEntity.create(command.getUsername(), command.getPassword());
        exampleRepository.save(entity);
    }

    @Override
    public void update(ExampleUpdatePasswordCommand command) {
        Long userId = securityRepository.getLoginUser();
        ExampleEntity entity = exampleRepository.getById(userId);
        entity.updatePassword(command.getOldPassword(), command.getNewPassword());
        exampleRepository.save(entity);
    }

    @Override
    public void enable(Long id) {
        ExampleEntity entity = exampleRepository.getById(id);
        entity.enable();
        exampleRepository.save(entity);
    }

    @Override
    public void disable(Long id) {
        ExampleEntity entity = exampleRepository.getById(id);
        entity.disable();
        exampleRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        ExampleEntity entity = exampleRepository.getById(id);
        exampleRepository.remove(entity);
    }
}
