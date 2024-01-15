package org.example.ddduser.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.example.ddduser.domain.repository.UserRepository;
import org.example.ddduser.domain.user.UserEntity;
import org.example.ddduser.infrastructure.convertor.UserConvertor;
import org.example.ddduser.infrastructure.repository.database.UserDetailMapper;
import org.example.ddduser.infrastructure.repository.database.UserMapper;
import org.example.ddduser.infrastructure.repository.database.dataobject.User;
import org.example.ddduser.infrastructure.repository.database.dataobject.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private UserConvertor userConvertor;

    @Override
    public UserEntity find(Long userId) {
        if(userId == null) {
            return null;
        }

        User user = userMapper.selectById(userId);
        if(user == null) {
            return null;
        }

        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().lambda().eq(UserDetail::getUserId, userId));
        return userConvertor.toEntity(user, userDetail);
    }

    @Override
    public UserEntity find(String username) {
        if(TextUtil.isBlank(username)) {
            return null;
        }

        Wrapper<User> query = new QueryWrapper<User>().lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(query);
        if(user == null) {
            return null;
        }

        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().lambda().eq(UserDetail::getUserId, user.getId()));
        return userConvertor.toEntity(user, userDetail);
    }

    @Override
    public Long save(UserEntity userEntity) {
        if(userEntity.getId() != null) {
            User existingUser = userMapper.selectById(userEntity.getId());
            if(existingUser == null) {
                return null;
            }

            userConvertor.toUserPo(userEntity, existingUser);
            userMapper.updateById(existingUser);

            UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().lambda().eq(UserDetail::getUserId, userEntity.getId()));
            userConvertor.toDetailPo(userEntity, userDetail);
            userDetailMapper.updateById(userDetail);
            return userEntity.getId();
        }

        User newUser = userConvertor.toUserPo(userEntity);
        userMapper.insert(newUser);

        UserDetail userDetail = userConvertor.toDetailPo(userEntity, newUser.getId());
        userDetailMapper.insert(userDetail);
        return newUser.getId();
    }
}
