package org.example.infrastructure.repository.example.database.dataobject;

import io.github.chensheng.dddboot.microservice.core.DataObject;
import lombok.Data;
import org.example.domain.example.valueobject.ExampleStatus;

@Data
public class Example extends DataObject {
    private String username;

    private String password;

    private ExampleStatus status;
}
