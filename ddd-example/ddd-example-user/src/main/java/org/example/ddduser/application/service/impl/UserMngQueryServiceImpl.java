package org.example.ddduser.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.chensheng.dddboot.tools.base.BeanUtil;
import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.application.dto.query.UserProfilePageQuery;
import org.example.ddduser.application.service.UserMngQueryService;
import org.example.ddduser.infrastructure.repository.database.UserMapper;
import org.example.ddduser.infrastructure.repository.database.condition.UserProfilePageCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMngQueryServiceImpl implements UserMngQueryService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<UserProfile> profilePage(UserProfilePageQuery query) {
        UserProfilePageCondition condition = new UserProfilePageCondition();
        BeanUtil.copyNotBlankProperties(query, condition);
        return userMapper.selectProfilePage(condition);
    }
}
