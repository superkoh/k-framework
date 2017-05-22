package me.superkoh.kframework.mvc.security;

import me.superkoh.kframework.core.security.subject.LoginUser;
import org.hibernate.validator.constraints.NotBlank;

public interface LoginUserService {
    LoginUser getUserByToken(@NotBlank String token);
}
