package org.example.ddduser.api.microservice;

import io.github.chensheng.dddboot.web.core.IgnoreResponseWrapper;
import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.application.service.UserMsQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/microservice/user")
public class UserMsController {
    @Autowired
    private UserMsQueryService userMsQueryService;

    @GetMapping("/{id}")
    @IgnoreResponseWrapper
    public UserProfile profile(@PathVariable("id") Long userId) {
        return userMsQueryService.findByUserId(userId);
    }
}
