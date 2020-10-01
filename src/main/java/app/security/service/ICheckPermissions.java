package app.security.service;

import javax.servlet.http.HttpServletRequest;

public interface ICheckPermissions {

    public boolean check(HttpServletRequest request);

}
