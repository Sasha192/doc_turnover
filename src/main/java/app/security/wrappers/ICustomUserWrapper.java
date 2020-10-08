package app.security.wrappers;

import app.security.models.auth.CustomUser;
import javax.servlet.http.HttpServletRequest;

public interface ICustomUserWrapper {

    CustomUser retrieveUser(HttpServletRequest request);

    CustomUser retrieveUser();

    void update(CustomUser user);

}
