package org.example.domain.example.repository;

import org.example.domain.example.valueobject.ExampleAddress;

public interface ExampleLocationRepository {
    ExampleAddress find(Double longitude, Double latitude);
}
