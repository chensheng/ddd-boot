package org.example.ddduser.domain.repository;

public interface SecurityRepository {
    Long findLoginUser();

    Long findLoginUserQuietly();
}
