package org.example.ddduser.infrastructure.repository;

import org.example.ddduser.domain.repository.LocationRepository;
import org.example.ddduser.domain.user.valueobject.Address;
import org.springframework.stereotype.Component;

@Component
public class LocationRepositoryImpl implements LocationRepository {
    @Override
    public Address find(Double longitude, Double latitude) {
        //dummy
        Address address = Address.builder()
                .country("中国")
                .province("广东省")
                .city("惠州市")
                .county("惠城区")
                .detail(String.format("仲恺高新区惠环街道惠风三路TCL科技大厦(%s,%s)", longitude, latitude))
                .build();
        return address;
    }
}
