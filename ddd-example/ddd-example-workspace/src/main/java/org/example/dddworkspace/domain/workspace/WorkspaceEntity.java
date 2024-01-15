package org.example.dddworkspace.domain.workspace;

import io.github.chensheng.dddboot.web.core.BizException;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class WorkspaceEntity {
    private String name;

    private Long owner;

    public static WorkspaceEntity create(Long owner) {
        if(owner == null || owner <= 0) {
            throw new BizException("owner不能为空");
        }

        String workspaceName = String.format("workspace_%s", UUID.randomUUID().toString());
        WorkspaceEntity workspace = WorkspaceEntity.builder()
                .name(workspaceName)
                .owner(owner)
                .build();
        return workspace;
    }
}
