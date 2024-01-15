package org.example.domain.example.repository;

import io.github.chensheng.dddboot.microservice.core.DDDRepository;
import org.example.domain.example.entity.ExampleEntity;

public interface ExampleRepository extends DDDRepository<ExampleEntity> {
    ExampleEntity getByUsername(String username);
}
