package me.superkoh.kframework.mvc.security;

import org.hibernate.validator.constraints.NotBlank;

public interface LoginUserService {
    LoginUser getUserByToken(@NotBlank String token);
}
