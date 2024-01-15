package org.example.ddduser.api.web;

import org.example.ddduser.application.dto.command.ModifyAddressCommand;
import org.example.ddduser.application.dto.command.ModifyPasswordCommand;
import org.example.ddduser.application.dto.command.ModifyProfileCommand;
import org.example.ddduser.application.dto.command.UserRegisterCommand;
import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.application.service.UserCommandService;
import org.example.ddduser.application.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/web/user")
public class UserController {
    @Autowired
    private UserCommandService userCommandService;

    @Autowired
    private UserQueryService userQueryService;

    @PostMapping
    public void register(@Valid @RequestBody UserRegisterCommand command) {
        userCommandService.register(command);
    }

    @PutMapping("/profile")
    public void modifyProfile(@Valid @RequestBody ModifyProfileCommand command) {
        userCommandService.modifyProfile(command);
    }

    @PutMapping("/password")
    public void modifyPassword(@Valid @RequestBody ModifyPasswordCommand command) {
        userCommandService.modifyPassword(command);
    }

    @PutMapping("/address")
    public void modifyAddress(@Valid @RequestBody ModifyAddressCommand command) {
        userCommandService.modifyAddress(command);
    }

    @GetMapping
    public UserProfile profile() {
        return userQueryService.profile();
    }
}
