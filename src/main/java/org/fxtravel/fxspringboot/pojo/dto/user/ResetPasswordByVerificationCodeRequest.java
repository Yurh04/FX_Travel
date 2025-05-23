package org.fxtravel.fxspringboot.pojo.dto.user;

import lombok.Data;

@Data
public class ResetPasswordByVerificationCodeRequest {
    private String email;
    private String code;
    private String password;
}
