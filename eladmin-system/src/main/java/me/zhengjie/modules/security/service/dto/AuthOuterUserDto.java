package me.zhengjie.modules.security.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthOuterUserDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
