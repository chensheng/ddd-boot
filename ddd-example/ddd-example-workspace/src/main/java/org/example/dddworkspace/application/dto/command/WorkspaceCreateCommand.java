package org.example.dddworkspace.application.dto.command;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WorkspaceCreateCommand {
    @NotNull(message = "owner不能为空")
    private Long owner;
}
