package org.example.ddduser.application.service;

import org.example.ddduser.application.dto.result.UserProfile;

public interface UserMsQueryService {
    UserProfile findByUserId(Long userId);
}
