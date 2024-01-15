package org.example.dddworkspace.application.service.impl;

import org.example.dddworkspace.application.dto.command.WorkspaceCreateCommand;
import org.example.dddworkspace.application.dto.result.WorkspaceResult;
import org.example.dddworkspace.application.service.WorkspaceMsCommandService;
import org.example.dddworkspace.domain.workspace.WorkspaceEntity;
import org.example.dddworkspace.infrastructure.convertor.WorkspaceConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorkspaceMsCommandServiceImpl implements WorkspaceMsCommandService {
    @Autowired
    private WorkspaceConvertor workspaceConvertor;

    @Override
    public WorkspaceResult create(WorkspaceCreateCommand command) {
        WorkspaceEntity workspace = WorkspaceEntity.create(command.getOwner());
        return workspaceConvertor.toDto(workspace);
    }
}
