#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure.repository.example;

import ${package}.domain.example.valueobject.ExampleAddress;
import ${package}.domain.example.repository.ExampleLocationRepository;
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
