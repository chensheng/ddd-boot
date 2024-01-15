#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.example.entity;

import io.github.chensheng.dddboot.microservice.core.DDDEntity;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import io.github.chensheng.dddboot.web.core.BizException;
import lombok.Builder;
import lombok.Getter;
import ${package}.domain.example.valueobject.ExampleStatus;

@Getter
@Builder
public class ExampleEntity implements DDDEntity {
    private Long id;

    private String username;

    private String password;

    private ExampleStatus status;

    /**
     * 新建用户
     * @param username 用户名；必传
     * @param password 密码；必传
     * @return
     */
    public static ExampleEntity create(String username, String password) {
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

        ExampleEntity user = builder()
                .username(username)
                .password(password)
                .status(ExampleStatus.ENABLE)
                .build();
        user.checkPasswordFormat();
        return user;
    }

    /**
     * 修改密码
     * @param oldPassword 原密码；必传
     * @param newPassword 新密码；必传
     */
    public void updatePassword(String oldPassword, String newPassword) {
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
        this.checkPasswordFormat();
    }

    /**
     * 启用用户
     */
    public void enable() {
        if(this.status == ExampleStatus.ENABLE) {
            throw new BizException("用户已是启用状态");
        }

        this.status = ExampleStatus.ENABLE;
    }

    /**
     * 停用用户
     */
    public void disable() {
        if(this.status == ExampleStatus.DISABLE) {
            throw new BizException("用户已是禁用状态");
        }

        this.status = ExampleStatus.DISABLE;
    }

    private void checkPasswordFormat() {
        String passwordFormat = "^(?![A-Za-z0-9]+${symbol_dollar})(?![a-z0-9${symbol_escape}${symbol_escape}W]+${symbol_dollar})(?![A-Za-z${symbol_escape}${symbol_escape}W]+${symbol_dollar})(?![A-Z0-9${symbol_escape}${symbol_escape}W]+${symbol_dollar})[a-zA-Z0-9${symbol_escape}${symbol_escape}W]{8,}${symbol_dollar}";
        if(TextUtil.isBlank(password) || !password.matches(passwordFormat)) {
            throw new BizException("密码必须由数字、字母、特殊字符_${symbol_pound}@!组成，且不能少于8位！");
        }
    }
}
