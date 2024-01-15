package org.example.ddduser.infrastructure.repository.microservice;

import io.github.chensheng.dddboot.openfeign.annotation.FeignClient;
import io.github.chensheng.dddboot.web.core.Response;
import feign.Headers;
import feign.RequestLine;
import org.example.ddduser.infrastructure.repository.microservice.request.WorkspaceCreateRequest;
import org.example.ddduser.infrastructure.repository.microservice.response.Workspace;

@FeignClient(url = "${microservice.workspace.url}", interceptors = {WorkspaceSecurityInterceptor.class})
public interface WorkspaceMicroservice {
    @RequestLine("POST /microservice/workspace")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Response<Workspace> create(WorkspaceCreateRequest request);
}
