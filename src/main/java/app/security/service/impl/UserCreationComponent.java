package app.security.service.impl;

import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IPerformerService;
import app.security.models.UserDto;
import app.security.models.auth.CustomUser;
import app.security.models.auth.UserInfo;
import app.security.service.IUserCreation;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.tenantconfiguration.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCreationComponent implements IUserCreation {

    private final IUserService userService;

    private final DefaultPasswordEncoder encoder;

    private final IPerformerService performerService;

    @Autowired
    public UserCreationComponent(IUserService userService,
                                 DefaultPasswordEncoder encoder,
                                 IPerformerService performerService) {
        this.userService = userService;
        this.encoder = encoder;
        this.performerService = performerService;
    }

    @Override
    public CustomUser create(UserDto dto) {
        CustomUser customUser = new CustomUser(dto, encoder);
        UserInfo info = customUser.getUserInfo();
        info.setActiveTenant(TenantContext.PHANTOM_TENANT_IDENTIFIER);
        userService.saveUserInfo(info);
        customUser.setUserInfoId(info.getId());
        userService.create(customUser);
        performerSetting(customUser);
        return customUser;
    }

    @Override
    public CustomUser create(CustomUser user) {
        userService.create(user);
        performerSetting(user);
        return user;
    }

    private void performerSetting(CustomUser customUser) {
        String prevTenant = TenantContext.getTenant();
        TenantContext.setTenant(TenantContext.PHANTOM_TENANT_IDENTIFIER);
        Performer performer = new Performer(customUser);
        performerService.create(performer);
        TenantContext.setTenant(prevTenant);
    }
}
