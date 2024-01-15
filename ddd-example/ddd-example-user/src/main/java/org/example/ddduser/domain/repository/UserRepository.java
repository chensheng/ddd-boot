package org.example.ddduser.domain.repository;

import org.example.ddduser.domain.user.UserEntity;

public interface UserRepository {
    UserEntity find(Long userId);

    UserEntity find(String username);

    /**
     * Create or update user.
     * Create user when {@link UserEntity#getId()} is null, otherwise update user.
     * @param user
     * @return {@link UserEntity#getId()}
     */
    Long save(UserEntity user);
}
