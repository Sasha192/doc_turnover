package app.security.service.impl;

import app.configuration.spring.constants.Constants;
import app.models.basic.CustomUser;
import app.models.basic.Performer;
import app.security.models.SimpleRole;
import app.security.models.UserDto;
import app.security.service.IUserCreation;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.service.interfaces.IPerformerService;
import app.service.interfaces.IPerformerStatisticCreation;
import app.utils.ImgToken;
import java.util.Set;
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
        customUser.setEnabled(true);
        customUser.addRole(SimpleRole.PERFORMER);
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
        performer.setRoles(Set.copyOf(customUser.getRoles()));
        String imgToken = ImgToken.generate(performer.getName());
        performer.setImgIdToken(imgToken);
        performerService.create(performer);
        customUser.setPerformer(performer);
        userService.update(customUser);
        statisticCreation.create(performer.getId());
        return customUser;
    }
}
