package org.example.ddduser.infrastructure.convertor;

import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.domain.user.UserEntity;
import org.example.ddduser.infrastructure.repository.database.dataobject.User;
import org.example.ddduser.infrastructure.repository.database.dataobject.UserDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserConvertor {
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "address.country", source="detail.country")
    @Mapping(target = "address.province", source="detail.province")
    @Mapping(target = "address.city", source="detail.city")
    @Mapping(target = "address.county", source="detail.county")
    @Mapping(target = "address.detail", source = "detail.detailAddress")
    UserEntity toEntity(User user, UserDetail detail);

    User toUserPo(UserEntity entity);

    void toUserPo(UserEntity entity, @MappingTarget User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "country", source = "entity.address.country")
    @Mapping(target = "province", source = "entity.address.province")
    @Mapping(target = "city", source = "entity.address.city")
    @Mapping(target = "county", source = "entity.address.county")
    @Mapping(target = "detailAddress", source = "entity.address.detail")
    UserDetail toDetailPo(UserEntity entity, Long userId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "province", source = "address.province")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "county", source = "address.county")
    @Mapping(target = "detailAddress", source = "address.detail")
    void toDetailPo(UserEntity entity, @MappingTarget UserDetail detail);

    @Mapping(target = "address", expression = "java(entity.getAddress().getFullAddress())")
    UserProfile toDto(UserEntity entity);
}
