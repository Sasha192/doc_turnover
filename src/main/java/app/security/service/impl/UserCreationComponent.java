package app.security.service.impl;

import app.customtenant.service.interfaces.IPerformerService;
import app.customtenant.service.interfaces.IPerformerStatisticCreation;
import app.security.models.UserDto;
import app.security.models.auth.CustomUser;
import app.security.service.IUserCreation;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCreationComponent implements IUserCreation {

    private final IUserService userService;

    private final IPerformerService performerService;

    private final DefaultPasswordEncoder encoder;

    private final IPerformerStatisticCreation statisticCreation;

    @Autowired
    public UserCreationComponent(IUserService userService,
                                 IPerformerService performerService,
                                 DefaultPasswordEncoder encoder,
                                 IPerformerStatisticCreation statisticCreation) {
        this.userService = userService;
        this.performerService = performerService;
        this.encoder = encoder;
        this.statisticCreation = statisticCreation;
    }

    @Override
    public CustomUser create(UserDto dto) {
        CustomUser customUser = new CustomUser(dto, encoder);
        userService.create(customUser);
        return customUser;
    }
}
