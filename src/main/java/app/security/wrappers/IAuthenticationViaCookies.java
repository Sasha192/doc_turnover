package app.security.wrappers;

import app.security.models.auth.CustomUser;
import javax.servlet.http.HttpServletRequest;

public interface IAuthenticationViaCookies {

    CustomUser authenticatedViaCookies(HttpServletRequest request);

}
