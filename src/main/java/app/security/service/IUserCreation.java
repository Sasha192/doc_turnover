package app.security.service;

import app.models.basic.CustomUser;
import app.security.models.UserDto;

public interface IUserCreation {
    CustomUser create(UserDto dto);
}
