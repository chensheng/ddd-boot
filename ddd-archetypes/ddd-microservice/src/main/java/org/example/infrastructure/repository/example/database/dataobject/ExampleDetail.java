package org.example.infrastructure.repository.example.database.dataobject;

import io.github.chensheng.dddboot.microservice.core.DataObject;
import lombok.Data;

@Data
public class ExampleDetail extends DataObject {
    private Long userId;

    private String country;

    private String province;

    private String city;

    private String county;

    private String detailAddress;
}
