package org.fxtravel.fxspringboot.service.inter.common;

import org.fxtravel.fxspringboot.common.Gender;
import org.fxtravel.fxspringboot.pojo.dto.user.RegisterRequest;
import org.fxtravel.fxspringboot.pojo.entities.User;

public interface UserService {
    /**
     * 根据邮箱和密码注册用户账号
     */
    public User register(RegisterRequest request);

    public boolean verifyCode(String code);

    public User login(String email, String password);

    public boolean forgotPassword(String email);

    public boolean resetPasswordByVerificationCode(String email,String code, String password);

    public boolean resetPasswordByOldPassword(String email, String oldPassword, String newPassword);

    public boolean updateUserData(User user, String userName, Gender gender);

    public boolean grantAdminRole(Integer userId);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户实体
     */


}




