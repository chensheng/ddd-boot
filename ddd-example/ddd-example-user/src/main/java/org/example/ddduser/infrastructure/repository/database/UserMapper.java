package org.example.ddduser.infrastructure.repository.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.infrastructure.repository.database.condition.UserProfilePageCondition;
import org.example.ddduser.infrastructure.repository.database.dataobject.User;

public interface UserMapper extends BaseMapper<User> {
    Page<UserProfile> selectProfilePage(UserProfilePageCondition condition);
}
