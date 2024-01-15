package org.example.ddduser.domain.repository;

import org.example.ddduser.domain.user.valueobject.Address;

public interface LocationRepository {
    Address find(Double longitude, Double latitude);
}
