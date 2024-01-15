package org.example.ddduser.infrastructure.repository.database.dataobject;

import io.github.chensheng.dddboot.microservice.core.DataObject;
import lombok.Data;
import org.example.ddduser.domain.user.valueobject.UserStatus;

@Data
public class User extends DataObject {
    private String username;

    private String password;

    private UserStatus status;
}
