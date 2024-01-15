package org.example.dddworkspace.infrastructure.convertor;

import org.example.dddworkspace.application.dto.result.WorkspaceResult;
import org.example.dddworkspace.domain.workspace.WorkspaceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkspaceConvertor {
    WorkspaceResult toDto(WorkspaceEntity entity);
}
