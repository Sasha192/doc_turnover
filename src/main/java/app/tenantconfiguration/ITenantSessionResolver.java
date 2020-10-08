package app.tenantconfiguration;

import javax.servlet.http.HttpServletRequest;

public interface ITenantSessionResolver {

    public String resolve(HttpServletRequest request);

    public String resolve();

}
