package org.fxtravel.fxspringboot.pojo.dto.user;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
