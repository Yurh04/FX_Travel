package org.fxtravel.fxspringboot.pojo.dto.user;

import lombok.Data;

@Data
public class ResetPasswordByOldPasswordRequest {
    String email;
    String oldPassword;
    String newPassword;
}
