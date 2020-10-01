package app.security.wrappers;

import javax.servlet.http.HttpServletRequest;

public interface IAuthenticated {

    public boolean isAuthenticated(HttpServletRequest request);

}
