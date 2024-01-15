package org.example.ddduser.api.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.application.dto.query.UserProfilePageQuery;
import org.example.ddduser.application.service.UserMngCommandService;
import org.example.ddduser.application.service.UserMngQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web/mng/user")
public class UserMngController {
    @Autowired
    private UserMngQueryService userMngQueryService;

    @Autowired
    private UserMngCommandService userMngCommandService;

    @GetMapping("/page")
    public Page<UserProfile> page(UserProfilePageQuery query) {
        return userMngQueryService.profilePage(query);
    }

    @PutMapping("/enabled/{id}")
    public void enable(@PathVariable Long id) {
        userMngCommandService.enable(id);
    }

    @PutMapping("/disabled/{id}")
    public void disable(@PathVariable Long id) {
        userMngCommandService.disable(id);
    }
}
