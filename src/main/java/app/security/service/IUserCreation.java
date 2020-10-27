package app.security.service;

import app.security.models.UserDto;
import app.security.models.auth.CustomUser;

public interface IUserCreation {
    CustomUser create(UserDto dto);

    CustomUser create(CustomUser user);
}
