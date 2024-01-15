package org.example.ddduser.application.service;

import org.example.ddduser.application.dto.command.ModifyAddressCommand;
import org.example.ddduser.application.dto.command.ModifyPasswordCommand;
import org.example.ddduser.application.dto.command.ModifyProfileCommand;
import org.example.ddduser.application.dto.command.UserRegisterCommand;

public interface UserCommandService {
    void register(UserRegisterCommand command);

    void modifyProfile(ModifyProfileCommand command);

    void modifyPassword(ModifyPasswordCommand command);

    void modifyAddress(ModifyAddressCommand command);
}
