package org.example.ddduser.infrastructure.repository;

import io.github.chensheng.dddboot.web.core.BizException;
import io.github.chensheng.dddboot.web.core.Response;
import org.example.ddduser.domain.repository.WorkspaceRepository;
import org.example.ddduser.infrastructure.repository.microservice.WorkspaceMicroservice;
import org.example.ddduser.infrastructure.repository.microservice.request.WorkspaceCreateRequest;
import org.example.ddduser.infrastructure.repository.microservice.response.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceRepositoryImpl implements WorkspaceRepository {
    @Autowired
    private WorkspaceMicroservice workspaceFacade;

    @Override
    public void create(Long userId) {
        WorkspaceCreateRequest request = new WorkspaceCreateRequest();
        request.setOwner(userId);
        Response<Workspace> response = workspaceFacade.create(request);
        if(response == null || !response.isSuccess()) {
            if(response != null) {
                throw new BizException(response.getCode(), response.getMsg());
            } else {
                throw new BizException();
            }
        }
    }
}
