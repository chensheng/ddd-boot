package org.example.application.example.service;

import org.example.application.example.dto.command.ExampleUpdatePasswordCommand;
import org.example.application.example.dto.command.ExampleCreateCommand;

public interface ExampleCommandService {
    void create(ExampleCreateCommand command);

    void update(ExampleUpdatePasswordCommand command);

    void enable(Long id);

    void disable(Long id);

    void delete(Long id);
}
