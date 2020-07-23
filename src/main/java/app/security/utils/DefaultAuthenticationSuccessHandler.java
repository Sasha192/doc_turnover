package app.security.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component("default.auth.success.handler")
public class DefaultAuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger LOGGER = Logger.getLogger("authLogger");
    private static final String DELIMETER = " | ".intern();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final Map<String, String> roleTargetUrlMap = new HashMap<>();

    public DefaultAuthenticationSuccessHandler() {
        roleTargetUrlMap.put("ROLE_USER", "/home");
        roleTargetUrlMap.put("ROLE_ADMIN", "/god/console");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse res,
                                        Authentication authentication)
            throws IOException, ServletException {
        handle(req, res, authentication);
        super.clearAuthenticationAttributes(req);
    }

    @Override
    protected void handle(HttpServletRequest req,
                          HttpServletResponse res,
                          Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (res.isCommitted()) {
            LOGGER.debug(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(req, res, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (roleTargetUrlMap.containsKey(authorityName)) {
                LOGGER.debug(new StringBuilder()
                        .append(authorityName)
                        .append(DELIMETER)
                        .append(authentication.getPrincipal())
                        .append(DELIMETER)
                        .append(LocalDateTime.now())
                        .toString());
                return roleTargetUrlMap.get(authorityName);
            }
        }
        throw new IllegalStateException();
    }

    public Map<String, String> getRoleTargetUrlMap() {
        return this.roleTargetUrlMap;
    }
}
