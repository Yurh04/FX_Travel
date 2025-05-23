package org.fxtravel.fxspringboot.pojo.dto.user;

import lombok.Data;
import org.fxtravel.fxspringboot.common.Gender;

@Data
public class UpdateUserDataRequest {
    private String username;
    private Gender gender;
}
