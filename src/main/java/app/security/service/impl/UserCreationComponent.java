package app.security.service.impl;

import app.configuration.spring.constants.Constants;
import app.models.basic.CustomUser;
import app.models.basic.Performer;
import app.security.models.UserDto;
import app.security.service.IUserCreation;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.service.interfaces.IPerformerService;
import app.utils.ImgToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCreationComponent implements IUserCreation {

    private final IUserService userService;

    private final IPerformerService performerService;

    private final DefaultPasswordEncoder encoder;

    @Autowired
    public UserCreationComponent(IUserService userService,
                                 IPerformerService performerService,
                                 DefaultPasswordEncoder encoder) {
        this.userService = userService;
        this.performerService = performerService;
        this.encoder = encoder;
    }

    @Override
    public CustomUser create(UserDto dto) {
        CustomUser customUser = new CustomUser(dto, encoder);
        customUser.setEnabled(true);
        userService.create(customUser);
        Performer performer = new Performer();
        String performerName =
                (dto.getFirstName() == null
                        ? Constants.EMPTY_STRING :
                        dto.getFirstName())
                        + " "
                        + (dto.getLastName() == null
                        ? Constants.EMPTY_STRING :
                        dto.getLastName());
        performer.setName(performerName);
        performer.setRoles(customUser.getRoles());
        String imgToken = ImgToken.generate(performer.getName());
        performer.setImgIdToken(imgToken);
        performerService.create(performer);
        customUser.setPerformer(performer);
        userService.update(customUser);
        return customUser;
    }

}
