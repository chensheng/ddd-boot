package org.example.ddduser.infrastructure.repository.database.dataobject;

import io.github.chensheng.dddboot.microservice.core.DataObject;
import lombok.Data;
import org.example.ddduser.domain.user.valueobject.Gender;

@Data
public class UserDetail extends DataObject {
    private Long userId;

    private String nickName;

    private String avatar;

    private Gender gender;

    private Integer age;

    private String country;

    private String province;

    private String city;

    private String county;

    private String detailAddress;
}
