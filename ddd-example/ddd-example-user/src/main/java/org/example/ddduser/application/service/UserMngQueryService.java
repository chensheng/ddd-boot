package org.example.ddduser.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.application.dto.query.UserProfilePageQuery;

public interface UserMngQueryService {
    Page<UserProfile> profilePage(UserProfilePageQuery query);
}
