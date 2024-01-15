package org.example.ddduser.domain.user;

import io.github.chensheng.dddboot.tools.text.TextUtil;
import io.github.chensheng.dddboot.web.core.BizException;
import lombok.Builder;
import lombok.Getter;
import org.example.ddduser.domain.user.valueobject.Address;
import org.example.ddduser.domain.user.valueobject.Gender;
import org.example.ddduser.domain.user.valueobject.UserStatus;

@Getter
@Builder
public class UserEntity {
    private Long id;

    private String username;

    private String password;

    private UserStatus status;

    private String nickName;

    private String avatar;

    private Gender gender = Gender.UNKNOWN;

    private Integer age;

    private Address address = Address.builder().build();

    public static UserEntity create(String username, String password) {
        if(TextUtil.isBlank(username)) {
            throw new BizException("用户名不能为空");
        }
        if(TextUtil.isCNWord(username)) {
            throw new BizException("用户名不能包含中文");
        }
        if(TextUtil.isBlank(password)) {
            throw new BizException("密码不能为空");
        }
        if(password.length() < 6) {
            throw new BizException("密码不能少于6位数");
        }

        UserEntity user = builder().build();
        user.username = username;
        user.password = password;
        user.status = UserStatus.ENABLE;
        return user;
    }

    public void modifyPassword(String oldPassword, String newPassword) {
        if(TextUtil.isBlank(oldPassword)) {
            throw new BizException("原密码不能为空");
        }
        if(TextUtil.isBlank(newPassword)) {
            throw new BizException("新密码不能为空");
        }
        if(newPassword.length() < 6) {
            throw new BizException("密码不能少于6位数");
        }
        if(!this.password.equals(oldPassword)) {
            throw new BizException("原密码错误");
        }
        if(oldPassword.equals(newPassword)) {
            throw new BizException("新密码不能与旧密码相同");
        }

        this.password = newPassword;
    }

    public void modifyProfile(String nickName, String avatar,
                              Gender gender, Integer age) {
        this.nickName = nickName;
        this.avatar = avatar;
        this.age = age;

        if(gender != null) {
            this.gender = gender;
        } else {
            this.gender = Gender.UNKNOWN;
        }
    }

    public void modifyAddress(Address address) {
        this.address = address;
    }

    public void enable() {
        if(this.status == UserStatus.ENABLE) {
            throw new BizException("用户已是启用状态");
        }

        this.status = UserStatus.ENABLE;
    }

    public void disable() {
        if(this.status == UserStatus.DISABLE) {
            throw new BizException("用户已是禁用状态");
        }

        this.status = UserStatus.DISABLE;
    }
}
