package org.example.infrastructure.repository.example;

import org.example.domain.example.valueobject.ExampleAddress;
import org.example.domain.example.repository.ExampleLocationRepository;
import org.springframework.stereotype.Component;

@Component
public class ExampleLocationRepositoryImpl implements ExampleLocationRepository {
    @Override
    public ExampleAddress find(Double longitude, Double latitude) {
        //dummy
        ExampleAddress address = ExampleAddress.builder()
                .country("中国")
                .province("广东省")
                .city("惠州市")
                .county("惠城区")
                .detail(String.format("仲恺高新区惠环街道惠风三路xxx大厦6楼(%s,%s)", longitude, latitude))
                .build();
        return address;
    }
}
