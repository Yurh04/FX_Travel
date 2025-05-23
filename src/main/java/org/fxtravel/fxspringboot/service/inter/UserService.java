package org.fxtravel.fxspringboot.service.inter;

import org.fxtravel.fxspringboot.common.Gender;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserService {
    /**
     * 根据邮箱和密码注册用户账号
     * @param email 用户邮箱
     * @param password 用户密码
     * @return 是否注册成功
     */
    public boolean register(String email, String password);

    public boolean verifyCode(String code);

    public User login(String email, String password);

    public boolean forgotPassword(String email);

    public boolean resetPasswordByVerificationCode(String email,String code, String password);

    public boolean resetPasswordByOldPassword(String email, String oldPassword, String newPassword);

    public boolean updateUserData(User user, String userName, Gender gender);

    public boolean grantAdminRole(Integer userId);
}
