package org.example.dddworkspace.api.microservice;

import org.example.dddworkspace.application.dto.command.WorkspaceCreateCommand;
import org.example.dddworkspace.application.dto.result.WorkspaceResult;
import org.example.dddworkspace.application.service.WorkspaceMsCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/microservice/workspace")
public class WorkspaceMsController {
    @Autowired
    private WorkspaceMsCommandService workspaceMsCommandService;

    @PostMapping
    public WorkspaceResult create(@Valid @RequestBody WorkspaceCreateCommand command) {
        return workspaceMsCommandService.create(command);
    }
}
