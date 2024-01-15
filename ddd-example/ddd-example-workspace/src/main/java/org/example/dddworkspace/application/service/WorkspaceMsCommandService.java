package org.example.dddworkspace.application.service;

import org.example.dddworkspace.application.dto.command.WorkspaceCreateCommand;
import org.example.dddworkspace.application.dto.result.WorkspaceResult;

public interface WorkspaceMsCommandService {
    WorkspaceResult create(WorkspaceCreateCommand command);
}
