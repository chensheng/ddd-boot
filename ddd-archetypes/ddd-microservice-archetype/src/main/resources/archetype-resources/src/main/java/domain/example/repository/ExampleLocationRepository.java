#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.example.repository;

import ${package}.domain.example.valueobject.ExampleAddress;

public interface ExampleLocationRepository {
    ExampleAddress find(Double longitude, Double latitude);
}
