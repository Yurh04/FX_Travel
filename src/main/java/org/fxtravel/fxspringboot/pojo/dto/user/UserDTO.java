package org.fxtravel.fxspringboot.pojo.dto.user;

import jakarta.annotation.Nullable;
import org.fxtravel.fxspringboot.common.Gender;
import org.fxtravel.fxspringboot.common.Role;

public class UserDTO {
    @Nullable
    private int id;
    private String email;
    private String password;
    private boolean verified;
    private String username;
    private Gender gender;
    private Role role;
}
