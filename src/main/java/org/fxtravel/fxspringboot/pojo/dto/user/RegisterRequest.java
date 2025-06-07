package org.fxtravel.fxspringboot.pojo.dto.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.fxtravel.fxspringboot.common.Gender;
import org.fxtravel.fxspringboot.common.Role;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String username;
    private Gender gender;
    private Role role;
}
