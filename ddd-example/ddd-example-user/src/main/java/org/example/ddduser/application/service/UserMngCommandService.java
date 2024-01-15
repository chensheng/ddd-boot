package org.example.ddduser.application.service;

public interface UserMngCommandService {
    void enable(Long userId);

    void disable(Long userId);
}
