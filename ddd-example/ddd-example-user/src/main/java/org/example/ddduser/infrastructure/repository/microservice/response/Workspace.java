package org.example.ddduser.infrastructure.repository.microservice.response;

import lombok.Data;

@Data
public class Workspace {
    private String name;

    private Long owner;
}
